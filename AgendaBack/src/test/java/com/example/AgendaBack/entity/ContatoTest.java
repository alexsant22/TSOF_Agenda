package com.example.AgendaBack.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContatoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void devePassarParaEmailValido() {
        Contato contato = new Contato();
        contato.setNome("Teste");
        contato.setEmail("teste@valido.com");
        contato.setTelefone("+55 11 99999-9999");

        assertTrue(validator.validate(contato).isEmpty());
    }

    @Test
    void deveFalharParaEmailInvalido() {
        Contato contato = new Contato();
        contato.setNome("Teste");
        contato.setEmail("email-invalido");
        contato.setTelefone("+55 11 99999-9999");

        assertFalse(validator.validate(contato).isEmpty());
    }

    @Test
    void deveAceitarTelefoneVazio() {
        Contato contato = new Contato();
        contato.setNome("Teste");
        contato.setEmail("teste@valido.com");
        contato.setTelefone("");

        assertTrue(validator.validate(contato).isEmpty());
    }
}