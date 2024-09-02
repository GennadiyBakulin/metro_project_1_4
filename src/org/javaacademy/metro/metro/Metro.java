package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.lineexception.LineNotCreatedException;
import org.javaacademy.metro.exception.stationexception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.stationexception.StationNotAddedException;
import org.javaacademy.metro.exception.stationexception.StationWasNotFoundException;
import org.javaacademy.metro.metro.line.Line;
import org.javaacademy.metro.metro.line.LineColor;
import org.javaacademy.metro.metro.station.Station;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Metro {
    private static final String PREFIX_NUMBER_TRAVEL_TICKET = "a";
    private final SortedMap<String, LocalDate> travelTickets = new TreeMap<>();
    private final Set<Line> lines = new HashSet<>(2);

    private final String city;

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

    public void createFirstStation(LineColor lineColor, String name, Line changeLines)
            throws StationNotAddedException {
        Line line = getLineWithThisColor(lineColor);
        if (line != null && line.isEmpty() && nameStationIsUnique(name)) {
            line.addStation(name, changeLines);
        } else {
            throw new StationNotAddedException("Не удалось добавить первую станцию в линию метро");
        }
    }

    public void createEndStation(LineColor lineColor, String name,
                                 Duration timeTransferFromPreviousStation,
                                 Line changeLines) throws StationNotAddedException {
        Line line = getLineWithThisColor(lineColor);
        if (line != null && !line.isEmpty() && nameStationIsUnique(name)
                && timeTransferFromPreviousStation.getSeconds() > 0
                && Objects.requireNonNull(line.getStations().peekLast()).getNext() == null) {
            line.addStation(name, changeLines, timeTransferFromPreviousStation);
        } else {
            throw new StationNotAddedException("Не удалось добавить конечную станцию в линию метро");
        }
    }

    private boolean nameStationIsUnique(String nameStation) {
        if (lines.isEmpty()) {
            return false;
        }
        return getStationByName(nameStation) == null;
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

    public int numberOfRunsBetweenTwoStationsOneLineDirectSearch(Station start, Station end) {
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

    public int numberOfRunsBetweenTwoStationsOneLineReverseSearch(Station start, Station end) {
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

    public int numberOfRunsBetweenTwoStationsOneLine(Station start, Station end) throws NoWayOutOfStationException {
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
            throw new StationWasNotFoundException("Одна из станций или обе станции не найдены на линиях метро Пермь!");
        }

        if (startStation.getLine().equals(endStation.getLine())) {
            return numberOfRunsBetweenTwoStationsOneLine(startStation, endStation);
        }

        Station startTransferStation = findTransferStations(startStation.getLine(), endStation.getLine());
        int count = numberOfRunsBetweenTwoStationsOneLine(startStation, startTransferStation);
        Station endTransferStation = findTransferStations(endStation.getLine(), startStation.getLine());
        count += numberOfRunsBetweenTwoStationsOneLine(endStation, endTransferStation);

        return count;
    }

    public SortedMap<String, LocalDate> getTravelTickets() {
        return travelTickets;
    }

    public Station getStationByName(String name) {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public String generateNumberTravelTicket() {
        return String.format("%s%04d",
                PREFIX_NUMBER_TRAVEL_TICKET, travelTickets.size());
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
