package ru.practicum.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncodeDate {


    public static String encodeDate(String date) throws UnsupportedEncodingException {
        return URLEncoder.encode(date, StandardCharsets.UTF_8);
    }
}
