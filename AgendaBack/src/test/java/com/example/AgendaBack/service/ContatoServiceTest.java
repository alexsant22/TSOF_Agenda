package com.example.AgendaBack.service;

import com.example.AgendaBack.entity.Contato;
import com.example.AgendaBack.repository.ContatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {

    @Mock
    private ContatoRepository repository;

    @InjectMocks
    private ContatoService service;

    private Contato contatoValido;

    @BeforeEach
    void setUp() {
        contatoValido = new Contato();
        contatoValido.setId(1L);
        contatoValido.setNome("João Silva");
        contatoValido.setEmail("joao@email.com");
        contatoValido.setTelefone("+55 11 99999-9999");
    }

    @Test
    void deveAdicionarContatoComSucesso() {
        when(repository.existsByEmail(any())).thenReturn(false);
        when(repository.existsByTelefone(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(contatoValido);

        Contato resultado = service.adicionarContato(contatoValido);

        assertNotNull(resultado);
        verify(repository).save(contatoValido);
    }

    @Test
    void deveLancarExcecaoParaEmailDuplicado() {
        when(repository.existsByEmail(any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarContato(contatoValido)
        );
    }

    @Test
    void deveBuscarTodosContatos() {
        when(repository.findAll()).thenReturn(Arrays.asList(contatoValido));

        List<Contato> resultado = service.getAll();

        assertEquals(1, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void deveBuscarContatoPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(contatoValido));

        Optional<Contato> resultado = service.getById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(contatoValido.getEmail(), resultado.get().getEmail());
    }

    @Test
    void deveValidarFormatoTelefoneInvalido() {
        Contato contatoInvalido = new Contato();
        contatoInvalido.setTelefone("11-99999-9999");

        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarContato(contatoInvalido)
        );
    }

    @Test
    void devePermitirTelefoneVazio() {
        contatoValido.setTelefone("");
        when(repository.existsByEmail(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(contatoValido);

        assertDoesNotThrow(() -> service.adicionarContato(contatoValido));
    }
}