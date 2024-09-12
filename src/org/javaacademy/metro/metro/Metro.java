package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.lineexception.LineCreatedException;
import org.javaacademy.metro.exception.lineexception.LineNotFoundException;
import org.javaacademy.metro.exception.stationexception.StationCreateException;
import org.javaacademy.metro.exception.stationexception.StationNotFoundException;
import org.javaacademy.metro.metro.lineattribute.LineColor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Metro {
    private static final String PREFIX_NUMBER_TRAVEL_TICKET = "a";
    private final SortedMap<String, LocalDate> travelTickets = new TreeMap<>();
    private final Set<Line> lines = new HashSet<>();

    private final String city;

    public Metro(String city) {
        this.city = city;
    }

    public void createLine(LineColor lineColor) throws LineCreatedException {
        try {
            if (hasLineWithThisColor(lineColor)) {
                throw new LineCreatedException(String.format("Не удалось создать %s линию метро, " +
                        "так как данная линия уже существует!\n", lineColor.getName()));
            }
        } catch (LineNotFoundException e) {
            lines.add(new Line(lineColor, this));
        }
    }

    public void createFirstStation(LineColor lineColor, String name, String... changeLines)
            throws LineNotFoundException, StationCreateException {
        Line line = getLineWithThisColor(lineColor);

        if (checkLineNotContainStations(line) && nameStationUnique(name)) {
            line.addStation(new Station(name, line, changeLines));
        }
    }

    public void createLastStation(LineColor lineColor, String name,
                                  Duration timeTransferFromPreviousStation,
                                  String... changeLines) throws LineNotFoundException, StationNotFoundException,
            StationCreateException {

        Line line = getLineWithThisColor(lineColor);
        if (checkLineContainStations(line) && nameStationUnique(name)
                && checkTimeTransfer(timeTransferFromPreviousStation)) {
            Station lastStationInLine = line.getLastStation();
            if (lastStationNotHaveNextStation(lastStationInLine)) {
                Station newStation = new Station(name, line, changeLines);
                newStation.setPrevious(lastStationInLine);
                lastStationInLine.setNext(newStation);
                lastStationInLine.setTimeTransferToNextStation(timeTransferFromPreviousStation);
                line.addStation(newStation);
            }
        }
    }

    private boolean checkLineContainStations(Line line) throws StationCreateException {
        if (!line.isEmpty()) {
            return true;
        }

        throw new StationCreateException(String.format("Не удалось добавить конечную станцию, " +
                "так как линия '%s' не содержит станции!\n", line.getColor().getName()));
    }

    private boolean checkLineNotContainStations(Line line) throws StationCreateException {
        if (line.isEmpty()) {
            return true;
        }

        throw new StationCreateException(String.format("Не удалось добавить первую станцию, " +
                "так как линия '%s' уже содержит станции!\n", line.getColor().getName()));
    }

    private boolean hasLineWithThisColor(LineColor lineColor) throws LineNotFoundException {
        if (lines.stream()
                .anyMatch(line -> line.getColor().equals(lineColor))) {
            return true;
        }
        throw new LineNotFoundException(String.format("%s линия не найдена!\n", lineColor.getName()));
    }

    private boolean nameStationUnique(String nameStation) throws StationCreateException {
        if (lines.isEmpty() || lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .noneMatch(station -> station.getName().equals(nameStation))) {
            return true;
        }

        throw new StationCreateException(String.format("Не удалось добавить станцию, " +
                "так как станция с именем '%s' уже существует!\n", nameStation));
    }

    private boolean checkTimeTransfer(Duration time) throws StationCreateException {
        if (time.getSeconds() > 0) {
            return true;
        }

        throw new StationCreateException("Не удалось добавить конечную станцию, " +
                "так как указанное время перегона не может быть меньше либо равно нулю!");
    }

    private boolean lastStationNotHaveNextStation(Station station) throws StationCreateException {
        if (station.getNext() == null) {
            return true;
        }
        throw new StationCreateException("Не удалось добавить конечную станцию, " +
                "так как последняя станция в линии уже имеет ссылку на следующую станцию!");
    }

    private Line getLineWithThisColor(LineColor lineColor) throws LineNotFoundException {
        return lines.stream()
                .filter(line -> line.getColor().equals(lineColor))
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException(
                        String.format("%s линия метро не найдена!\n", lineColor.getName())));
    }

    public Station getStationByName(String name) throws StationNotFoundException {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(station -> station.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(
                        String.format("Не найдена станция '%s'!\n", name)));
    }

    private Station findTransferStations(Line startLine, Line endLine) throws StationNotFoundException {
        return startLine.getStations()
                .stream()
                .filter(station -> station.getChangeLines() != null)
                .filter(station -> station.getChangeLines().stream()
                        .map(nameTransferStation -> {
                            try {
                                return this.getStationByName(nameTransferStation);
                            } catch (StationNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .anyMatch(transferStation ->
                                transferStation != null && transferStation.getLine().equals(endLine)))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException("Станция на пересадку не найдена!"));
    }

    private int numberOfRunsBetweenTwoStationsOneLineDirectSearch(Station start, Station end) {
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

    private int numberOfRunsBetweenTwoStationsOneLineReverseSearch(Station start, Station end) {
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

    private int numberOfRunsBetweenTwoStationsOneLine(Station start, Station end) throws NoWayOutOfStationException {
        int count = numberOfRunsBetweenTwoStationsOneLineDirectSearch(start, end);
        if (count != -1) {
            return count;
        }

        count = numberOfRunsBetweenTwoStationsOneLineReverseSearch(start, end);
        if (count != -1) {
            return count;
        }

        throw new NoWayOutOfStationException(
                String.format("Нет пути из станции %s в %s", start.getName(), end.getName()));
    }

    public int numberOfRunsBetweenTwoStations(String start, String end)
            throws NoWayOutOfStationException, StationNotFoundException {
        if (start.equals(end)) {
            return 0;
        }
        Station startStation = getStationByName(start);
        Station endStation = getStationByName(end);

        Line lineStartStation = startStation.getLine();
        Line lineEndStation = endStation.getLine();

        if (lineStartStation.equals(lineEndStation)) {
            return numberOfRunsBetweenTwoStationsOneLine(startStation, endStation);
        }

        Station startTransferStation = findTransferStations(lineStartStation, lineEndStation);
        int count = numberOfRunsBetweenTwoStationsOneLine(startStation, startTransferStation);
        Station endTransferStation = findTransferStations(lineEndStation, lineStartStation);
        count += numberOfRunsBetweenTwoStationsOneLine(endStation, endTransferStation);

        return count;
    }

    public SortedMap<String, LocalDate> getTravelTickets() {
        return travelTickets;
    }

    public String generateNumberTravelTicket() {
        return String.format("%s%04d", PREFIX_NUMBER_TRAVEL_TICKET, travelTickets.size());
    }

    public void addTravelTicket(String number, LocalDate date) {
        travelTickets.put(number, date);
    }

    public boolean validityCheckTravelTicket(String number, LocalDate date) {
        return date.isBefore(travelTickets.get(number))
                && date.isAfter(travelTickets.get(number).minusMonths(1))
                || date.isEqual(travelTickets.get(number))
                || date.isEqual(travelTickets.get(number).minusMonths(1));
    }

    public Map<LocalDate, BigDecimal> getIncomeAllTicketsOffice() {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .map(station -> station.getTicketOffice().getRecordIncome().entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BigDecimal::add));
    }

    public void printIncomeAllTicketsOffice() {
        TreeMap<LocalDate, BigDecimal> sorted = new TreeMap<>(getIncomeAllTicketsOffice());
        sorted.forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Override
    public String toString() {
        return "Metro{" +
                "city='" + city + '\'' +
                ", lines=" + lines +
                '}';
    }
}
