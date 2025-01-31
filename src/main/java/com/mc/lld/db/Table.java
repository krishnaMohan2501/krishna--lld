package com.mc.lld.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table {

   private String name;
   private List<Column> columns;
   private List<Map<String, Object>> records; // 10 rows and each row {c1:v1, c2:v2, c3:v3}

   public Table(String name, List<Column> columns) {
      this.name = name;
      this.columns = columns;
   }

   public void insert(Map<String, Object> record) throws ColumnConstraintException {
      // Validate record against column constraints
      for (Column column : columns) {
         Object value = record.get(column.getName());
         column.validateValue(value);
      }

      // Ensure all required columns are present
      for (Column column : columns) {
         if (column.isRequired() && !record.containsKey(column.getName())) {
            throw new ColumnConstraintException("Required column " + column.getName() + " is missing");
         }
      }

      records.add(record);
   }

   public List<Map<String, Object>> select(String filterColumn, Object filterValue) {
      // select *
      if (filterColumn == null || filterValue == null) {
         return records;
      }

      List<Map<String, Object>> filteredRecords = new ArrayList<>();
      for (Map<String, Object> record : records) {
         if (record.get(filterColumn) != null &&
                 record.get(filterColumn).equals(filterValue)) {
            filteredRecords.add(record);
         }
      }
      return filteredRecords;
   }

   public String getName() {
      return name;
   }
}
