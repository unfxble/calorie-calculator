package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private final static AtomicInteger counter = new AtomicInteger(0);
    private IdGenerator() {}

    public static Integer nextId() {
        return counter.incrementAndGet();
    }
}