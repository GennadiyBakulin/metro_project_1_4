package org.javaacademy.metro.metro;

import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.stationexception.StationNotFoundException;
import org.javaacademy.metro.ticketoffice.TicketOffice;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Station {
    private final String name;
    private final Metro metro;
    private final Line line;
    private Station previous;
    private Station next;
    private final Set<Station> changeLines = new HashSet<>();
    private Duration timeTransferToNextStation;
    private final TicketOffice ticketOffice = new TicketOffice();

    public Station(String name, Line line) {
        this.name = name;
        this.line = line;
        this.metro = line.getMetro();
    }

    public void salesTicket(LocalDate date, String startStation, String endStation)
            throws StationNotFoundException, NoWayOutOfStationException {
        int count = metro.numberOfRunsBetweenTwoStations(startStation, endStation);
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

    public void addChangeLine(Station station) {
        if (changeLines.add(station)) {
            station.addChangeLine(this);
        }
    }

    public Set<Station> getChangeLines() {
        return changeLines;
    }

    public TicketOffice getTicketOffice() {
        return ticketOffice;
    }

    private String getColorChangeLines() {
        return changeLines.isEmpty() ? "null" : changeLines.stream()
                .map(station -> station.getLine().getColor().getName())
                .collect(Collectors.joining(", "));
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
                ", changeLines='" + getColorChangeLines() +
                "'}";
    }
}
