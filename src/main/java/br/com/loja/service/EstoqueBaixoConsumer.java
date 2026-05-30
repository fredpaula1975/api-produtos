package br.com.loja.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EstoqueBaixoConsumer {

    private static final Logger LOG = Logger.getLogger(EstoqueBaixoConsumer.class);

    @Incoming("estoque-baixo-in")
    public void receberAlerta(String mensagem) {
        LOG.info("[ALERTA DE ESTOQUE] " + mensagem);
    }
}