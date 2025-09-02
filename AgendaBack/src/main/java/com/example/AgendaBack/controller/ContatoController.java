package com.example.AgendaBack.controller;

import com.example.AgendaBack.entity.Contato;
import com.example.AgendaBack.service.ContatoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    // GET - Listar todos os contatos
    @GetMapping
    public ResponseEntity<List<Contato>> listarTodosContatos() {
        try {
            List<Contato> contatos = contatoService.getAll();
            return ResponseEntity.ok(contatos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Buscar contato por ID
    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarContatoPorId(@PathVariable Long id) {
        try {
            Optional<Contato> contato = contatoService.getById(id);
            return contato.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Buscar contatos por nome
    @GetMapping("/buscar")
    public ResponseEntity<List<Contato>> buscarContatosPorNome(@RequestParam String nome) {
        try {
            List<Contato> contatos = contatoService.buscarPorNome(nome);
            return ResponseEntity.ok(contatos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Adicionar novo contato
    @PostMapping
    public ResponseEntity<?> adicionarContato(@Valid @RequestBody Contato contato) {
        try {
            Contato novoContato = contatoService.adicionarContato(contato);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao adicionar contato");
        }
    }

    // PUT - Editar contato existente
    @PutMapping("/{id}")
    public ResponseEntity<?> editarContato(@PathVariable Long id, @Valid @RequestBody Contato contato) {
        try {
            Contato contatoAtualizado = contatoService.editarContato(id, contato);
            return ResponseEntity.ok(contatoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao editar contato");
        }
    }

    // DELETE - Remover contato por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerContato(@PathVariable Long id) {
        try {
            contatoService.removerContatoPorId(id);
            return ResponseEntity.ok().body("Contato removido com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao remover contato");
        }
    }

    // GET - Health check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API de Contatos está funcionando!");
    }
}