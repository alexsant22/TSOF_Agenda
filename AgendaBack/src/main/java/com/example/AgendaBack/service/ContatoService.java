package com.example.AgendaBack.service;

import com.example.AgendaBack.entity.Contato;
import com.example.AgendaBack.repository.ContatoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ContatoService {

    private final ContatoRepository repository;

    @Autowired
    public ContatoService(ContatoRepository repository) {
        this.repository = repository;
    }

    // Adicionar contato com validações
    public Contato adicionarContato(@Valid Contato contato) {
        validarTelefone(contato.getTelefone());
        validarEmail(contato.getEmail());

        // Verificar duplicata de email
        if (repository.existsByEmail(contato.getEmail().trim())) {
            throw new IllegalArgumentException("Já existe um contato com este email");
        }

        // Verificar duplicata de telefone (se fornecido)
        if (contato.getTelefone() != null && !contato.getTelefone().trim().isEmpty()) {
            if (repository.existsByTelefone(contato.getTelefone().trim())) {
                throw new IllegalArgumentException("Já existe um contato com este telefone");
            }
        }

        return repository.save(contato);
    }

    // Buscar todos os contatos
    public List<Contato> getAll() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<Contato> getById(Long id) {
        return repository.findById(id);
    }

    // Buscar por nome
    public List<Contato> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        return repository.findByNomeContainingIgnoreCase(nome.trim());
    }

    // Editar contato
    public Contato editarContato(Long id, @Valid Contato contatoAtualizado) {
        Contato contatoExistente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contato não encontrado"));

        validarTelefone(contatoAtualizado.getTelefone());
        validarEmail(contatoAtualizado.getEmail());

        // Verificar duplicata de email (excluindo o atual)
        if (repository.existsByEmailAndIdNot(contatoAtualizado.getEmail().trim(), id)) {
            throw new IllegalArgumentException("Já existe outro contato com este email");
        }

        // Verificar duplicata de telefone (excluindo o atual, se fornecido)
        if (contatoAtualizado.getTelefone() != null && !contatoAtualizado.getTelefone().trim().isEmpty()) {
            if (repository.existsByTelefoneAndIdNot(contatoAtualizado.getTelefone().trim(), id)) {
                throw new IllegalArgumentException("Já existe outro contato com este telefone");
            }
        }

        contatoExistente.setNome(contatoAtualizado.getNome().trim());
        contatoExistente.setTelefone(contatoAtualizado.getTelefone() != null ?
                contatoAtualizado.getTelefone().trim() : null);
        contatoExistente.setEmail(contatoAtualizado.getEmail().trim());

        return repository.save(contatoExistente);
    }

    // Remover contato por ID
    public void removerContatoPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Contato não encontrado");
        }
        repository.deleteById(id);
    }

    // Métodos auxiliares de validação
    private void validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return;
        }

        String telefoneLimpo = telefone.trim();
        if (!telefoneLimpo.matches("^\\+55 \\d{2} \\d{5}-\\d{4}$")) {
            throw new IllegalArgumentException("Formato de telefone inválido. Use: +55 XX XXXXX-XXXX");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode estar vazio");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
}