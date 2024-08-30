package org.javaacademy.metro.metro;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Station {
    private final String name;
    private final Metro metro;
    private final Line line;
    private Station previous;
    private Station next;
    private List<Station> changeLines = new ArrayList<>(1);
    private Duration timeTransferToNextStation;

    public Station(String name, List<Station> changeLines, Line line) {
        this.name = name;
        this.changeLines = changeLines;
        this.line = line;
        this.metro = line.getMetro();
    }

    public String getName() {
        return name;
    }

    public Metro getMetro() {
        return metro;
    }

    public Station getPrevious() {
        return previous;
    }

    public void setPrevious(Station previous) {
        this.previous = previous;
    }

    public Station getNext() {
        return next;
    }

    public void setNext(Station next) {
        this.next = next;
    }

    public Duration getTimeTransferToNextStation() {
        return timeTransferToNextStation;
    }

    public void setTimeTransferToNextStation(Duration timeTransferToNextStation) {
        this.timeTransferToNextStation = timeTransferToNextStation;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Station station = (Station) o;
        return name.equals(station.getName()) && metro.equals(station.getMetro());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + metro.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", changeLines=" + changeLines +
                '}';
    }
}
