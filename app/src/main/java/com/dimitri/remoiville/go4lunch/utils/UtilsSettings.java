package com.dimitri.remoiville.go4lunch.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UtilsSettings {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateName(String name) {
        return name.length() <= 0;
    }
}
