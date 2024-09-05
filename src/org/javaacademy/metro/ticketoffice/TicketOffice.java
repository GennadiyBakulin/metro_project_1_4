package org.javaacademy.metro.ticketoffice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.SortedMap;
import java.util.TreeMap;

public class TicketOffice {
    public static final BigDecimal SINGLE_PAYMENT = BigDecimal.valueOf(20);
    public static final BigDecimal COST_TICKET = BigDecimal.valueOf(5);
    public static final BigDecimal COST_TRAVEL_TICKET = BigDecimal.valueOf(3000);

    private final TreeMap<LocalDate, BigDecimal> recordIncome = new TreeMap<>();

    public SortedMap<LocalDate, BigDecimal> getRecordIncome() {
        return recordIncome;
    }

    public void addRecordOfTicketSale(LocalDate date, BigDecimal price) {
        recordIncome.merge(date, price, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "TicketOffice{" +
                "recordIncome=" + recordIncome +
                '}';
    }
}
