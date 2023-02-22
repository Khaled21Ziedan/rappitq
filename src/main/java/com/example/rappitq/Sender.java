package com.example.rappitq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.AMQBasicProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Sender implements CommandLineRunner {
    private static final String EXCHANGE_NAME = "ab-training-exchange";
    private static final String QUEUE_NAME = "ab-training-queue";
    private static final String ROUTING_KEY = "ab-training-rout";

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (Connection connection = connectionFactory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,false, builder.build(), "hello last".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
