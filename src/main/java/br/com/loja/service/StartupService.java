package br.com.loja.service;

import br.com.loja.entity.Usuario;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StartupService {

    @Transactional
    void onStart(@Observes StartupEvent event) {
        criarUsuarioPadrao("Admin Sistema", "admin@loja.com", "admin123", "ADMIN");
        criarUsuarioPadrao("User Padrao", "user@loja.com", "user123", "USER");
    }

    private void criarUsuarioPadrao(String nome, String email, String senha, String role) {
        Usuario usuarioExistente = Usuario.find("email", email).firstResult();

        if (usuarioExistente != null) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.nome = nome;
        usuario.email = email;
        usuario.senha = BcryptUtil.bcryptHash(senha);
        usuario.role = role;
        usuario.persist();
    }
}