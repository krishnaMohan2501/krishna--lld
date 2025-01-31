package com.mc.lld.conpool;

public class ConnPoolDriver {
    public static void main(String[] args) throws InterruptedException {
        ObjectPoolManager<String> pool = new ObjectPoolManager<>(10, 3, () -> "Resource");

        Runnable task = () -> {
            String userId = Thread.currentThread().getName();
            try {
                String resource = pool.borrowObject(userId);
                System.out.println(userId + " acquired " + resource);
                Thread.sleep(1000); // Simulate work
                pool.returnObject(userId, resource);
                System.out.println(userId + " returned " + resource);
            } catch (Exception e) {
                System.err.println(userId + " failed: " + e.getMessage());
            }
        };

        // Simulate multiple users
        for (int i = 0; i < 5; i++) {
            new Thread(task, "User" + (i % 2)).start(); // 2 users (User0, User1)
        }
    }

}

