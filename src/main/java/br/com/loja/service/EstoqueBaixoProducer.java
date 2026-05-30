package br.com.loja.service;

import br.com.loja.entity.Produto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class EstoqueBaixoProducer {

    @Inject
    @Channel("estoque-baixo-out")
    Emitter<String> emitter;

    public void enviarAlertaSeNecessario(Produto produto) {
        if (produto.estoque < 5) {
            String unidade = produto.estoque == 1 ? "unidade" : "unidades";

            String mensagem = String.format(
                    "Produto '%s' com estoque baixo (%d %s). " +
                            "E-mail de alerta enviado para estoque@empresa.com",
                    produto.nome,
                    produto.estoque,
                    unidade
            );

            emitter.send(mensagem);
        }
    }
}