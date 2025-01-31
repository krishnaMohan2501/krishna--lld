package com.mc.lld.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver {
    public static void main(String[] args) throws Exception{
        Database db = new Database();

        // Create a table with columns and constraints
        List<Column> userColumns = Arrays.asList(
                new Column("id", ColumnType.INT, null, 1024, true),
                new Column("name", ColumnType.STRING, 20, null, true),
                new Column("email", ColumnType.STRING, 20, null, false)
        );
        db.createTable("users", userColumns);

        // Insert records
        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", 2048);
        user1.put("name", "John Doe");
        user1.put("email", "john@example.com");

        db.insertRecord("users", user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("id", 3072);
        user2.put("name", "Jane Smith");
        user2.put("email", "jane@example.com");
        db.insertRecord("users", user2);

        // Print all records
        System.out.println("All Users:");
        for (Map<String, Object> record : db.selectRecords("users", null, null)) {
            System.out.println(record);
        }

        // Filter and display records
        System.out.println("\nUsers with name 'John Doe':");
        for (Map<String, Object> record : db.selectRecords("users", "name", "John Doe")) {
            System.out.println(record);
        }
    }


}
