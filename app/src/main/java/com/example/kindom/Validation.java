package com.example.kindom;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class Validation {

    private static final Pattern PATTERN_PASSWORD = Pattern.compile(
            "^" +
            "(?=.*[A-Z])" +    // at least 1 uppercase letter
            "(?=.*[a-z])" +    // at least 1 lowercase letter
            "(?=.*[0-9])" +    // at least 1 number
            ".{8,}");          // at least 8 characters


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isNonEmpty(CharSequence target) {
        return !TextUtils.isEmpty(target);
    }

    public static boolean isValidPassword(CharSequence targert) {
        return isNonEmpty(targert) && PATTERN_PASSWORD.matcher(targert).matches();
    }
}