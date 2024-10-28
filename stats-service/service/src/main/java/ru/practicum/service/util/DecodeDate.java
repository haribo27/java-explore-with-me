package ru.practicum.service.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class DecodeDate {

    public static String decodeDate(LocalDateTime date) {
        return URLDecoder.decode(String.valueOf(date), StandardCharsets.UTF_8);
    }
}
