import org.javaacademy.metro.exception.lineexception.LineNotCreatedException;
import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.stationexception.StationNotAddedException;
import org.javaacademy.metro.exception.stationexception.StationWasNotFoundException;
import org.javaacademy.metro.metro.Line;
import org.javaacademy.metro.metro.Metro;
import org.javaacademy.metro.metro.Station;
import org.javaacademy.metro.metro.lineattribute.LineColor;

import java.time.Duration;
import java.time.LocalDate;

public class Runner {
    public static void main(String[] args) throws StationNotAddedException,
            LineNotCreatedException, StationWasNotFoundException, NoWayOutOfStationException {
        Metro metro = new Metro("Пермь");

        Line redLine = metro.createLine(LineColor.RED);
        Line blueLine = metro.createLine(LineColor.BLUE);

        metro.createFirstStation(LineColor.RED, "Спортивная", null);
        metro.createLastStation(LineColor.RED, "Медведковская", Duration.ofSeconds(141), null);
        metro.createLastStation(LineColor.RED, "Молодежная", Duration.ofSeconds(118), null);
        metro.createLastStation(LineColor.RED, "Пермь 1", Duration.ofSeconds(180), blueLine);
        metro.createLastStation(LineColor.RED, "Пермь 2", Duration.ofSeconds(130), null);
        metro.createLastStation(LineColor.RED, "Дворец Культуры", Duration.ofSeconds(266), null);

        metro.createFirstStation(LineColor.BLUE, "Пацанская", null);
        metro.createLastStation(LineColor.BLUE, "Улица Кирова", Duration.ofSeconds(90), null);
        metro.createLastStation(LineColor.BLUE, "Тяжмаш", Duration.ofSeconds(107), redLine);
        metro.createLastStation(LineColor.BLUE, "Нижнекамская", Duration.ofSeconds(199), null);
        metro.createLastStation(LineColor.BLUE, "Соборная", Duration.ofSeconds(108), null);

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