import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.lineexception.LineCreatedException;
import org.javaacademy.metro.exception.lineexception.LineNotFoundException;
import org.javaacademy.metro.exception.stationexception.StationCreateException;
import org.javaacademy.metro.exception.stationexception.StationNotFoundException;
import org.javaacademy.metro.metro.Metro;
import org.javaacademy.metro.metro.Station;
import org.javaacademy.metro.metro.lineattribute.LineColor;

import java.time.Duration;
import java.time.LocalDate;

public class Runner {
    public static void main(String[] args) throws StationNotFoundException, NoWayOutOfStationException,
            LineNotFoundException, LineCreatedException, StationCreateException {
        Metro metro = new Metro("Пермь");

        metro.createLine(LineColor.RED);
        metro.createLine(LineColor.BLUE);

        Station sports = metro.createFirstStation(LineColor.RED, "Спортивная");
        Station medvedkovskaya = metro.createLastStation(LineColor.RED, "Медведковская", Duration.ofSeconds(141));
        Station junior = metro.createLastStation(LineColor.RED, "Молодежная", Duration.ofSeconds(118));
        Station perm1 = metro.createLastStation(LineColor.RED, "Пермь 1", Duration.ofSeconds(180));
        Station perm2 = metro.createLastStation(LineColor.RED, "Пермь 2", Duration.ofSeconds(130));
        Station palaceOfCulture = metro.createLastStation(LineColor.RED, "Дворец Культуры", Duration.ofSeconds(266));

        Station patsanskaya = metro.createFirstStation(LineColor.BLUE, "Пацанская");
        Station streetOfKirova = metro.createLastStation(LineColor.BLUE, "Улица Кирова", Duration.ofSeconds(90));
        Station tyazhmash = metro.createLastStation(LineColor.BLUE, "Тяжмаш", Duration.ofSeconds(107));
        Station nizhnekamsk = metro.createLastStation(LineColor.BLUE, "Нижнекамская", Duration.ofSeconds(199));
        Station cathedral = metro.createLastStation(LineColor.BLUE, "Соборная", Duration.ofSeconds(108));

        perm1.addChangeLine(tyazhmash);

        System.out.println(metro);

        patsanskaya.salesTicket(LocalDate.now(), "Пацанская", "Дворец Культуры");
        patsanskaya.salesTicket(LocalDate.now(), "Пацанская", "Нижнекамская");
        patsanskaya.salesTicket(LocalDate.of(2019, 3, 10), "Пацанская", "Нижнекамская");
        nizhnekamsk.salesTicket(LocalDate.of(2019, 3, 10), "Нижнекамская", "Пацанская");
        nizhnekamsk.salesTicket(LocalDate.of(2020, 5, 17), "Соборная", "Спортивная");
        metro.printIncomeAllTicketsOffice();
        nizhnekamsk.salesTravelTicket(LocalDate.of(2020, 5, 17));
        System.out.println(metro.validityCheckTravelTicket("a0000", LocalDate.of(2020, 5, 17)));
    }
}