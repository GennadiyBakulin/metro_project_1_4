package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.LineNotCreatedException;
import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.StationNotAddedException;
import org.javaacademy.metro.exception.StationWasNotFoundException;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
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

    public Station findTransferStations(Line start, Line end) throws StationWasNotFoundException {
        try {
            return getLineWithThisColor(start.getColor()).getStations()
                    .stream()
                    .filter(station -> station.getChangeLines() != null && station.getChangeLines().equals(end))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new StationWasNotFoundException("Станция на пересадку не найдена!");
        }
    }

    public int numberOfRunsBetweenTwoStations(Station start, Station end) throws NoWayOutOfStationException {


        return 0;
    }

    public int numberOfRunsBetweenTwoStationsOneLine(Line line, Station start, Station end) throws NoWayOutOfStationException {
        int count;
        if ((count = numberOfRunsBetweenTwoStationsOneLineDirectSearch(line, start, end)) != -1) {
            return count;
        }
        if ((count = numberOfRunsBetweenTwoStationsOneLineReverseSearch(line, start, end)) != -1) {
            return count;
        }
        throw new NoWayOutOfStationException(
                String.format("Нет пути из станции %s в %s", start.getName(), end.getName()));
    }

    public int numberOfRunsBetweenTwoStationsOneLineDirectSearch(Line line, Station start, Station end) {
//        AtomicInteger count = new AtomicInteger();
//        line.getStations().stream()
//                .takeWhile(station -> !station.equals(end))
//                .dropWhile(station -> !station.equals(start))
//                .forEach(x -> count.getAndIncrement());
//
//        return count.get() != 0 ? count.get() : -1;
        int count = 0;
        while (!start.equals(end)) {
            if (start.getNext() == null) {
                return -1;
            }
            start = start.getNext();
            count++;
        }
        return count;
    }

    public int numberOfRunsBetweenTwoStationsOneLineReverseSearch(Line line, Station start, Station end) {
//        AtomicInteger count = new AtomicInteger();
//        line.getStations().stream()
//                .takeWhile(station -> !station.equals(start))
//                .dropWhile(station -> !station.equals(end))
//                .forEach(x -> count.getAndIncrement());
//
//        return count.get() != 0 ? count.get() : -1;
        int count = 0;
        while (!start.equals(end)) {
            if (start.getPrevious() == null) {
                return -1;
            }
            start = start.getPrevious();
            count++;
        }
        return count;
    }


    @Override
    public String toString() {
        return "Metro{" +
                "city='" + city + '\'' +
                ", lines=" + lines +
                '}';
    }
}
