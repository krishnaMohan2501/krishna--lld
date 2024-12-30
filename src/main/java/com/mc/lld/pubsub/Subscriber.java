package com.mc.lld.pubsub;

public interface Subscriber {
    void onMessage(Message message);
}
