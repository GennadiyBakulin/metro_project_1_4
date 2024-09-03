package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.stationexception.StationNotAddedException;
import org.javaacademy.metro.metro.lineattribute.LineColor;

import java.time.Duration;
import java.util.LinkedList;

public class Line {
    private final LineColor color;
    private final Metro metro;
    private final LinkedList<Station> stations = new LinkedList<>();

    private Line(LineColor color, Metro metro) {
        this.color = color;
        this.metro = metro;
    }

    public static Line createLine(LineColor lineColor, Metro metro) {
        return new Line(lineColor, metro);
    }

    void addFirstStation(String nameStation, Line changeLines) {
        stations.addFirst(Station.createStation(nameStation, changeLines, this));
    }

    void addLastStation(String nameStation, Line changeLines, Duration time) throws StationNotAddedException {
        Station lastStation = stations.peekLast();
        Station newStation = Station.createStation(nameStation, changeLines, this);
        newStation.setPrevious(lastStation);
        lastStation.setNext(newStation);
        lastStation.setTimeTransferToNextStation(time);
        stations.addLast(newStation);
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public LineColor getColor() {
        return color;
    }

    public LinkedList<Station> getStations() {
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
