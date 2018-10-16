package bettersum.tools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Clock {

    private final ZoneId timeZone;

    private long start;
    private long stop;

    public Clock() {
        this("Z");
    }

    public Clock(String timeZone) {
        this.start = 0;
        this.stop = 0;
        this.timeZone = ZoneId.of(timeZone);
    }

    public String datetime(DateTimeFormatter formatter) {
        return LocalDate.now().format(formatter);
    }

    public String datetime(FormatStyle style, Locale locale) {
        return datetime(DateTimeFormatter.ofLocalizedDate(style).withLocale(locale));
    }

    public String datetime() {
        return datetime(FormatStyle.SHORT, Locale.UK);
    }

    public void start() {
        this.start = System.nanoTime();
    }

    public void stop() {
        this.stop = System.nanoTime();
    }

    public long resultTime() {
        return resultTime(TimeUnit.NANOSECONDS);
    }

    public long timePassed() {
        return timePassed(TimeUnit.NANOSECONDS);
    }

    public long resultTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.stop - this.start, TimeUnit.NANOSECONDS);
    }

    public long timePassed(TimeUnit timeUnit) {
        return timeUnit.convert(System.nanoTime() - this.start, TimeUnit.NANOSECONDS);
    }

    public int second() {
        return LocalTime.now(timeZone).getSecond();
    }

    public int minute() {
        return LocalTime.now(timeZone).getMinute();
    }

    public int hour() {
        return LocalTime.now(timeZone).getHour();
    }

    public int day() {
        return LocalDate.now(timeZone).getDayOfMonth();
    }

    public int month() {
        return LocalDate.now(timeZone).getMonthValue();
    }

    public int year() {
        return LocalDate.now(timeZone).getYear();
    }

    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
