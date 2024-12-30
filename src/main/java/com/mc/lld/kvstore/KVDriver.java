package com.mc.lld.kvstore;

import java.util.*;

public class KVDriver {

    public static void run() {
        KeyValueStore kvStore = new KeyValueStore();

        // Demonstrate primitive operations
        System.out.println("=== Primitive Operations ===");
        demonstratePrimitiveOperations(kvStore);

        // Demonstrate collection operations
        System.out.println("\n=== Collection Operations ===");
        demonstrateCollectionOperations(kvStore);

        // Demonstrate type safety
//        System.out.println("\n=== Type Safety Demonstrations ===");
//        demonstrateTypeSafety(kvStore);
//
//        // Demonstrate error handling
//        System.out.println("\n=== Error Handling Demonstrations ===");
//        demonstrateErrorHandling(kvStore);
    }

    private static void demonstratePrimitiveOperations(KeyValueStore kvStore) {
        try {
            // Store different primitive types
            kvStore.put("name", "John Doe");
            kvStore.put("age", 30);
            kvStore.put("salary", 75000.50);
            kvStore.put("isEmployee", true);

            // Retrieve and display values
            String name = kvStore.get("name");
            Integer age = kvStore.get("age");
            Double salary = kvStore.get("salary");
            Boolean isEmployee = kvStore.get("isEmployee");

            System.out.println("Stored primitive values:");
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Salary: " + salary);
            System.out.println("Is Employee: " + isEmployee);

            // Delete a key
            kvStore.delete("salary");
            System.out.println("\nAfter deleting salary, value exists? " + (kvStore.get("salary") != null));

        } catch (Exception e) {
            System.out.println("Error in primitive operations: " + e.getMessage());
        }
    }


    private static void demonstrateCollectionOperations(KeyValueStore kvStore) {
        try {
            // List operations
            List<String> hobbies = Arrays.asList("reading", "gaming", "cooking");
            kvStore.putCollection("hobbies", hobbies, String.class);

            // Set operations
            Set<Integer> scores = new HashSet<>(Arrays.asList(95, 87, 91, 83));
            kvStore.putCollection("scores", scores, Integer.class);

            // Retrieve and display collections
            Collection<String> storedHobbies = kvStore.getCollection("hobbies");
            Collection<Integer> storedScores = kvStore.getCollection("scores");

            System.out.println("Stored collections:");
            System.out.println("Hobbies (List): " + storedHobbies);
            System.out.println("Scores (Set): " + storedScores);

            // Delete specific values from collection
            kvStore.deleteFromCollection("hobbies", Arrays.asList("gaming"));
            storedHobbies = kvStore.getCollection("hobbies");
            System.out.println("\nHobbies after removing 'gaming': " + storedHobbies);

        } catch (Exception e) {
            System.out.println("Error in collection operations: " + e.getMessage());
        }
    }
}
