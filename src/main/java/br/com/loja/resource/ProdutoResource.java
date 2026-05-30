package br.com.loja.resource;

import br.com.loja.dto.ErroResponse;
import br.com.loja.dto.ProdutoRequest;
import br.com.loja.entity.Produto;
import br.com.loja.service.EstoqueBaixoProducer;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Inject
    EstoqueBaixoProducer estoqueBaixoProducer;

    @GET
    @RolesAllowed({"USER", "ADMIN"})
    @CacheResult(cacheName = "produtos")
    public List<Produto> listar() {
        return Produto.listAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    @CacheResult(cacheName = "produto-por-id")
    public Response buscarPorId(@PathParam("id") Long id) {
        Produto produto = Produto.findById(id);

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErroResponse("Produto não encontrado"))
                    .build();
        }

        return Response.ok(produto).build();
    }

    @POST
    @Transactional
    @RolesAllowed("ADMIN")
    @CacheInvalidateAll(cacheName = "produtos")
    @CacheInvalidateAll(cacheName = "produto-por-id")
    public Response cadastrar(ProdutoRequest request) {
        if (request == null || camposInvalidos(request)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErroResponse("Campos obrigatórios não informados"))
                    .build();
        }

        Produto produto = new Produto();
        produto.nome = request.nome;
        produto.descricao = request.descricao;
        produto.preco = request.preco;
        produto.estoque = request.estoque;
        produto.persist();
        estoqueBaixoProducer.enviarAlertaSeNecessario(produto);

        return Response.status(Response.Status.CREATED)
                .entity(produto)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ADMIN")
    @CacheInvalidateAll(cacheName = "produtos")
    @CacheInvalidateAll(cacheName = "produto-por-id")
    public Response atualizar(@PathParam("id") Long id, ProdutoRequest request) {
        Produto produto = Produto.findById(id);

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErroResponse("Produto não encontrado"))
                    .build();
        }

        if (request == null || camposInvalidos(request)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErroResponse("Campos obrigatórios não informados"))
                    .build();
        }

        produto.nome = request.nome;
        produto.descricao = request.descricao;
        produto.preco = request.preco;
        produto.estoque = request.estoque;

        estoqueBaixoProducer.enviarAlertaSeNecessario(produto);

        return Response.ok(produto).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ADMIN")
    @CacheInvalidateAll(cacheName = "produtos")
    @CacheInvalidateAll(cacheName = "produto-por-id")
    public Response remover(@PathParam("id") Long id) {
        Produto produto = Produto.findById(id);

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErroResponse("Produto não encontrado"))
                    .build();
        }

        produto.delete();

        return Response.noContent().build();
    }

    private boolean camposInvalidos(ProdutoRequest request) {
        return campoVazio(request.nome)
                || campoVazio(request.descricao)
                || request.preco == null
                || request.estoque == null;
    }

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}