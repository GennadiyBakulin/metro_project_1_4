package org.javaacademy.metro;

import java.time.LocalDate;
import java.util.TreeMap;

public class TicketOffice {
    private final TreeMap<LocalDate, Integer> recordIncome = new TreeMap<>();

    public TreeMap<LocalDate, Integer> getRecordIncome() {
        return recordIncome;
    }

    public void addRecordOfTicketSale(LocalDate date, int price) {
        recordIncome.merge(date, price, Integer::sum);
    }

    @Override
    public String toString() {
        return "TicketOffice{" +
                "recordIncome=" + recordIncome +
                '}';
    }
}
