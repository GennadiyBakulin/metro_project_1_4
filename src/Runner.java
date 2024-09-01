import org.javaacademy.metro.exception.LineNotCreatedException;
import org.javaacademy.metro.exception.NoWayOutOfStationException;
import org.javaacademy.metro.exception.StationNotAddedException;
import org.javaacademy.metro.exception.StationWasNotFoundException;
import org.javaacademy.metro.metro.Line;
import org.javaacademy.metro.metro.LineColor;
import org.javaacademy.metro.metro.Metro;
import org.javaacademy.metro.metro.Station;

import java.time.Duration;

public class Runner {
    public static void main(String[] args) throws StationNotAddedException, LineNotCreatedException, StationWasNotFoundException, NoWayOutOfStationException {
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

//        System.out.println(metro.findTransferStations(blueLine, redLine));

        Station station1 = redLine.getStationByName("Пермь 2");
        Station station2 = redLine.getStationByName("Медведковская");

//        System.out.println(station1.getPrevious());
//        System.out.println(station1.getNext());

        System.out.println(metro.numberOfRunsBetweenTwoStationsOneLine(blueLine, station1, station2));

//        System.out.println(metro.numberOfRunsBetweenTwoStationsOneLineDirectSearch(redLine, station1, station2));
//        System.out.println(metro.numberOfRunsBetweenTwoStationsOneLineReverseSearch(redLine, station1, station2));
//        System.out.println(metro);

//        System.out.println(redLine.getStations().indexOf(new Station("Пермь 1", null, blueLine)));
    }
}