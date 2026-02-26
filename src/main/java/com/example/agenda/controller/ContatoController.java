package com.example.agenda.controller;

import com.example.agenda.dto.ContatoRequestDTO;
import com.example.agenda.dto.ContatoResponseDTO;
import com.example.agenda.service.ContatoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contatos")
@CrossOrigin(origins = "*")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @PostMapping
    public ResponseEntity<ContatoResponseDTO> criar(@RequestBody ContatoRequestDTO dto) {
        ContatoResponseDTO criado = contatoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponseDTO> atualizar(@PathVariable Long id,
                                                        @RequestBody ContatoRequestDTO dto) {
        ContatoResponseDTO atualizado = contatoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping
    public ResponseEntity<List<ContatoResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String telefone) {
        List<ContatoResponseDTO> lista = contatoService.listar(nome, telefone);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponseDTO> buscarPorId(@PathVariable Long id) {
        ContatoResponseDTO dto = contatoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        contatoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

