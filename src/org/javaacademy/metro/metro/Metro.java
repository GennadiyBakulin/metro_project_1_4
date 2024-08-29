package org.javaacademy.metro.metro;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Metro {
    private final String city;
    private final Set<Line> lines = new HashSet<>(2);

    public Metro(String city) {
        this.city = city;
    }

    public void createNewLine(LineColor lineColor) {
        if (isNotLineWithThisColor(lineColor)) {
            lines.add(new Line(lineColor, this));
        }
    }

    public void createFirstStation(LineColor lineColor, String nameStation, Station[] changeLines) {
        Line line;
        if (!isNotLineWithThisColor(lineColor) && isUniqueNameOfStation(nameStation)) {
            line = lines.stream().filter(x -> x.getColor().equals(lineColor)).findFirst().get();
            if (line.isEmpty()) {
                line.getStations().add(new Station(nameStation, changeLines, this, line));
            }
        }
    }

    public void createEndStation(LineColor lineColor, String nameStation,
                                 Duration timeTransferFromPreviousStation,
                                 Station[] changeLines) {

    }

    private boolean isNotLineWithThisColor(LineColor lineColor) {
        return lines.stream().noneMatch(line -> line.getColor().equals(lineColor));
    }

    private boolean isUniqueNameOfStation(String nameStation) {
        if (lines.isEmpty()) {
            return false;
        }
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .noneMatch(a -> a.getName().equals(nameStation));
    }

    @Override
    public String toString() {
        return "Metro{" +
                "city='" + city + '\'' +
                ", lines=" + lines.toString() +
                '}';
    }
}
