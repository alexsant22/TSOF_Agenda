package com.example.AgendaBack.controller;

import com.example.AgendaBack.entity.Contato;
import com.example.AgendaBack.service.ContatoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    // GET - Listar todos os contatos
    @GetMapping
    public List<Contato> listarTodosContatos() {
        return contatoService.getAll();
    }

    // GET - Buscar contato por ID
    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarContatoPorId(@PathVariable Long id) {
        return contatoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Buscar contatos por nome
    @GetMapping("/buscar")
    public List<Contato> buscarContatosPorNome(@RequestParam String nome) {
        return contatoService.buscarPorNome(nome);
    }

    // POST - Adicionar novo contato
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Define o status de sucesso aqui
    public Contato adicionarContato(@Valid @RequestBody Contato contato) {
        return contatoService.adicionarContato(contato);
    }

    // PUT - Editar contato existente
    @PutMapping("/{id}")
    public Contato editarContato(@PathVariable Long id, @Valid @RequestBody Contato contato) {
        return contatoService.editarContato(id, contato);
    }

    // DELETE - Remover contato por ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Define o status de sucesso aqui
    public ResponseEntity<String> removerContato(@PathVariable Long id) {
        contatoService.removerContatoPorId(id);
        return ResponseEntity.ok("Contato removido com sucesso");
    }

}