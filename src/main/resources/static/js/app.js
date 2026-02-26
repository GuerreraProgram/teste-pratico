const apiBaseUrl = '/api/contatos';

let mensagemTimeoutId = null;

const exibirMensagem = (tipo, mensagem) => {
    const $div = $('#div_mensagens');

    if (mensagemTimeoutId) {
        clearTimeout(mensagemTimeoutId);
        mensagemTimeoutId = null;
    }

    if (!mensagem) {
        $div.stop(true, true)
            .hide()
            .removeClass('alert alert-danger alert-success')
            .text('');
        return;
    }

    const classeBase = 'alert';
    const classeTipo = tipo === 'sucesso' ? 'alert-success' : 'alert-danger';

    $div
        .stop(true, true)
        .removeClass('alert-danger alert-success')
        .addClass(classeBase + ' ' + classeTipo)
        .text(mensagem)
        .fadeIn(200);

    mensagemTimeoutId = setTimeout(() => {
        $div.fadeOut(300, () => {
            $div.removeClass('alert alert-danger alert-success').text('');
        });
        mensagemTimeoutId = null;
    }, 2000);
};

const formatarTelefone = (valor) => {
    if (!valor) {
        return '';
    }

    const digitos = valor.replace(/\D/g, '').substring(0, 11);

    if (digitos.length === 0) {
        return '';
    }

    if (digitos.length <= 2) {
        return '(' + digitos;
    }

    if (digitos.length <= 7) {
        return '(' + digitos.substring(0, 2) + ') ' + digitos.substring(2);
    }

    const ddd = digitos.substring(0, 2);
    const parte1 = digitos.substring(2, 7);
    const parte2 = digitos.substring(7);

    return '(' + ddd + ') ' + parte1 + '-' + parte2;
};

const configurarMascarasTelefone = () => {
    $(document).off('input', '.input-telefone').on('input', '.input-telefone', (event) => {
        const $campo = $(event.currentTarget);
        const valor = $campo.val();
        const formatado = formatarTelefone(valor);
        $campo.val(formatado);
    });

    $('#input_filtro_telefone').off('input').on('input', (event) => {
        const $campo = $(event.currentTarget);
        const valor = $campo.val();
        const formatado = formatarTelefone(valor);
        $campo.val(formatado);
    });
};

const montarObjetoContato = () => {
    const telefones = [];
    $('.input-telefone').each((index, element) => {
        const valor = $(element).val();
        if (valor && valor.trim().length > 0) {
            const somenteDigitos = valor.replace(/\D/g, '');
            telefones.push(somenteDigitos);
        }
    });

    return {
        nomeContato: $('#input_nome_contato').val(),
        idadeContato: $('#input_idade_contato').val() ? parseInt($('#input_idade_contato').val(), 10) : null,
        telefones: telefones
    };
};

const limparFormulario = () => {
    $('#input_id_contato').val('');
    $('#input_nome_contato').val('');
    $('#input_idade_contato').val('');
    $('#div_telefones').empty();
    $('#div_telefones').append(
        '<label class="form-label">Telefones</label>' +
        '<div class="row mb-2 div-telefone-linha">' +
        '<div class="col-md-4">' +
        '<input type="text" class="form-control input-telefone" placeholder="(99) 99999-9999">' +
        '</div>' +
        '</div>'
    );
};

const adicionarLinhaTelefone = () => {
    const html = '' +
        '<div class="row mb-2 div-telefone-linha">' +
        '<div class="col-md-4">' +
        '<input type="text" class="form-control input-telefone" placeholder="(99) 99999-9999">' +
        '</div>' +
        '<div class="col-md-2">' +
        '<button type="button" class="btn btn-danger btn-telefone-remove">Remover</button>' +
        '</div>' +
        '</div>';
    $('#div_telefones').append(html);
};

const carregarContatos = (filtros) => {
    const params = {};
    if (filtros && filtros.nome) {
        params.nome = filtros.nome;
    }
    if (filtros && filtros.telefone) {
        params.telefone = filtros.telefone;
    }

    axios.get(apiBaseUrl, { params: params })
        .then((response) => {
            const dados = response.data || [];
            const $tbody = $('#tbody_contatos');
            $tbody.empty();

            dados.forEach((contato) => {
                const telefonesStr = (contato.telefones || [])
                    .map((t) => formatarTelefone(t.numeroTelefone))
                    .join(', ');

                const linha = '' +
                    '<tr>' +
                    '<td>' + contato.nomeContato + '</td>' +
                    '<td>' + (contato.idadeContato != null ? contato.idadeContato : '') + '</td>' +
                    '<td>' + telefonesStr + '</td>' +
                    '<td>' +
                    '<button type="button" class="btn btn-sm btn-primary btn-editar" data-id="' + contato.idContato + '">Editar</button> ' +
                    '<button type="button" class="btn btn-sm btn-danger btn-excluir" data-id="' + contato.idContato + '">Excluir</button>' +
                    '</td>' +
                    '</tr>';
                $tbody.append(linha);
            });
        })
        .catch(() => {
            exibirMensagem('erro', 'Erro ao carregar contatos.');
        });
};

const carregarContatoParaEdicao = (id) => {
    axios.get(apiBaseUrl + '/' + id)
        .then((response) => {
            const contato = response.data;
            $('#input_id_contato').val(contato.idContato);
            $('#input_nome_contato').val(contato.nomeContato);
            $('#input_idade_contato').val(contato.idadeContato);

            $('#div_telefones').empty();
            $('#div_telefones').append('<label class="form-label">Telefones</label>');

            if (contato.telefones && contato.telefones.length > 0) {
                contato.telefones.forEach((t) => {
                    const html = '' +
                        '<div class="row mb-2 div-telefone-linha">' +
                        '<div class="col-md-4">' +
                        '<input type="text" class="form-control input-telefone" value="' + formatarTelefone(t.numeroTelefone) + '">' +
                        '</div>' +
                        '<div class="col-md-2">' +
                        '<button type="button" class="btn btn-danger btn-telefone-remove">Remover</button>' +
                        '</div>' +
                        '</div>';
                    $('#div_telefones').append(html);
                });
            } else {
                $('#div_telefones').append(
                    '<div class="row mb-2 div-telefone-linha">' +
                    '<div class="col-md-4">' +
                    '<input type="text" class="form-control input-telefone" placeholder="(99) 99999-9999">' +
                    '</div>' +
                    '</div>'
                );
            }
        })
        .catch(() => {
            exibirMensagem('erro', 'Erro ao carregar contato para edição.');
        });
};

const excluirContato = (id) => {
    if (!window.confirm('Confirma a exclusão deste contato?')) {
        return;
    }

    axios.delete(apiBaseUrl + '/' + id)
        .then(() => {
            carregarContatos();
        })
        .catch(() => {
            exibirMensagem('erro', 'Erro ao excluir contato.');
        });
};

$(document).ready(() => {
    carregarContatos();

    $('#form_contato').off('submit').on('submit', (event) => {
        event.preventDefault();

        const objeto = montarObjetoContato();
        if (!objeto.nomeContato || objeto.nomeContato.trim().length === 0) {
            exibirMensagem('erro', 'Informe o nome do contato.');
            return;
        }

        const id = $('#input_id_contato').val();
        if (id) {
            axios.put(apiBaseUrl + '/' + id, objeto)
                .then(() => {
                    limparFormulario();
                    carregarContatos();
                    exibirMensagem('sucesso', 'Contato atualizado com sucesso.');
                })
                .catch(() => {
                    exibirMensagem('erro', 'Erro ao atualizar contato.');
                });
        } else {
            axios.post(apiBaseUrl, objeto)
                .then(() => {
                    limparFormulario();
                    carregarContatos();
                    exibirMensagem('sucesso', 'Contato salvo com sucesso.');
                })
                .catch(() => {
                    exibirMensagem('erro', 'Erro ao salvar contato.');
                });
        }
    });

    $('#btn_adicionar_telefone').off('click').on('click', () => {
        adicionarLinhaTelefone();
    });

    $('#btn_limpar').off('click').on('click', () => {
        limparFormulario();
    });

    $('#btn_pesquisar').off('click').on('click', () => {
        const nome = $('#input_filtro_nome').val();
        const telefone = $('#input_filtro_telefone').val();
        const telefoneSomenteDigitos = telefone ? telefone.replace(/\D/g, '') : null;
        carregarContatos({ nome: nome, telefone: telefoneSomenteDigitos });
    });

    $('#btn_listar_todos').off('click').on('click', () => {
        $('#input_filtro_nome').val('');
        $('#input_filtro_telefone').val('');
        carregarContatos();
    });

    $(document).off('click', '.btn-telefone-remove').on('click', '.btn-telefone-remove', (event) => {
        const $linha = $(event.currentTarget).closest('.div-telefone-linha');
        $linha.remove();
    });

    $(document).off('click', '.btn-editar').on('click', '.btn-editar', (event) => {
        const id = $(event.currentTarget).data('id');
        carregarContatoParaEdicao(id);
    });

    $(document).off('click', '.btn-excluir').on('click', '.btn-excluir', (event) => {
        const id = $(event.currentTarget).data('id');
        excluirContato(id);
    });

    configurarMascarasTelefone();
});

