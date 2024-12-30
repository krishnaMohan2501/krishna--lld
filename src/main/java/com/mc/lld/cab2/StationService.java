package com.mc.lld.cab2;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StationService {

    @Getter
    private Map<String, Station> stationMap;

    private StationService () {
        this.stationMap = new ConcurrentHashMap<>();
    }

    private static volatile StationService INSTANCE;

    public static StationService getInstance() {
        if (INSTANCE == null) {
            synchronized (StationService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StationService();
                }
            }
        }
        return INSTANCE;
    }

    public Station getStation(String stationId) {
        return stationMap.get(stationId);
    }

    public void addStation( Station station) {
        stationMap.put(station.getStationId(), station);
    }
}
