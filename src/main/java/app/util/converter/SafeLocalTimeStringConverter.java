package app.util.converter;

import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SafeLocalTimeStringConverter extends LocalTimeStringConverter {
    public SafeLocalTimeStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        super(formatter, parser);
    }

    @Override
    public LocalTime fromString(String value) {
        try {
            return super.fromString(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @Override
    public String toString(LocalTime value) {
        return super.toString(value);
    }
}
