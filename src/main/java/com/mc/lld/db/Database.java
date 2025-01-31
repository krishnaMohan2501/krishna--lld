package com.mc.lld.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private Map<String, Table> tableMap;

    public Database() {
        tableMap = new HashMap<>();
    }

    public void createTable(String name, List<Column> columns) throws Exception {
        if (tableMap.containsKey(name)) {
            throw new Exception("Table " + name + " already exists");
        }
        tableMap.put(name, new Table(name, columns));
    }

    public void dropTable(String name) throws Exception {
        if (!tableMap.containsKey(name)) {
            throw new Exception("Table " + name + " does not exist");
        }
        tableMap.remove(name);
    }

    public void insertRecord(String tableName, Map<String, Object> record) throws Exception {
        Table table = tableMap.get(tableName);
        if (table == null) {
            throw new Exception("Table " + tableName + " does not exist");
        }
        table.insert(record);
    }

    public List<Map<String, Object>> selectRecords(String tableName, String filterColumn, Object filterValue) throws Exception {
        Table table = tableMap.get(tableName);
        if (table == null) {
            throw new Exception("Table " + tableName + " does not exist");
        }
        return table.select(filterColumn, filterValue);
    }
}
