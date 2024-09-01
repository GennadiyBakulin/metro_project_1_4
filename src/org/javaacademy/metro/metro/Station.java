package org.javaacademy.metro.metro;

import org.javaacademy.metro.TicketOffice;
import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.StationWasNotFoundException;

import java.time.Duration;
import java.time.LocalDate;

public class Station {
    private static final int SINGLE_PAYMENT = 20;
    private static final int TICKET_PRICE = 5;
    private final String name;
    private final Metro metro;
    private final Line line;
    private Station previous;
    private Station next;
    private final Line changeLines;
    private Duration timeTransferToNextStation;
    private final TicketOffice ticketOffice = new TicketOffice();

    public Station(String name, Line changeLines, Line line) {
        this.name = name;
        this.changeLines = changeLines;
        this.line = line;
        this.metro = line.getMetro();
    }

    public void ticketSales(LocalDate date, String start, String end)
            throws StationWasNotFoundException, NoWayOutOfStationException {
        int count = metro.numberOfRunsBetweenTwoStations(start, end);
        int ticketPrice = count * TICKET_PRICE + SINGLE_PAYMENT;
        ticketOffice.addRecordOfTicketSale(date, ticketPrice);
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

    public Duration getTimeTransferToNextStation() {
        return timeTransferToNextStation;
    }

    public void setTimeTransferToNextStation(Duration timeTransferToNextStation) {
        this.timeTransferToNextStation = timeTransferToNextStation;
    }

    public Line getLine() {
        return line;
    }

    public Line getChangeLines() {
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
                ", changeLines=" + (changeLines == null ? "null" : changeLines.getColor().getName()) +
                '}';
    }
}
