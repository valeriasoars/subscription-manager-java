package com.dev.subscriptionmanager.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_ASSINATURAS = "exchange.assinaturas";

    public static final String FILA_CRIACAO = "queue.assinatura.criacao";
    public static final String FILA_PAGAMENTO = "queue.assinatura.pagamento";

    public static final String ROUTING_KEY_CRIACAO = "assinatura.criada";
    public static final String ROUTING_KEY_PAGAMENTO = "pagamento.processado";

    @Bean
    public TopicExchange assinaturaExchange() {
        return new TopicExchange(EXCHANGE_ASSINATURAS);
    }

    @Bean
    public Queue filaCriacao() {
        return new Queue(FILA_CRIACAO, true);
    }

    @Bean
    public Queue filaPagamento() {
        return new Queue(FILA_PAGAMENTO, true);
    }

    @Bean
    public Binding bindingCriacao(Queue filaCriacao, TopicExchange assinaturaExchange) {
        return BindingBuilder.bind(filaCriacao).to(assinaturaExchange).with(ROUTING_KEY_CRIACAO);
    }

    @Bean
    public Binding bindingPagamento(Queue filaPagamento, TopicExchange assinaturaExchange) {
        return BindingBuilder.bind(filaPagamento).to(assinaturaExchange).with(ROUTING_KEY_PAGAMENTO);
    }
}
