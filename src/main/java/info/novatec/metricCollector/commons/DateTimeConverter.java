package info.novatec.metricCollector.commons;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.springframework.stereotype.Component;

import lombok.Setter;


@Component
public class DateTimeConverter {

    @Setter
    private LocalDate baseDate; //required to add past data for testing

    public DateTimeConverter() {
        this.baseDate = LocalDate.now();
    }

    public long getCurrentDateInSeconds(int minusDays) {
        return baseDate.minusDays(minusDays).atStartOfDay(ZoneId.of("Z")).toEpochSecond();
    }

    public long getDateInSecondsMinusDays(long dateInSeconds, int minusDays){
        return dateInSeconds - (minusDays*86400);
    }

    /**
     * Example input: Mon Mar 20 21:44:45 CET 2017
     * output: 2017-03-20T21:44:45
     */
    public String twitterToInflux(String dateTime){
        Locale dateLocale = Locale.UK;
        DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy", dateLocale);
        TemporalAccessor date = inFormatter.parse(dateTime);
        DateTimeFormatter outFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return outFormatter.format(date);
    }

    public String twitterDateTimeToInfluxDate(String dateTime){
        Locale dateLocale = Locale.UK;
        DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy", dateLocale);
        TemporalAccessor date = inFormatter.parse(dateTime);
        DateTimeFormatter outFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return outFormatter.format(date);
    }

    public long influxToSeconds(String dateTime){
        return LocalDate.parse(dateTime).atStartOfDay(ZoneId.of("Z")).toEpochSecond();
    }

    public long twitterToSeconds(String dateTime){
        return influxToSeconds(twitterDateTimeToInfluxDate(dateTime));
    }
}
