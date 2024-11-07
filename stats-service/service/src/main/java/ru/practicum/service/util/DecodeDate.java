package ru.practicum.service.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class DecodeDate {

    public static String decodeDate(String date) {
        String onceDecoded = URLDecoder.decode(date, StandardCharsets.UTF_8);
        return URLDecoder.decode(onceDecoded, StandardCharsets.UTF_8);
    }
}
