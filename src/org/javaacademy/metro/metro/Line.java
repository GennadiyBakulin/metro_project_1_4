package org.javaacademy.metro.metro;

import java.util.LinkedHashSet;
import java.util.Set;

public class Line {
    private final LineColor color;
    private final Metro metro;
    private final Set<Station> stations = new LinkedHashSet<>();

    public Line(LineColor color, Metro metro) {
        this.color = color;
        this.metro = metro;
    }

    public LineColor getColor() {
        return color;
    }

    public Set<Station> getStations() {
        return stations;
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    @Override
    public String toString() {
        return "Line{" +
                "color=" + color +
                ", stations=" + stations +
                '}';
    }
}
