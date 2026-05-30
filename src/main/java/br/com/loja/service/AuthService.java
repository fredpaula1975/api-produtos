package br.com.loja.service;

import br.com.loja.dto.LoginRequest;
import br.com.loja.dto.LoginResponse;
import br.com.loja.dto.RegisterRequest;
import br.com.loja.dto.UsuarioResponse;
import br.com.loja.entity.Usuario;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Transactional
    public UsuarioResponse registrar(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.nome = request.nome;
        usuario.email = request.email;
        usuario.senha = BcryptUtil.bcryptHash(request.senha);
        usuario.role = request.role;

        usuario.persist();

        return new UsuarioResponse(usuario.id, usuario.nome, usuario.email, usuario.role);
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = Usuario.find("email", request.email).firstResult();

        if (usuario == null || !BcryptUtil.matches(request.senha, usuario.senha)) {
            return null;
        }

        String token = Jwt.issuer("api-produtos")
                .subject(usuario.email)
                .groups(Set.of(usuario.role))
                .expiresIn(Duration.ofHours(1))
                .sign();

        return new LoginResponse(token, "Bearer", usuario.role);
    }

    public boolean emailJaCadastrado(String email) {
        Usuario usuario = Usuario.find("email", email).firstResult();
        return usuario != null;
    }
}