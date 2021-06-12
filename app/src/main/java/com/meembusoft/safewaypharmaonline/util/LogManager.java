package com.meembusoft.safewaypharmaonline.util;
//
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class LogManager {

    private static String TAG = "LogManager";
    private static String DOWNLOADS_PATH;
    private static String LOG_PREFIX;
    private static String LOG_DIR;
    private static boolean sDebugMode = true;
    private static boolean sSaveToDisk = true;
    public static int level = Log.VERBOSE;

    static {
//        DOWNLOADS_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        DOWNLOADS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        LOG_PREFIX = setLogPrefix("");
        LOG_DIR = setLogPath("Safeway pharma");
    }

    public static void v(String msg) {
        trace(Log.VERBOSE, TAG, msg);
    }

    public static void v(String tag, String msg) {
        trace(Log.VERBOSE, tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        trace(Log.VERBOSE, tag, msg, tr);
    }

    public static void d(String msg) {
        trace(Log.DEBUG, TAG, msg);
    }

    public static void d(String tag, String msg) {
        trace(Log.DEBUG, tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        trace(Log.DEBUG, tag, msg, tr);
    }

    public static void i(String msg) {
        trace(Log.INFO, TAG, msg);
    }

    public static void i(String tag, String msg) {
        trace(Log.INFO, tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        trace(Log.INFO, tag, msg, tr);
    }

    public static void w(String msg) {
        trace(Log.WARN, TAG, msg);
    }

    public static void w(String tag, String msg) {
        trace(Log.WARN, tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        trace(Log.WARN, tag, msg, tr);
    }

    public static void e(String msg) {
        trace(Log.ERROR, TAG, msg);
    }

    public static void e(String tag, String msg) {
        trace(Log.ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        trace(Log.ERROR, tag, msg, tr);
    }

    public static void setDebuggable(boolean shouldPrintLogs) {
        sDebugMode = shouldPrintLogs;
    }

    public static void setSaveLogToDisk(boolean shouldSaveToDisk) {
        sSaveToDisk = shouldSaveToDisk;
    }

    public static String setLogPrefix(final String prefix) {
        return TextUtils.isEmpty(prefix) ? "LogManager-" : prefix + "-";
    }

    public static String setLogPath(final String subPath) {
        return TextUtils.isEmpty(subPath) ? DOWNLOADS_PATH + "/logs" : subPath;
    }

    private static void trace(final int type, String tag, final String msg) {
        trace(type, tag, msg, null);
    }

    private static void trace(final int type, final String tag, final String msg, final Throwable tr) {
        if (sDebugMode) {
            switch (type) {
                case Log.VERBOSE:
                    Log.v(tag, msg);
                    break;
                case Log.DEBUG:
                    Log.d(tag, msg);
                    break;
                case Log.INFO:
                    Log.i(tag, msg);
                    break;
                case Log.WARN:
                    Log.w(tag, msg);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg);
                    break;
            }
        }
        if (sSaveToDisk && type >= level) {
            writeLog(type, msg + '\n' + Log.getStackTraceString(tr));
        }
    }

    private static void writeLog(int type, String msg) {
        // Check storage available
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        // Check write permission
//        int permissionStatus = ContextCompat.checkSelfPermission(context, permission);
//        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        try {
            SparseArray<String> logMap = new SparseArray<>();
            logMap.put(Log.VERBOSE, " VERBOSE ");
            logMap.put(Log.DEBUG, " DEBUG ");
            logMap.put(Log.INFO, " INFO ");
            logMap.put(Log.WARN, " WARN ");
            logMap.put(Log.ERROR, " ERROR ");

            msg = "\r\n" + getDateFormat(DateFormatter.SS.getValue()) + logMap.get(type) + msg;
            String fileName = LOG_PREFIX + getDateFormat(DateFormatter.DD.getValue()) + ".txt";
//            String fileName = LOG_PREFIX + getDateFormat(DateFormatter.DD.getValue()) + ".log";
            recordLog(LOG_DIR, fileName, msg, true);
        } catch (Exception e) {
            LogManager.e(LOG_PREFIX, e.getMessage());
        }
    }

    private static void recordLog(String logDir, String fileName, String msg, boolean append) {
        try {
            createDir(logDir);
            final File saveFile = new File(logDir + "/" + fileName);
            if (!append && saveFile.exists()) {
                saveFile.delete();
                saveFile.createNewFile();
                write(saveFile, msg, append);
            } else if (append && saveFile.exists()) {
                write(saveFile, msg, append);
            } else if (!saveFile.exists()) {
                saveFile.createNewFile();
                write(saveFile, msg, append);
            }
        } catch (IOException e) {
            recordLog(logDir, fileName, msg, append);
        }
    }

    private static String getDateFormat(String pattern) {
        final DateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date());
    }

    private static void createDir(String dir) {
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void write(final File file, final String msg, final boolean append) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file, append);
                    try {
                        fos.write(msg.getBytes());
                    } catch (IOException e) {
                        LogManager.e(TAG, "write log failed", e);
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            LogManager.d(TAG, "Exception closing stream: ", e);
                        }
                    }
                } catch (FileNotFoundException e) {
                    LogManager.e(TAG, "write fail failed", e);
                }
                return null;
            }
        }.execute();
    }

    public enum DateFormatter {
        NORMAL("yyyy-MM-dd HH:mm"),
        DD("yyyy-MM-dd"),
        SS("yyyy-MM-dd HH:mm:ss");
        private String value;

        DateFormatter(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}