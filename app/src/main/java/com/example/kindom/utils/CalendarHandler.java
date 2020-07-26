package com.example.kindom.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Encapsulate the logic of handling date and time objects
 */
public class CalendarHandler {

    /**
     * Get a String representation of the current date time in the format of YYYY-MM-DD[T]HH:mm:ss
     *
     * @return String representation of the current date time
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateTime() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(date);
        return dateString + "T" + timeString;
    }

    /**
     * Get a date String representation in the format of DD/MM/YYYY
     *
     * @param year       the year
     * @param month      the date
     * @param dayOfMonth the day of month
     * @return String representing a date
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateString(int year, int month, int dayOfMonth) {
        String year_string = Integer.toString(year);
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(dayOfMonth);
        if (month_string.length() == 1) {
            // Append a zero in front if month is 1 digit
            month_string = "0" + month_string;
        }
        if (day_string.length() == 1) {
            // Append a zero in front if day is 1 digit
            day_string = "0" + day_string;
        }
        return day_string + "/" + month_string + "/" + year_string;
    }

    /**
     * Get a simplified date String representation in the format of d MMM
     *
     * @param dateString the date
     * @return simplified representation of a date
     */
    @SuppressLint("SimpleDateFormat")
    public static String getSimplifiedDateString(String dateString) {
        String date = "";
        try {
            Date dateObj = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
            DateFormat dateFormat = new SimpleDateFormat("d MMM");
            assert dateObj != null;
            date = dateFormat.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Get a time String representation in the format of HH:MM AM/PM
     *
     * @param hourOfDay the hour of day
     * @param minute    the minute
     * @return String representing a time
     */
    public static String getTimeString(int hourOfDay, int minute) {
        int hour = hourOfDay < 12 ? hourOfDay : hourOfDay - 12;
        String hourString = Integer.toString(hour);
        String minuteString = Integer.toString(minute);
        if (hourString.equals("0")) {
            hourString = "12";
        }
        if (hourString.length() == 1) {
            // Append a zero in front if hour is 1 digit
            hourString = "0" + hourString;
        }
        if (minuteString.length() == 1) {
            // Append a zero in front if minute is 1 digit
            minuteString = "0" + minuteString;
        }
        String am_pm_string = hourOfDay < 12 ? "AM" : "PM";
        return hourString + ":" + minuteString + " " + am_pm_string;
    }

    /**
     * Check if the date and time are in the past
     *
     * @param dateString the date
     * @param timeString the time
     * @return if the date and time are in the past
     */
    public static boolean checkIfExpired(String dateString, String timeString) {
        String concat = dateString + " " + timeString;
        boolean expired = true;
        try {
            @SuppressLint("SimpleDateFormat")
            Date dateObj = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(concat);
            Calendar postDate = Calendar.getInstance();
            assert dateObj != null;
            postDate.setTime(dateObj);
            postDate.set(Calendar.SECOND, 60);
            Calendar currDate = Calendar.getInstance();
            expired = postDate.before(currDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return expired;
    }
}