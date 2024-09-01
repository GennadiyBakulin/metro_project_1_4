import org.javaacademy.metro.exception.LineNotCreatedException;
import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.StationNotAddedException;
import org.javaacademy.metro.exception.StationWasNotFoundException;
import org.javaacademy.metro.metro.Line;
import org.javaacademy.metro.metro.LineColor;
import org.javaacademy.metro.metro.Metro;
import org.javaacademy.metro.metro.Station;

import java.time.Duration;
import java.time.LocalDate;

public class Runner {
    public static void main(String[] args) throws StationNotAddedException,
            LineNotCreatedException, StationWasNotFoundException, NoWayOutOfStationException {
        Metro metro = new Metro("Пермь");

        Line redLine = metro.createLine(LineColor.RED);
        Line blueLine = metro.createLine(LineColor.BLUE);

        metro.createFirstStation(LineColor.RED, "Спортивная", null);
        metro.createEndStation(LineColor.RED, "Медведковская", Duration.ofSeconds(141), null);
        metro.createEndStation(LineColor.RED, "Молодежная", Duration.ofSeconds(118), null);
        metro.createEndStation(LineColor.RED, "Пермь 1", Duration.ofSeconds(180), blueLine);
        metro.createEndStation(LineColor.RED, "Пермь 2", Duration.ofSeconds(130), null);
        metro.createEndStation(LineColor.RED, "Дворец Культуры", Duration.ofSeconds(266), null);

        metro.createFirstStation(LineColor.BLUE, "Пацанская", null);
        metro.createEndStation(LineColor.BLUE, "Улица Кирова", Duration.ofSeconds(90), null);
        metro.createEndStation(LineColor.BLUE, "Тяжмаш", Duration.ofSeconds(107), redLine);
        metro.createEndStation(LineColor.BLUE, "Нижнекамская", Duration.ofSeconds(199), null);
        metro.createEndStation(LineColor.BLUE, "Соборная", Duration.ofSeconds(108), null);

        System.out.println(metro);

        Station station2 = metro.getStationByName("Пацанская");
        Station station3 = metro.getStationByName("Нижнекамская");

        System.out.println(metro.numberOfRunsBetweenTwoStationsOneLineDirectSearch(station2, station3));
        System.out.println(metro.numberOfRunsBetweenTwoStationsOneLineReverseSearch(station3, station2));

        station2.ticketSales(LocalDate.now(), "Пацанская", "Дворец Культуры");
        station2.ticketSales(LocalDate.now(), "Пацанская", "Нижнекамская");
        station2.ticketSales(LocalDate.of(2019, 3, 10), "Пацанская", "Нижнекамская");
        station3.ticketSales(LocalDate.of(2019, 3, 10), "Нижнекамская", "Пацанская");
        station3.ticketSales(LocalDate.of(2020, 5, 17), "Соборная", "Спортивная");
//        System.out.println(station2.getTicketOffice().getRecordIncome());

        metro.printIncomeAllTicketsOffice();


    }
}