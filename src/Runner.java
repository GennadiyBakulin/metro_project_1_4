import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.lineexception.LineNotCreatedException;
import org.javaacademy.metro.exception.stationexception.StationNotAddedException;
import org.javaacademy.metro.exception.stationexception.StationWasNotFoundException;
import org.javaacademy.metro.metro.Metro;
import org.javaacademy.metro.metro.Station;
import org.javaacademy.metro.metro.lineattribute.LineColor;

import java.time.Duration;
import java.time.LocalDate;

public class Runner {
    public static void main(String[] args) throws StationNotAddedException,
            LineNotCreatedException, StationWasNotFoundException, NoWayOutOfStationException {
        Metro metro = new Metro("Пермь");

        metro.createLine(LineColor.RED);
        metro.createLine(LineColor.BLUE);

        metro.createFirstStation(LineColor.RED, "Спортивная");
        metro.createLastStation(LineColor.RED, "Медведковская", Duration.ofSeconds(141));
        metro.createLastStation(LineColor.RED, "Молодежная", Duration.ofSeconds(118));
        metro.createLastStation(LineColor.RED, "Пермь 1", Duration.ofSeconds(180), "Тяжмаш");
        metro.createLastStation(LineColor.RED, "Пермь 2", Duration.ofSeconds(130));
        metro.createLastStation(LineColor.RED, "Дворец Культуры", Duration.ofSeconds(266));

        metro.createFirstStation(LineColor.BLUE, "Пацанская");
        metro.createLastStation(LineColor.BLUE, "Улица Кирова", Duration.ofSeconds(90));
        metro.createLastStation(LineColor.BLUE, "Тяжмаш", Duration.ofSeconds(107), "Пермь 1");
        metro.createLastStation(LineColor.BLUE, "Нижнекамская", Duration.ofSeconds(199));
        metro.createLastStation(LineColor.BLUE, "Соборная", Duration.ofSeconds(108));

        System.out.println(metro);

        Station station2 = metro.getStationByName("Пацанская");
        station2.salesTicket(LocalDate.now(), "Пацанская", "Дворец Культуры");
        station2.salesTicket(LocalDate.now(), "Пацанская", "Нижнекамская");
        station2.salesTicket(LocalDate.of(2019, 3, 10), "Пацанская", "Нижнекамская");
        Station station3 = metro.getStationByName("Нижнекамская");
        station3.salesTicket(LocalDate.of(2019, 3, 10), "Нижнекамская", "Пацанская");
        station3.salesTicket(LocalDate.of(2020, 5, 17), "Соборная", "Спортивная");

        metro.printIncomeAllTicketsOffice();
        System.out.println(metro);
    }
}