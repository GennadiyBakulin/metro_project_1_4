package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.stationexception.StationWasNotFoundException;
import org.javaacademy.metro.metro.lineattribute.LineColor;

import java.util.LinkedHashSet;

public class Line {
    private final LineColor color;
    private final Metro metro;
    private final LinkedHashSet<Station> stations = new LinkedHashSet<>();

    public Line(LineColor color, Metro metro) {
        this.color = color;
        this.metro = metro;
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public Station getLastStation() throws StationWasNotFoundException {
        if (stations.isEmpty()) {
            throw new StationWasNotFoundException(
                    "Не получилось получить последнюю станцию на линии, так как линия не содержит станций!");
        }
        return stations.stream()
                .skip(stations.size() - 1)
                .findFirst()
                .get();
    }

    public LineColor getColor() {
        return color;
    }

    public LinkedHashSet<Station> getStations() {
        return stations;
    }

    public Metro getMetro() {
        return metro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Line line = (Line) o;
        return color == line.getColor() && metro.equals(line.getMetro());
    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + metro.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Line{" +
                "color=" + color.getName() +
                ", stations=" + stations +
                '}';
    }
}
