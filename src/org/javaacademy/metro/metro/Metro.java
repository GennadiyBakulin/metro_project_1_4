package org.javaacademy.metro.metro;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Metro {

    private final String city;
    private final Set<Line> lines = new HashSet<>(2);

    public Metro(String city) {
        this.city = city;
    }

    public void createLine(LineColor lineColor) {
        if (!hasLineWithThisColor(lineColor)) {
            lines.add(new Line(lineColor, this));
        }
    }

    public void createFirstStation(LineColor lineColor, String nameStation, List<Station> changeLines) {
        Line line;
        if (hasLineWithThisColor(lineColor) && nameStationIsUnique(nameStation)) {
            line = getLineWithThisColor(lineColor);
            if (line.isEmpty()) {
                line.addStation(nameStation, changeLines);
            }
        }
    }

    public void createEndStation(LineColor lineColor, String nameStation,
                                 Duration timeTransferFromPreviousStation,
                                 List<Station> changeLines) {
        Line line;
        if (hasLineWithThisColor(lineColor) && nameStationIsUnique(nameStation)
                && timeTransferFromPreviousStation.getSeconds() > 0) {
            line = getLineWithThisColor(lineColor);
            if (!line.isEmpty() && line.getEndStation().getNext() == null) {
                line.addStation(nameStation, changeLines, timeTransferFromPreviousStation);
            }
        }
    }

    private boolean hasLineWithThisColor(LineColor lineColor) {
        return lines.stream().anyMatch(line -> line.getColor().equals(lineColor));
    }

    private boolean nameStationIsUnique(String nameStation) {
        if (lines.isEmpty()) {
            return false;
        }
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .noneMatch(a -> a.getName().equals(nameStation));
    }

    private Line getLineWithThisColor(LineColor lineColor) {
        return lines.stream().filter(x -> x.getColor().equals(lineColor)).findFirst().get();
    }

    @Override
    public String toString() {
        return "Metro{" +
                "city='" + city + '\'' +
                ", lines=" + lines.toString() +
                '}';
    }
}
