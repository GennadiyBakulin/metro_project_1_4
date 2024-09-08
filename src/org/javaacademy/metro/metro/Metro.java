package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.lineexception.LineNotCreatedException;
import org.javaacademy.metro.exception.stationexception.StationNotAddedException;
import org.javaacademy.metro.exception.stationexception.StationWasNotFoundException;
import org.javaacademy.metro.metro.lineattribute.LineColor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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

    public void createLine(LineColor lineColor) throws LineNotCreatedException {
        if (getLineWithThisColor(lineColor) != null) {
            throw new LineNotCreatedException("Не удалось создать линию, так как такая линия уже существует!");
        }
        Line newline = new Line(lineColor, this);
        lines.add(newline);
    }

    public void createFirstStation(LineColor lineColor, String name, String... changeLines)
            throws StationNotAddedException {
        Line line = getLineWithThisColor(lineColor);
        if (line == null || !line.isEmpty() || nameStationNotUnique(name)) {
            throw new StationNotAddedException("Не удалось добавить первую станцию в линию метро");
        }
        line.addStation(new Station(name, line, changeLines));
    }

    public void createLastStation(LineColor lineColor, String name,
                                  Duration timeTransferFromPreviousStation,
                                  String... changeLines) throws StationNotAddedException, StationWasNotFoundException {
        Line line = getLineWithThisColor(lineColor);
        Station lastStationInLine;
        if (line == null || line.isEmpty() || nameStationNotUnique(name)
                || timeTransferFromPreviousStation.getSeconds() <= 0
                || Objects.requireNonNull(lastStationInLine = line.getLastStation()).getNext() != null) {
            throw new StationNotAddedException("Не удалось добавить конечную станцию в линию метро");
        }
        Station newStation = new Station(name, line, changeLines);
        newStation.setPrevious(lastStationInLine);
        lastStationInLine.setNext(newStation);
        lastStationInLine.setTimeTransferToNextStation(timeTransferFromPreviousStation);
        line.addStation(newStation);
    }

    private Line getLineWithThisColor(LineColor lineColor) {
        return lines.stream().filter(x -> x.getColor().equals(lineColor)).findFirst().orElse(null);
    }

    private boolean nameStationNotUnique(String nameStation) {
        if (lines.isEmpty()) {
            return true;
        }
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .anyMatch(station -> station.getName().equals(nameStation));
    }

    public Station getStationByName(String name) throws StationWasNotFoundException {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(station -> station.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new StationWasNotFoundException(
                        String.format("Не найдена станция %s на линиях метро %s\n", name, city)));
    }

    private Station findTransferStations(Line startLine, Line endLine) throws StationWasNotFoundException {
        return startLine.getStations()
                .stream()
                .filter(station -> station.getChangeLines() != null)
                .filter(station -> station.getChangeLines().stream()
                        .map(nameTransferStation -> {
                            try {
                                return this.getStationByName(nameTransferStation);
                            } catch (StationWasNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .anyMatch(transferStation ->
                                transferStation != null && transferStation.getLine().equals(endLine)))
                .findFirst()
                .orElseThrow(() -> new StationWasNotFoundException("Станция на пересадку не найдена!"));
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
        int count;
        if ((count = numberOfRunsBetweenTwoStationsOneLineDirectSearch(start, end)) != -1) {
            return count;
        }
        if ((count = numberOfRunsBetweenTwoStationsOneLineReverseSearch(start, end)) != -1) {
            return count;
        }
        throw new NoWayOutOfStationException(
                String.format("Нет пути из станции %s в %s", start.getName(), end.getName()));
    }

    public int numberOfRunsBetweenTwoStations(String start, String end)
            throws NoWayOutOfStationException, StationWasNotFoundException {

        if (start.equals(end)) {
            return 0;
        }
        Station startStation = getStationByName(start);
        Station endStation = getStationByName(end);

        if (startStation == null || endStation == null) {
            throw new StationWasNotFoundException(
                    String.format("Одна из станций или обе станции не найдены на линиях метро %s!\n", city));
        }

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
        return date.isEqual(travelTickets.get(number)) || date.isBefore(travelTickets.get(number));
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
