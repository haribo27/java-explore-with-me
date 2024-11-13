package ru.practicum.service.decoder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Decoder {

    public static LocalDateTime decodeString(String date) {
        String decodedDate = URLDecoder.decode(date, StandardCharsets.UTF_8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(decodedDate, formatter);
    }
}
