package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.stationexception.StationWasNotFoundException;
import org.javaacademy.metro.ticketoffice.TicketOffice;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Station {
    private final String name;
    private final Metro metro;
    private final Line line;
    private Station previous;
    private Station next;
    private Set<String> changeLines;
    private Duration timeTransferToNextStation;
    private final TicketOffice ticketOffice = new TicketOffice();

    public Station(String name, Line line, String... changeLines) {
        this.name = name;
        this.line = line;
        this.metro = line.getMetro();
        if (Objects.nonNull(changeLines)) {
            this.changeLines = new HashSet<>();
            this.changeLines.addAll(Set.of(changeLines));
        }
    }

    public void salesTicket(LocalDate date, String start, String end)
            throws StationWasNotFoundException, NoWayOutOfStationException {
        int count = metro.numberOfRunsBetweenTwoStations(start, end);
        ticketOffice.addRecordOfTicketSale(date, count);
    }

    public void salesTravelTicket(LocalDate date) {
        metro.addTravelTicket(metro.generateNumberTravelTicket(), date.plusMonths(1));
        ticketOffice.addRecordOfTravelTicket(date);
    }

    public void extensionsTravelTicket(String number, LocalDate date) {
        metro.addTravelTicket(number, date.plusMonths(1));
        ticketOffice.addRecordOfTravelTicket(date);
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

    public void setTimeTransferToNextStation(Duration timeTransferToNextStation) {
        this.timeTransferToNextStation = timeTransferToNextStation;
    }

    public Duration getTimeTransferToNextStation() {
        return timeTransferToNextStation;
    }

    public Line getLine() {
        return line;
    }

    public Set<String> getChangeLines() {
        return changeLines;
    }

    public TicketOffice getTicketOffice() {
        return ticketOffice;
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
                ", changeLines=" + (changeLines == null ? "null" : changeLines.stream()
                .map(stationName -> {
                    try {
                        return metro.getStationByName(stationName).getLine().getColor().getName();
                    } catch (StationWasNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining())) +
                '}';
    }
}
