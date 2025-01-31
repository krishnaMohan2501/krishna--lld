package com.mc.lld.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Column {
    private String name;
    private ColumnType dataType;
    private Integer maxLength;
    private Integer minValue;
    private boolean required;


    public void validateValue(Object value) throws ColumnConstraintException {
        if (value == null) {
            if (required) {
                throw new ColumnConstraintException("Column " + name + " is required");
            }
            return;
        }

        switch (dataType) {
            case STRING:
                if (!(value instanceof String)) {
                    throw new ColumnConstraintException("Column " + name + " must be a string");
                }
                String strValue = (String) value;
                if (maxLength != null && strValue.length() > maxLength) {
                    throw new ColumnConstraintException("String length for " + name + " cannot exceed " + maxLength);
                }
                break;
            case INT:
                if (!(value instanceof Integer)) {
                    throw new ColumnConstraintException("Column " + name + " must be an integer");
                }
                Integer intValue = (Integer) value;
                if (minValue != null && intValue < minValue) {
                    throw new ColumnConstraintException("Integer value for " + name + " must be at least " + minValue);
                }
                break;
        }
    }
}
