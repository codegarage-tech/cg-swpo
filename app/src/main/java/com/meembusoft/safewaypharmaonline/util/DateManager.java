package com.meembusoft.safewaypharmaonline.util;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DateManager {

    public static final String TAG = DateManager.class.getSimpleName();

    /**
     * @param time        The base time
     * @param secondValue The second value you want to add or subtract
     * @return String The time after adding or subtracting the second
     */
    public static String getAddedOrSubtractedTime(String time, int secondValue) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            Date d = df.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.SECOND, secondValue);
            return df.format(cal.getTime());
        } catch (Exception ex) {
            Logger.d(TAG, "Exception: " + ex.getMessage());
        }
        return "";
    }

    /**
     * @param initialTime Start time in format hh:mm a
     * @param finalTime   End time in format hh:mm a
     * @param currentTime Current time in format hh:mm a
     * @return true if initialTime <= timeToCheck < finalTime, otherwise false
     */
    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime) {
        try {
            String reg = "^((1[0-2]|0?[1-9]):([0-5][0-9]) ([AaPp][Mm]))$";

            if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg)) {
                boolean valid = false;
                //Start Time
                //all times are from java.util.Date
                Date inTime = new SimpleDateFormat("hh:mm a").parse(initialTime);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(inTime);

                //Current Time
                Date checkTime = new SimpleDateFormat("hh:mm a").parse(currentTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(checkTime);

                //End Time
                Date finTime = new SimpleDateFormat("hh:mm a").parse(finalTime);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(finTime);

                if (finalTime.compareTo(initialTime) < 0) {
                    calendar2.add(Calendar.DATE, 1);
                    calendar3.add(Calendar.DATE, 1);
                }

                java.util.Date actualTime = calendar3.getTime();
                if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0) && actualTime.before(calendar2.getTime())) {
                    valid = true;
                    return valid;
                }
            }

//            if (argStartTime.matches(reg) && argEndTime.matches(reg) && argCurrentTime.matches(reg)) {
//                boolean valid = false;
//                // Start Time
//                java.util.Date startTime = new SimpleDateFormat("hh:mm a").parse(argStartTime);
//                Calendar startCalendar = Calendar.getInstance();
//                startCalendar.setTime(startTime);
//                // Current Time
//                java.util.Date currentTime = new SimpleDateFormat("hh:mm a").parse(argCurrentTime);
//                Calendar currentCalendar = Calendar.getInstance();
//                currentCalendar.setTime(currentTime);
//                // End Time
//                java.util.Date endTime = new SimpleDateFormat("hh:mm a").parse(argEndTime);
//                Calendar endCalendar = Calendar.getInstance();
//                endCalendar.setTime(endTime);
//                //
//                if (currentTime.compareTo(endTime) < 0) {
//                    currentCalendar.add(Calendar.DATE, 1);
//                    currentTime = currentCalendar.getTime();
//                }
//                if (startTime.compareTo(endTime) < 0) {
//                    startCalendar.add(Calendar.DATE, 1);
//                    startTime = startCalendar.getTime();
//                }
//                //
//                if (currentTime.before(startTime)) {
//                    Logger.d(TAG, " Time is Lesser ");
//                    valid = false;
//                } else {
//                    if (currentTime.after(endTime)) {
//                        endCalendar.add(Calendar.DATE, 1);
//                        endTime = endCalendar.getTime();
//
//                    }
//
//                    Logger.d(TAG, "Comparing , Start Time: " + startTime);
//                    Logger.d(TAG, "Comparing , End Time: " + endTime);
//                    Logger.d(TAG, "Comparing , Current Time: " + currentTime);
//
//                    if (currentTime.before(endTime)) {
//                        Logger.d(TAG, "RESULT, Time lies b/w");
//                        valid = true;
//                    } else {
//                        valid = false;
//                        Logger.d(TAG, "RESULT, Time does not lies b/w");
//                    }
//                }
//                return valid;
//            }
        } catch (Exception ex) {
            Logger.d(TAG, "Exception: " + ex.getMessage());
        }
        return false;
    }

    //Date time format
    public static final String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
    public static final String dd_MM_yyyy_hh_mm_ss = "dd-MM-yyyy hh:mm:ss";
    public static final String yyyy_MM_dd_hh_mm = "yyyy-MM-dd hh:mm";

    public static String convertDateTime(String date, String originalFormat, String outputFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(originalFormat, Locale.US);
        try {
            Date mDate = formatter.parse(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat, new Locale("US"));
            return dateFormat.format(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String formatDateFromString(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Logger.d("ParseException", "ParseException - dateFormat");
        }

        return outputDate;

    }


    public static String formatCurrentDateTime(String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final CharSequence getFormattedTimestamp(long timestamp) {
        return DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    public static long convertDateTimeToMillisecond(String dateTime, String format) {
//        String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        long timeInMilliseconds = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date mDate = simpleDateFormat.parse(dateTime);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            timeInMilliseconds = 0;
        }
        Log.d(TAG, TAG + ">> " + "DateTimeToMillisecond: " + timeInMilliseconds);

        return timeInMilliseconds;
    }

    public static String getTimeStringFromDate(Date date) {
        String dateString = "";

        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            dateString = curFormater.format(date);

        } catch (Exception e) {
            Log.d(TAG, TAG + ">> " + "Exception: " + e.toString());

        }

        return dateString;
    }

    public static Date getDateTimeNowbyAddingTime(int hours) {
        GregorianCalendar cal = null;
        try {
            cal = new GregorianCalendar();
            cal.add(Calendar.HOUR, hours);
        } catch (Exception e) {
            Log.d(TAG, TAG + ">> " + "Exception: " + e.toString());

        }
        return cal.getTime();
    }

    public static String millsToTimeFormat(long mills) {

        Date date = new Date(mills);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss a");
        String dateFormatted = formatter.format(date);
        return dateFormatted; //note that it will give you the time in GMT+0
    }

    public static long getTransactionTime(String givenDateString) {
        long timeInMilliseconds = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            Log.d(TAG, TAG + ">> " + "timeInMilliseconds: " + timeInMilliseconds + "");

        } catch (Exception ex) {
            Log.e("ParseException", ex.getMessage() + "Exception ");

        }
        return timeInMilliseconds;
    }

}