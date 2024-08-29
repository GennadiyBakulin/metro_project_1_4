package org.javaacademy.metro.metro;

import java.time.Duration;
import java.util.Arrays;

public class Station {
    private String name;
    private Metro metro;
    private Line line;
    private Station previous;
    private Station next;
    private Station[] changeLines;
    private Duration timeTransferToNextStation;

    public Station(String name, Metro metro, Line line,
                   Station previous, Station next,
                   Station[] changeLines,
                   Duration timeTransferToNextStation) {
        this.name = name;
        this.metro = metro;
        this.line = line;
        this.previous = previous;
        this.next = next;
        this.changeLines = changeLines;
        this.timeTransferToNextStation = timeTransferToNextStation;
    }

    public Station(String name, Station[] changeLines, Metro metro, Line line) {
        this.name = name;
        this.changeLines = changeLines;
        this.metro = metro;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", changeLines=" + Arrays.toString(changeLines) +
                '}';
    }
}
