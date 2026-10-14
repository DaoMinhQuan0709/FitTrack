package com.fittrack.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    private static final String PATTERN = "yyyy-MM-dd";

    private DateUtils() {
        // utility class
    }

    public static String today() {
        return format(new Date());
    }

    public static String format(Date date) {
        return new SimpleDateFormat(PATTERN, Locale.getDefault()).format(date);
    }

    /** Returns date (yyyy-MM-dd) after adding offsetDays to current date. */
    public static String offsetFromToday(int offsetDays) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, offsetDays);
        return format(cal.getTime());
    }

    /** Returns date (yyyy-MM-dd) after adding offsetDays to isoDate. */
    public static String offsetDate(String isoDate, int offsetDays) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat(PATTERN, Locale.getDefault());
            Calendar cal = Calendar.getInstance();
            cal.setTime(parser.parse(isoDate));
            cal.add(Calendar.DAY_OF_YEAR, offsetDays);
            return format(cal.getTime());
        } catch (Exception e) {
            return isoDate;
        }
    }

    /** Format "yyyy-MM-dd" -> "EEEE, MMMM d" (e.g., Saturday, August 15). */
    public static String toHeaderFormat(String isoDate) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat(PATTERN, Locale.getDefault());
            SimpleDateFormat display = new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH);
            return display.format(parser.parse(isoDate));
        } catch (Exception e) {
            return isoDate;
        }
    }

    /** Format "yyyy-MM-dd" -> "dd/MM" (e.g., 15/08). */
    public static String toChartFormat(String isoDate) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat(PATTERN, Locale.getDefault());
            SimpleDateFormat display = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
            return display.format(parser.parse(isoDate));
        } catch (Exception e) {
            return "";
        }
    }

    /** Format "yyyy-MM-dd" -> "dd/MM/yyyy" for display. */
    public static String toDisplayFormat(String isoDate) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat(PATTERN, Locale.getDefault());
            SimpleDateFormat display = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return display.format(parser.parse(isoDate));
        } catch (Exception e) {
            return isoDate;
        }
    }
}
