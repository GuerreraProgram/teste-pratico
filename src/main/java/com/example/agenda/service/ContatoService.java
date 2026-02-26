package com.example.agenda.service;

import com.example.agenda.dto.ContatoRequestDTO;
import com.example.agenda.dto.ContatoResponseDTO;
import com.example.agenda.dto.TelefoneDTO;
import com.example.agenda.entity.Contato;
import com.example.agenda.entity.Telefone;
import com.example.agenda.repository.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    @Transactional
    public ContatoResponseDTO criar(ContatoRequestDTO dto) {
        Contato contato = new Contato();
        contato.setNomeContato(dto.getNomeContato());
        contato.setIdadeContato(dto.getIdadeContato());

        List<Telefone> telefones = new ArrayList<>();
        if (dto.getTelefones() != null) {
            for (String numero : dto.getTelefones()) {
                if (numero != null && !numero.isBlank()) {
                    Telefone telefone = new Telefone();
                    telefone.setNumeroTelefone(numero);
                    telefone.setContato(contato);
                    telefones.add(telefone);
                }
            }
        }
        contato.setTelefones(telefones);

        Contato salvo = contatoRepository.save(contato);
        registrarEmArquivo("adicionado", salvo.getNomeContato());
        return mapToResponse(salvo);
    }

    @Transactional
    public ContatoResponseDTO atualizar(Long id, ContatoRequestDTO dto) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));

        contato.setNomeContato(dto.getNomeContato());
        contato.setIdadeContato(dto.getIdadeContato());

        contato.getTelefones().clear();
        if (dto.getTelefones() != null) {
            for (String numero : dto.getTelefones()) {
                if (numero != null && !numero.isBlank()) {
                    Telefone telefone = new Telefone();
                    telefone.setNumeroTelefone(numero);
                    telefone.setContato(contato);
                    contato.getTelefones().add(telefone);
                }
            }
        }

        Contato salvo = contatoRepository.save(contato);
        registrarEmArquivo("editado", salvo.getNomeContato());
        return mapToResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<ContatoResponseDTO> listar(String nome, String telefone) {
        String nomeFiltro = (nome != null && !nome.isBlank()) ? nome.trim() : null;
        String telefoneFiltro = (telefone != null && !telefone.isBlank()) ? telefone.trim() : null;

        List<Contato> contatos;
        if (nomeFiltro == null && telefoneFiltro == null) {
            contatos = contatoRepository.findAll();
        } else {
            List<Long> ids = contatoRepository.pesquisarIds(nomeFiltro, telefoneFiltro);
            if (ids.isEmpty()) {
                contatos = List.of();
            } else {
                contatos = contatoRepository.findByIdContatoInWithTelefones(ids);
            }
        }

        return contatos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContatoResponseDTO buscarPorId(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));
        return mapToResponse(contato);
    }

    @Transactional
    public void excluir(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));

        String nomeContato = contato.getNomeContato();

        contatoRepository.delete(contato);

        registrarEmArquivo("excluído", nomeContato);
    }

    private ContatoResponseDTO mapToResponse(Contato contato) {
        ContatoResponseDTO dto = new ContatoResponseDTO();
        dto.setIdContato(contato.getIdContato());
        dto.setNomeContato(contato.getNomeContato());
        dto.setIdadeContato(contato.getIdadeContato());

        List<TelefoneDTO> telefones = contato.getTelefones()
                .stream()
                .filter(Objects::nonNull)
                .map(t -> {
                    TelefoneDTO tdto = new TelefoneDTO();
                    tdto.setIdTelefone(t.getIdTelefone());
                    tdto.setNumeroTelefone(t.getNumeroTelefone());
                    return tdto;
                })
                .collect(Collectors.toList());
        dto.setTelefones(telefones);
        return dto;
    }

    private void registrarEmArquivo(String acao, String nomeContato) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String linha = timestamp + " - Contato " + acao + ": " + nomeContato + System.lineSeparator();

        Path path = Path.of("contatos_log.txt");
        try {
            Files.write(path, linha.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

