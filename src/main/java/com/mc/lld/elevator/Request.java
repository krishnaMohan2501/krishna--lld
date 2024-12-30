package com.mc.lld.elevator;

import lombok.ToString;

@ToString
public class Request {
    private final int fromFloor;
    private final int toFloor;
    private final Direction direction;

    public Request(int fromFloor, int toFloor) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.direction = toFloor > fromFloor ? Direction.UP : Direction.DOWN;
    }

    public int getFromFloor() { return fromFloor; }
    public int getToFloor() { return toFloor; }
    public Direction getDirection() { return direction; }
}
