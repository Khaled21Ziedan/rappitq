package com.example.rappitq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class HeadersSender implements CommandLineRunner {
    private static final String EXCHANGE_NAME = "ab-training-headers-exchange";
    private static final String QUEUE_NAME = "ab-training-headers-queue-1";

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (Connection connection = connectionFactory.newConnection()) {
            Channel channel = connection.createChannel();
            Map<String, Object> header = new HashMap<>();
            header.put("A","1");
            channel.exchangeDeclare(EXCHANGE_NAME, "headers");
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME,"",header);
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
            channel.basicPublish(" ",QUEUE_NAME,false, builder.build(), "مرحبا".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
