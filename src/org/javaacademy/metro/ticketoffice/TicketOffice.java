package org.javaacademy.metro.ticketoffice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

public class TicketOffice {
    private static final BigDecimal SINGLE_PAYMENT = BigDecimal.valueOf(20);
    private static final BigDecimal COST_TICKET = BigDecimal.valueOf(5);
    private static final BigDecimal COST_TRAVEL_TICKET = BigDecimal.valueOf(3000);

    private final TreeMap<LocalDate, BigDecimal> recordIncome = new TreeMap<>();

    public TreeMap<LocalDate, BigDecimal> getRecordIncome() {
        return recordIncome;
    }

    public void addRecordOfTicketSale(LocalDate date, int count) {
        BigDecimal price = COST_TICKET.multiply(BigDecimal.valueOf(count)).add(SINGLE_PAYMENT);
        addRecordIncome(date, price);
    }

    public void addRecordOfTravelTicket(LocalDate date) {
        addRecordIncome(date, COST_TRAVEL_TICKET);
    }

    private void addRecordIncome(LocalDate date, BigDecimal price) {
        recordIncome.merge(date, price, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "TicketOffice{" +
                "recordIncome=" + recordIncome +
                '}';
    }
}
