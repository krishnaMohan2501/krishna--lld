package com.mc.lld.sprintplanner.enums;

import lombok.Getter;

@Getter
public enum TaskType {

    BUG("bug"),
    FEATURE("feature"),
    STORY("story");

    private final String taskType;

    TaskType(String taskType) {
        this.taskType = taskType;
    }
}
