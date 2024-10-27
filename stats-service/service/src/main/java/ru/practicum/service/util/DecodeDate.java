package ru.practicum.service.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DecodeDate {

    public static String decodeDate(String date) {
        return URLDecoder.decode(date, StandardCharsets.UTF_8);
    }
}