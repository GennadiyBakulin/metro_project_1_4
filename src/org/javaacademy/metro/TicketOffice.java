package org.javaacademy.metro;

import java.time.LocalDate;
import java.util.SortedMap;
import java.util.TreeMap;

public class TicketOffice {
    private final SortedMap<LocalDate, Long> recordIncome = new TreeMap<>();

    public SortedMap<LocalDate, Long> getRecordIncome() {
        return recordIncome;
    }

    public void addRecordOfTicketSale(LocalDate date, long price) {
        recordIncome.merge(date, price, Long::sum);
    }

    @Override
    public String toString() {
        return "TicketOffice{" +
                "recordIncome=" + recordIncome +
                '}';
    }
}
