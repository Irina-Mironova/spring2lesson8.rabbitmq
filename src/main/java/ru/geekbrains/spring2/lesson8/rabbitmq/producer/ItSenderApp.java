package ru.geekbrains.spring2.lesson8.rabbitmq.producer;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class ItSenderApp {
    private final static String EXCHANGE_NAME = "it_blog_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            System.out.println("Введите тему и сообщение: ");
            Scanner sc = new Scanner(System.in);
            String message = sc.nextLine();
            String[] arr = message.split(" ", 2);

            if (!arr[0].trim().isEmpty()) {
                channel.basicPublish(EXCHANGE_NAME, arr[0], null, arr[1].getBytes());
                System.out.println("Сообщение отправлено");
            }
        }
        ;
    }
}

