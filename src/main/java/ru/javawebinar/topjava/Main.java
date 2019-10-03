package ru.javawebinar.topjava;

import java.util.function.Consumer;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        execute(() -> {
            System.out.println("Hello Topjava Enterprise!");
        });
        consume(System.out::println, "Hello Topjava Enterprise!");
    }

    private static void execute(Runnable runnable) {
        System.out.println("Start runner");
        runnable.run();
        System.out.println("End runner");
    }

    private static <String> void consume(Consumer<String> consumer, String out) {
        System.out.println("Start runner");
        consumer.accept(out);
        System.out.println("End runner");
    }
}