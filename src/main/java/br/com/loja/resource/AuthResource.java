package br.com.loja.resource;

import br.com.loja.dto.ErroResponse;
import br.com.loja.dto.LoginRequest;
import br.com.loja.dto.LoginResponse;
import br.com.loja.dto.RegisterRequest;
import br.com.loja.dto.UsuarioResponse;
import br.com.loja.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    public Response registrar(RegisterRequest request) {
        if (request == null ||
                campoVazio(request.nome) ||
                campoVazio(request.email) ||
                campoVazio(request.senha) ||
                campoVazio(request.role)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErroResponse("Campos obrigatórios não informados"))
                    .build();
        }

        if (!request.role.equals("ADMIN") && !request.role.equals("USER")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErroResponse("Role inválida"))
                    .build();
        }

        if (authService.emailJaCadastrado(request.email)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErroResponse("E-mail já cadastrado"))
                    .build();
        }

        UsuarioResponse response = authService.registrar(request);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null ||
                campoVazio(request.email) ||
                campoVazio(request.senha)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErroResponse("E-mail ou senha inválidos"))
                    .build();
        }

        LoginResponse response = authService.login(request);

        if (response == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErroResponse("E-mail ou senha inválidos"))
                    .build();
        }

        return Response.ok(response).build();
    }

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}