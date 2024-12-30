package com.mc.lld.sprintplanner.enums;

import lombok.Getter;

@Getter
public enum Status {

    IN_PROGESS("inprogeess"),
    DONE("done"),
    TODO("todo");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
