package com.example.rappitq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultSender implements CommandLineRunner {
    private static final String EXCHANGE_NAME = "ab-training-dafault-exchange";
    private static final String QUEUE_NAME_1 = "ab-training-queue-1";
    private static final String QUEUE_NAME_2 = "ab-training-queue-2";
    private static final String QUEUE_NAME_3 = "ab-training-queue-3";
    private static final String ROUTING_1_KEY = "greeting.#";
    private static final String ROUTING_2_KEY = "greeting.arabic";
    private static final String ROUTING_3_KEY = "greeting.english";

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (Connection connection = connectionFactory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.queueDeclare(QUEUE_NAME_1, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_2, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_3, true, false, false, null);
            channel.queueBind(QUEUE_NAME_1, EXCHANGE_NAME, ROUTING_1_KEY);
            channel.queueBind(QUEUE_NAME_2, EXCHANGE_NAME, ROUTING_2_KEY);
            channel.queueBind(QUEUE_NAME_3, EXCHANGE_NAME, ROUTING_3_KEY);
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
            channel.basicPublish(" ",QUEUE_NAME_2,false, builder.build(), "مرحبا".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
