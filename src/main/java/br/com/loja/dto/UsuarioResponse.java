package br.com.loja.dto;

public class UsuarioResponse {

    public Long id;

    public String nome;

    public String email;

    public String role;

    public UsuarioResponse(Long id, String nome, String email, String role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
    }
}
