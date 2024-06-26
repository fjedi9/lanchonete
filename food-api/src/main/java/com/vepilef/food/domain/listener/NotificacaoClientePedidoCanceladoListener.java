package com.vepilef.food.domain.listener;

import com.vepilef.food.domain.event.PedidoCanceladoEvent;
import com.vepilef.food.domain.event.PedidoConfirmadoEvent;
import com.vepilef.food.domain.model.Pedido;
import com.vepilef.food.domain.service.EnvioEmailService;
import com.vepilef.food.domain.service.EnvioEmailService.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoCanceladoListener {

    @Autowired
    private EnvioEmailService envioEmailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoCancelarPedido(PedidoCanceladoEvent event) {
        Pedido pedido = event.getPedido();

        var mensagem = Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - Pedido Cancelado")
                .corpo("emails/pedido-cancelado.html")
                .variavel("pedido", pedido)
                .destinatario(pedido.getCliente().getEmail())
                .build();

        envioEmailService.enviar(mensagem);
    }

}
