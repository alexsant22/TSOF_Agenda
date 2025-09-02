package com.example.AgendaBack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contatos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "uk_contato_email"),
                @UniqueConstraint(columnNames = "telefone", name = "uk_contato_telefone")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome não pode estar vazio")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Pattern(regexp = "^$|^\\+55 \\d{2} \\d{5}-\\d{4}$",
            message = "Telefone deve estar no formato +55 XX XXXXX-XXXX ou vazio")
    @Column(nullable = true, length = 20, unique = true)
    private String telefone;

    @NotBlank(message = "Email não pode estar vazio")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, length = 100, unique = true)
    private String email;
}