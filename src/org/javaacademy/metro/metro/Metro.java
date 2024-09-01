package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.LineNotCreatedException;
import org.javaacademy.metro.exception.StationNotAddedException;

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

    public Line createLine(LineColor lineColor) throws LineNotCreatedException {
        Line line = getLineWithThisColor(lineColor);
        if (line == null) {
            line = new Line(lineColor, this);
            lines.add(line);
        } else {
            throw new LineNotCreatedException("Не удалось создать новую линию");
        }
        return line;
    }

    public void createFirstStation(LineColor lineColor, String nameStation, Line changeLines) throws StationNotAddedException {
        Line line = getLineWithThisColor(lineColor);
        if (line != null && line.isEmpty() && nameStationIsUnique(nameStation)) {
            line.addStation(nameStation, changeLines);
        } else {
            throw new StationNotAddedException("Не удалось добавить первую станцию в линию метро");
        }
    }

    public void createEndStation(LineColor lineColor, String nameStation,
                                 Duration timeTransferFromPreviousStation,
                                 Line changeLines) throws StationNotAddedException {
        Line line = getLineWithThisColor(lineColor);
        if (line != null && !line.isEmpty() && nameStationIsUnique(nameStation)
                && timeTransferFromPreviousStation.getSeconds() > 0
                && line.getLastStation().getNext() == null) {
            line.addStation(nameStation, changeLines, timeTransferFromPreviousStation);
        } else {
            throw new StationNotAddedException("Не удалось добавить конечную станцию в линию метро");
        }
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
        return lines.stream().filter(x -> x.getColor().equals(lineColor)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "Metro{" +
                "city='" + city + '\'' +
                ", lines=" + lines +
                '}';
    }
}
