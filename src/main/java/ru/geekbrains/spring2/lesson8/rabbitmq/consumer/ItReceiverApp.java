package ru.geekbrains.spring2.lesson8.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class ItReceiverApp {
    private final static String EXCHANGE_NAME = "it_blog_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        System.out.println("Waiting message...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received message: " + message);

        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        while (flag) {
            System.out.println("Меню: ");
            System.out.println("1 : Новая подписка");
            System.out.println("2 : Удаление подписки");
            System.out.println("0 : Выход из меню");

            System.out.println("Выберите пункт меню:");
            int menu = sc.nextInt();
            sc.nextLine();
            switch (menu) {
                case 1:
                    System.out.println("Укажите название подписки (set_topic название): ");
                    String setTopic = sc.nextLine();
                    String[] arr = setTopic.split(" ", 2);
                    channel.queueBind(queueName, EXCHANGE_NAME, arr[1]);
                    System.out.println("Вы подписаны на " + arr[1]);
                    break;

                case 2:
                    System.out.println("Укажите название подписки, которую нужно удалить (delete_topic название): ");
                    String deleteTopic = sc.nextLine();
                    String[] arr1 = deleteTopic.split(" ", 2);
                    channel.queueUnbind(queueName, EXCHANGE_NAME, arr1[1]);
                    System.out.println("Подписка на " + arr1[1] + " удалена");
                    break;

                case 0:
                    sc.close();
                    flag = false;
                    break;
            }
        }
    }
}
