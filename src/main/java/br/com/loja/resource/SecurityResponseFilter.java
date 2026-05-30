package br.com.loja.resource;

import br.com.loja.dto.ErroResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SecurityResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) {

        if (responseContext.hasEntity()) {
            return;
        }

        if (responseContext.getStatus() == 401) {
            definirErro(responseContext, "Não autenticado");
        }

        if (responseContext.getStatus() == 403) {
            definirErro(responseContext, "Acesso negado");
        }
    }

    private void definirErro(
            ContainerResponseContext responseContext,
            String mensagem) {

        responseContext.setEntity(new ErroResponse(mensagem));
        responseContext.getHeaders()
                .putSingle(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }
}