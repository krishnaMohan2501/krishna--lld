package com.mc.lld.multithreading;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

// https://www.teamblind.com/post/India-Offer-Tracable-AI-vs-Quince-help-me-decide-nzQgYB1E


//* There is single Bathroom to be used in a Voting agency for both Democrats(D)
//        and Republicans(R) * This single Bathroom which can accomodate 3
//        people at most * each person takes f(N) secs to do his thing.
//        f(N) is a function of the person's name and returns varying number * CONDITION:
//        At any given time, the bathroom cannot have a mixed set of people i.e.
//        * CONDITION: Bathroom can have at most 3 people * these combinations aren't
//        allowed (2D, 1R) or (1D,1R) * These are allowed (), (3D), (2D), (1R) i.e.
//        pure Republicans or Pure Democrats *
//        While the bathroom is occupied people are to wait in a queue *
//        What is the most optimal system where you would manage people in this queue,
//        so that * the most eligible person instants gets to use the bathroom whenever its has room,
//        based on above conditions

//class Bathroom {
//    private static final int CAPACITY = 3; // Maximum people in the bathroom at once
//    private static final int MAX_CONSECUTIVE_USERS = 3; // Max consecutive users from the same group
//
//    private int currentCount = 0; // People currently in the bathroom
//    private String currentGroup = null; // Current group using the bathroom
//    private int consecutiveUsers = 0; // Track consecutive users from the same group
//
//    private final Lock lock = new ReentrantLock(); // Reentrant lock for synchronization
//    private final Condition democratsWaiting = lock.newCondition();
//    private final Condition republicansWaiting = lock.newCondition();
//
//    private int waitingDemocrats = 0;
//    private int waitingRepublicans = 0;
//
//    public void arrive(String person) {
//        lock.lock();
//        try {
//            boolean isDemocrat = person.startsWith("D");
//            if (isDemocrat) {
//                waitingDemocrats++;
//            } else {
//                waitingRepublicans++;
//            }
//
//            while (currentCount >= CAPACITY || (currentGroup != null && !currentGroup.equals(person.substring(0, 1)))
//                    || (consecutiveUsers >= MAX_CONSECUTIVE_USERS && currentGroup.equals(person.substring(0, 1)))) {
//                if (isDemocrat) {
//                    democratsWaiting.await();
//                } else {
//                    republicansWaiting.await();
//                }
//            }
//
//            // User can enter the bathroom
//            currentCount++;
//            currentGroup = person.substring(0, 1);
//            consecutiveUsers++;
//
//            if (isDemocrat) {
//                waitingDemocrats--;
//            } else {
//                waitingRepublicans--;
//            }
//
//            System.out.println(person + " entered the bathroom. Current count: " + currentCount);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void finish(String person) {
//        lock.lock();
//        try {
//            currentCount--;
//            System.out.println(person + " left the bathroom. Remaining: " + currentCount);
//
//            if (currentCount == 0) {
//                // If no one is left, reset the current group
//                currentGroup = null;
//                consecutiveUsers = 0;
//            }
//
//            if (waitingDemocrats > 0 && (currentGroup == null || currentGroup.equals("D"))) {
//                democratsWaiting.signalAll();
//            } else if (waitingRepublicans > 0 && (currentGroup == null || currentGroup.equals("R"))) {
//                republicansWaiting.signalAll();
//            }
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public static void main(String[] args) {
//        Bathroom bathroom = new Bathroom();
//        ExecutorService executor = Executors.newFixedThreadPool(3); // Limit concurrent threads to 3
//
//        // Simulating 10 users
//        for (int i = 0; i < 10; i++) {
//            final int id = i;
//            executor.submit(() -> {
//                String person = (id % 2 == 0) ? "D" + id : "R" + id;
//                bathroom.arrive(person);
//                try {
//                    Thread.sleep(500 + (int) (Math.random() * 1000)); // Simulate bathroom usage time
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                bathroom.finish(person);
//            });
//        }
//
//        executor.shutdown();
//        try {
//            executor.awaitTermination(10, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}



//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//import java.util.LinkedList;
//import java.util.Queue;
//
//
//class Bathroom {
//    private static final int CAPACITY = 3;
//    private Queue<String> waitingQueue = new LinkedList<>();
//    private String currentGroup = null; // Tracks the current group (D or R)
//    private int currentCount = 0; // Tracks the number of people currently in the bathroom
//
//    private final ReentrantLock lock = new ReentrantLock();
//    private final Condition condition = lock.newCondition();
//
//    public void arrive(String person) {
//        lock.lock();
//        try {
//            waitingQueue.add(person);
//            System.out.println(person + " arrived and is waiting.");
//            assignBathroom();
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void finish(String person) {
//        lock.lock();
//        try {
//            currentCount--;
//            System.out.println(person + " finished. Remaining: " + currentCount);
//
//            // Reset the group when the bathroom is empty
//            if (currentCount == 0) {
//                currentGroup = null;
//            }
//
//            assignBathroom();
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    private void assignBathroom() {
//        // Only allow entry if the bathroom is not full
//        while (currentCount < CAPACITY && !waitingQueue.isEmpty()) {
//            String nextPerson = waitingQueue.peek();
//
//            // Check if this person belongs to the same group as the current group
//            if (currentGroup == null || nextPerson.startsWith(currentGroup)) {
//                // Assign the group if not assigned yet
//                if (currentGroup == null) {
//                    currentGroup = nextPerson.startsWith("D") ? "D" : "R";
//                }
//
//                // Ensure the same group enters the bathroom
//                if (nextPerson.startsWith(currentGroup)) {
//                    waitingQueue.poll();
//                    currentCount++;
//                    System.out.println(nextPerson + " entered. Bathroom count: " + currentCount);
//                } else {
//                    break; // Wait if the group doesn't match
//                }
//            } else {
//                break; // If the groups don't match, stop trying to add people
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        Bathroom bathroom = new Bathroom();
//        ExecutorService executor = Executors.newFixedThreadPool(3); // Limit concurrent threads to 3
//
//        // Simulating 10 users
//        for (int i = 0; i < 10; i++) {
//            final int id = i;
//            executor.submit(() -> {
//                String person = (id % 2 == 0) ? "D" + id : "R" + id;
//                bathroom.arrive(person);
//                try {
//                    Thread.sleep(500 + (int) (Math.random() * 1000)); // Simulate bathroom usage time
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                bathroom.finish(person);
//            });
//        }
//
//        executor.shutdown();
//        try {
//            executor.awaitTermination(10, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}




import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class BathroomScheduler {
    private static final int MAX_CAPACITY = 3;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition democratsQueue = lock.newCondition();
    private final Condition republicansQueue = lock.newCondition();

    private int currentDemocrats = 0;
    private int currentRepublicans = 0;
    private Party currentParty = null;

    enum Party {
        DEMOCRAT,
        REPUBLICAN
    }

    public void enterBathroom(String personName, Party party) throws InterruptedException {
        lock.lock();
        try {
            while (!canEnter(party)) {
                if (party == Party.DEMOCRAT) {
                    democratsQueue.await();
                } else {
                    republicansQueue.await();
                }
            }

            // Enter bathroom
            if (party == Party.DEMOCRAT) {
                currentDemocrats++;
                currentParty = Party.DEMOCRAT;
            } else {
                currentRepublicans++;
                currentParty = Party.REPUBLICAN;
            }

            System.out.println(personName + " (" + party + ") entered. Current status: D=" +
                    currentDemocrats + ", R=" + currentRepublicans);

        } finally {
            lock.unlock();
        }

        // Simulate bathroom use
        Thread.sleep(calculateUsageTime(personName));

        exitBathroom(personName, party);
    }

    private void exitBathroom(String personName, Party party) {
        lock.lock();
        try {
            if (party == Party.DEMOCRAT) {
                currentDemocrats--;
                if (currentDemocrats == 0) {
                    currentParty = null;
                    // Signal waiting Republicans if Democrats are done
                    republicansQueue.signalAll();
                }
                // Signal other waiting Democrats if there's still room
                if (currentDemocrats < MAX_CAPACITY) {
                    democratsQueue.signal();
                }
            } else {
                currentRepublicans--;
                if (currentRepublicans == 0) {
                    currentParty = null;
                    // Signal waiting Democrats if Republicans are done
                    democratsQueue.signalAll();
                }
                // Signal other waiting Republicans if there's still room
                if (currentRepublicans < MAX_CAPACITY) {
                    republicansQueue.signal();
                }
            }

            System.out.println(personName + " (" + party + ") exited. Current status: D=" +
                    currentDemocrats + ", R=" + currentRepublicans);

        } finally {
            lock.unlock();
        }
    }

    private boolean canEnter(Party party) {
        if (currentParty == null) {
            return true;
        }

        if (party == Party.DEMOCRAT) {
            return currentParty == Party.DEMOCRAT &&
                    currentDemocrats < MAX_CAPACITY &&
                    currentRepublicans == 0;
        } else {
            return currentParty == Party.REPUBLICAN &&
                    currentRepublicans < MAX_CAPACITY &&
                    currentDemocrats == 0;
        }
    }

    private long calculateUsageTime(String name) {
        // Simple hash function to generate different times for different names
        return (Math.abs(name.hashCode()) % 3000) + 1000; // 1-4 seconds
    }
}

// Test class to simulate usage
class Bathroom {
    public static void main(String[] args) {
        BathroomScheduler scheduler = new BathroomScheduler();

        // Create some test users
        String[] democratNames = {"Biden", "Harris", "Pelosi", "Schumer", "Warren"};
        String[] republicanNames = {"Trump", "DeSantis", "Cruz", "Romney", "McCarthy"};

        // Create and start threads for each person
        for (String name : democratNames) {
            new Thread(() -> {
                try {
                    scheduler.enterBathroom(name, BathroomScheduler.Party.DEMOCRAT);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        for (String name : republicanNames) {
            new Thread(() -> {
                try {
                    scheduler.enterBathroom(name, BathroomScheduler.Party.REPUBLICAN);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
