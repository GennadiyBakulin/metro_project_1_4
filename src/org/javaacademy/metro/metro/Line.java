package org.javaacademy.metro.metro;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class Line {
    private final LineColor color;
    private final Metro metro;
    private final List<Station> stations = new LinkedList<>();
    private int size = 0;

    public Line(LineColor color, Metro metro) {
        this.color = color;
        this.metro = metro;
    }

    public void addStation(String nameStation, List<Station> changeLines) {
        stations.add(new Station(nameStation, changeLines, this));
        size++;
    }

    public void addStation(String nameStation, List<Station> changeLines, Duration time) {
        Station newStation = new Station(nameStation, changeLines, this);
        Station endStation = getEndStation();
        endStation.setNext(newStation);
        endStation.setTimeTransferToNextStation(time);
        newStation.setPrevious(endStation);
        stations.add(newStation);
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public LineColor getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station getEndStation() {
        return stations.get(size - 1);
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
