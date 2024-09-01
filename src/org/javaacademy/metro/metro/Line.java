package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.StationNotAddedException;

import java.time.Duration;
import java.util.LinkedList;

public class Line {
    private final LineColor color;
    private final Metro metro;
    private final LinkedList<Station> stations = new LinkedList<>();

    public Line(LineColor color, Metro metro) {
        this.color = color;
        this.metro = metro;
    }

    public void addStation(String nameStation, Line changeLines) throws StationNotAddedException {
        if (!stations.offerFirst(new Station(nameStation, changeLines, this))) {
            throw new StationNotAddedException("Не удалось добавить первую станцию в линию метро");
        }
    }

    public void addStation(String nameStation, Line changeLines, Duration time) throws StationNotAddedException {
        Station newStation = new Station(nameStation, changeLines, this);
        Station lastStation = getLastStation();

        if (!stations.offerLast(newStation)) {
            throw new StationNotAddedException("Не удалось добавить конечную станцию в линию метро");
        }
        lastStation.setNext(newStation);
        lastStation.setTimeTransferToNextStation(time);
        newStation.setPrevious(lastStation);
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

    public Station getFirstStations() {
        return stations.peekFirst();
    }

    public Station getLastStation() {
        return stations.peekLast();
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
