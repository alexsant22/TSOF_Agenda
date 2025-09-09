package com.example.AgendaBack.controller;

import com.example.AgendaBack.entity.Contato;
import com.example.AgendaBack.service.ContatoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContatoController.class)
class ContatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContatoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private final Contato contatoValido = new Contato(
            1L,
            "Maria Santos",
            "+55 11 98888-7777",
            "maria@email.com"
    );

    @Test
    void deveRetornar201AoAdicionarContato() throws Exception {
        when(service.adicionarContato(any())).thenReturn(contatoValido);

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contatoValido)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornar200AoBuscarPorId() throws Exception {
        when(service.getById(1L)).thenReturn(Optional.of(contatoValido));

        mockMvc.perform(get("/api/contatos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    void deveRetornar404ParaIdInexistente() throws Exception {
        when(service.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/contatos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        Contato contatoInvalido = new Contato();
        contatoInvalido.setNome(""); // Nome vazio

        mockMvc.perform(post("/api/contatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contatoInvalido)))
                .andExpect(status().isBadRequest());
    }
}