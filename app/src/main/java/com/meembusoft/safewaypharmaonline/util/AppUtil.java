package com.meembusoft.safewaypharmaonline.util;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.MedicineRemainderActivity;
import com.meembusoft.safewaypharmaonline.base.BaseUpdateListener;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.flytocart.CircleAnimationUtil;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.DayScheduling;
import com.meembusoft.safewaypharmaonline.model.OrderItems;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.realm.RealmManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AppUtil {

    private static String TAG = AppUtil.class.getSimpleName();
    private static AlertDialog pictureDialog = null;

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServiceInfos != null && runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns List of the List argument passed to this function with size = chunkSize
     *
     * @param list      input list to be portioned
     * @param chunkSize maximum size of each partition
     * @param <T>       Generic type of the List
     * @return A list of Lists which is portioned from the original list
     */
    public static <T> List<List<T>> chunkList(List<T> list, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Invalid chunk size: " + chunkSize);
        }
        List<List<T>> chunkList = new ArrayList<>(list.size() / chunkSize);
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunkList.add(list.subList(i, i + chunkSize >= list.size() ? list.size() : i + chunkSize));
        }
        return chunkList;
    }


    public static byte[] getOriginalSsidBytes(WifiInfo info) {
        try {
            Method method = info.getClass().getMethod("getWifiSsid");
            method.setAccessible(true);
            Object wifiSsid = method.invoke(info);
            method = wifiSsid.getClass().getMethod("getOctets");
            method.setAccessible(true);
            return (byte[]) method.invoke(wifiSsid);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> ArrayList<T> convertToArrayList(T[] objectArray) {
        return new ArrayList<T>(Arrays.asList(objectArray));
    }

    public static boolean hasNavigationBar(Context context) {
        boolean hasSoftwareKeys = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth > displayWidth) || (realHeight > displayHeight);
        } else {
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }
        return hasSoftwareKeys;
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            navigationBarHeight = (realHeight > displayHeight) ? (realHeight - displayHeight) : 0;
        }
        return navigationBarHeight;
    }

    public static String get12HourTime(int hourOfDay, int minute, int second) {
        int hour = hourOfDay;
        int minutes = minute;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String hr = "";
        if (hour < 10)
            hr = "0" + hour;
        else
            hr = String.valueOf(hour);
        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);
        String sec = "";
        if (second < 10)
            sec = "0" + second;
        else
            sec = String.valueOf(second);

        // Append in a StringBuilder
        String aTime = new StringBuilder()
                .append(hr).append(':')
                .append(min).append(':')
                .append(sec)
                .append(" ")
                .append(timeSet)
                .toString();
        return aTime;
    }

    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);
        }
        return false;
    }

    public static String convertToCamelCase(String str) {
        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static void generateQRCode(String text, ImageView imageView, int width, int height) {
        try {
            if (!AllSettingsManager.isNullOrEmpty(text)) {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, width, height);
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    //Toolbar
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int toPx(Context context, int value) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }

    public static void applyMarqueeOnTextView(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(-1);
        textView.setSelected(true);
    }

    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.dp_180);
        return Math.round(screenWidth / cellWidth);
    }


    public static int getGridSpanCount3(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.dp_120);
        return Math.round(screenWidth / cellWidth);
    }

    public static String formatLocationInfo(String myString) {
        String location = "";
        if (myString != null && myString.trim().length() > 0) {
            location = myString.startsWith(",") ? myString.substring(1).trim().replaceAll(", ,", ",") : myString.replaceAll(", ,", ",");
        }
        return location;
    }

    public static <T> void loadImage(Context context, ImageView imageView, T imageSource, boolean isGif, boolean isRoundedImage, boolean isPlaceHolder) {
        try {
            RequestManager requestManager = Glide.with(context);
            RequestBuilder requestBuilder = isGif ? requestManager.asGif() : requestManager.asBitmap();

            //Dynamic loading without caching while update need for each time loading
//            requestOptions.signature(new ObjectKey(System.currentTimeMillis()));
            //If Cache needed
//            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

            //If Cache needed
            RequestOptions requestOptionsCache = new RequestOptions();
            requestOptionsCache.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            requestBuilder.apply(requestOptionsCache);

            //For placeholder
            if (isPlaceHolder) {
                RequestOptions requestOptionsPlaceHolder = new RequestOptions();
                requestOptionsPlaceHolder.placeholder(R.mipmap.ic_launcher);
                requestBuilder.apply(requestOptionsPlaceHolder);
            }

            //For error
            RequestOptions requestOptionsError = new RequestOptions();
            requestOptionsError.error(R.drawable.vector_ic_error);
            requestBuilder.apply(requestOptionsError);

            //For rounded image
            if (isRoundedImage) {
                RequestOptions requestOptionsRounded = new RequestOptions();
                requestOptionsRounded.circleCrop();
                requestOptionsRounded.autoClone();
                requestBuilder.apply(requestOptionsRounded);
            }

            //Generic image source
            T mImageSource = null;
            if (imageSource instanceof String) {
                if (!AllSettingsManager.isNullOrEmpty((String) imageSource)) {
                    mImageSource = imageSource;
                }
            } else if (imageSource instanceof Integer) {
                mImageSource = imageSource;
            }
            requestBuilder.load((mImageSource != null) ? mImageSource : R.drawable.vector_ic_error);

            //Load into image view
            requestBuilder.into(imageView);

//            Glide
//                    .with(context)
//                    .asBitmap()
//                    .load((mImageSource != null) ? mImageSource : R.mipmap.ic_launcher)
//                    .apply(requestOptions)
//                    .into(imageView);

        } catch (Exception e) {
        }
    }

    public static ValueAnimator flashView(final View viewGroup, long time) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                viewGroup.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        animator.setDuration(time);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.start();
        return animator;
    }

//    public static KitchenTime getCurrentKitchenTime(List<KitchenTime> kitchenTimes) {
//        if (kitchenTimes != null && kitchenTimes.size() > 0) {
//            Calendar c = Calendar.getInstance();
//            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//
//            String kitchenTime = "";
//            if (timeOfDay >= 6 && timeOfDay < 10) {
//                kitchenTime = "breakfast";
//            } else if (timeOfDay >= 10 && timeOfDay < 12) {
//                kitchenTime = "morning snacks";
//            } else if (timeOfDay >= 12 && timeOfDay < 16) {
//                kitchenTime = "lunch";
//            } else if (timeOfDay >= 16 && timeOfDay < 19) {
//                kitchenTime = "evening snacks";
//            } else if (timeOfDay >= 19 && timeOfDay < 22) {
//                kitchenTime = "dinner";
//            }
//
//            for (KitchenTime mKitchenTime : kitchenTimes) {
//                if (mKitchenTime.getPrepare_time().equalsIgnoreCase(kitchenTime)) {
//                    return mKitchenTime;
//                }
//            }
//        }
//        return null;
//    }

    public static void makeFlyAnimation(Activity activity, View sourceView, View sourceViewCopy, View destinationView, int timeMilliSecond, Animator.AnimatorListener animatorListener) {
        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(sourceViewCopy)
                .setMoveDuration(timeMilliSecond)
                .setDestView(destinationView)
                .setAnimationListener(animatorListener).startAnimation();
    }

    public static RotateAnimation makeRotateAnimation(final View view, final int rotationCount, final BaseUpdateListener baseUpdateListener) {
        int previousDegrees = 0;
        int degrees = 360;
        final RotateAnimation animation = new RotateAnimation(previousDegrees, degrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setDuration(1500);
        animation.setAnimationListener(new Animation.AnimationListener() {

            int count = 0;

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rotationCount > 0) {
                    count++;
                    if (count == rotationCount) {
                        if (baseUpdateListener != null) {
                            baseUpdateListener.onUpdate(true);
                        }
                    } else {
                        view.startAnimation(animation);
                    }
                } else {
                    view.startAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
        return animation;
    }

    public static void makeFlyAnimation(Activity activity, View sourceView, int sourceViewBitmap, View destinationView, int timeMilliSecond, Animator.AnimatorListener animatorListener) {

        //Create a copy view
        ImageView animImg = new ImageView(activity);
//        Bitmap bm = ((BitmapDrawable) sourceView.getDrawable()).getBitmap();
        //  animImg.setImageBitmap(sourceViewBitmap);
        animImg.setImageResource(sourceViewBitmap);

        ViewGroup anim_mask_layout = CircleAnimationUtil.createAnimLayout(activity);
        anim_mask_layout.addView(animImg);

        int[] startXY = new int[2];
        sourceView.getLocationInWindow(startXY);
        final View v = CircleAnimationUtil.addViewToAnimLayout(activity, animImg, startXY, true);
        if (v == null) {
            return;
        }

        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(v)
                .setMoveDuration(timeMilliSecond)
                .setDestView(destinationView)
                .setAnimationListener(animatorListener).startAnimation();
    }

    public static StaggeredMedicineByItem getMedicineByItem(List<StaggeredMedicineByItem> medicineItems, StaggeredMedicineByItem medicineItem) {
        for (int i = 0; i < medicineItems.size(); i++) {
            if (medicineItems.get(i).getId().equalsIgnoreCase(medicineItem.getId())) {
                return medicineItems.get(i);
            }
        }
        return null;
    }


    public static void datePicker(String date, final EditText editText, boolean numberFormate, Context context) {

        int year = -1;
        int month = -1;
        int day = -1;

        if (numberFormate) {
            if (!TextUtils.isEmpty(date)) {
                String sDate[] = date.split("-");
                year = Integer.parseInt(sDate[0].trim());
                month = Integer.parseInt(sDate[1].trim()) - 1;
                day = Integer.parseInt(sDate[2].trim());
            } else {
                Calendar dateCalender = Calendar.getInstance();
                year = dateCalender.get(Calendar.YEAR);
                month = dateCalender.get(Calendar.MONTH);
                day = dateCalender.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            Calendar dateCalender = Calendar.getInstance();
            year = dateCalender.get(Calendar.YEAR);
            month = dateCalender.get(Calendar.MONTH);
            day = dateCalender.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                //2001-05-03
                // dayOfMonth  monthOfYear
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                if (monthOfYear < 10) {

                    formattedMonth = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                if (numberFormate) {
                    editText.setText(year + "-" + formattedMonth + "-" + formattedDayOfMonth);
                } else {

                    String getDate = DateManager.convertDateTime(year + "-" + formattedMonth + "-" + formattedDayOfMonth, "yyyy-MM-dd", "dd MMM yyyy");
                    if (getDate != null) {
                        editText.setText(getDate);
                    }
                }
                //  editText.setText(year + "-" + formattedMonth + "-" + formattedDayOfMonth);

            }
        }, year, month, day);
        datePickerDialog.setTitle("SELECT DATE");
        datePickerDialog.show();
    }


    public static void timePicker(String time, final EditText editText, Context context) {
        int hour = -1;
        int minute = -1;

        if (!TextUtils.isEmpty(time)) {
            if (time.trim().length() > 6) {
                String sDate[] = time.split(":");
                hour = Integer.parseInt(sDate[0].trim());
                String minutes = sDate[1].trim();
                String substring = minutes.length() > 2 ? minutes.substring(minutes.length() - 2) : minutes;
                minute = Integer.parseInt(minutes.substring(0, 2));
                if (substring.equalsIgnoreCase("PM") || (substring.equalsIgnoreCase("pm"))) {
                    hour = hour + 12;
                }
            } else {
                hour = Integer.parseInt(time.trim().substring(0, 2));
                String substring = time.length() > 2 ? time.substring(time.length() - 2) : time;

                if (substring.equalsIgnoreCase("PM") || (substring.equalsIgnoreCase("pm"))) {
                    hour = hour + 12;
                }
                minute = 0;
            }

        } else {

            Calendar timeCalender = Calendar.getInstance();
            hour = timeCalender.get(Calendar.HOUR_OF_DAY);
            minute = timeCalender.get(Calendar.MINUTE);

        }

        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int minutes = minute;
                String format;
                if (hourOfDay == 0) {
                    hourOfDay += 12;
                    format = "AM";
                } else if (hourOfDay == 12) {
                    format = "PM";
                } else if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                String min = "";
                if (minutes < 10)
                    min = "0" + minutes;
                else
                    min = String.valueOf(minutes);

                String hour = "";
                if (hourOfDay < 10)
                    hour = "0" + hourOfDay;
                else
                    hour = String.valueOf(hourOfDay);
// hourOfDay = hourOfDay + 1;
                editText.setText(hour + " : " + min + " " + format);

            }

        }, hour, minute, false);
        timePickerDialog.setTitle("SELECT TIME");
        timePickerDialog.show();
    }

    public static int getStaggeredMedicineByItemPosition(List<StaggeredMedicineByItem> medicineItems, StaggeredMedicineByItem foodItem) {
        for (int i = 0; i < medicineItems.size(); i++) {
            if (medicineItems.get(i).getId().equalsIgnoreCase(foodItem.getId())) {
                return i;
            }
        }
        return -1;
    }

    public static List<StaggeredMedicineByItem> getIsCheckoutItemList(Activity activity) {
        List<StaggeredMedicineByItem> dbMedicineItems = getAllStoredMedicineByItems(activity);
        List<StaggeredMedicineByItem> medicineItem = new ArrayList<>();
        for (StaggeredMedicineByItem staggeredMedicineByItem : dbMedicineItems) {
            if (staggeredMedicineByItem.getIsCheckedItem()) {
                medicineItem.add(staggeredMedicineByItem);
            }
        }
        return medicineItem;
    }

    public static void getIsUnCheckAllItemList(Activity activity) {
        List<StaggeredMedicineByItem> dbMedicineItems = getAllStoredMedicineByItems(activity);
        for (StaggeredMedicineByItem staggeredMedicineByItem : dbMedicineItems) {
            staggeredMedicineByItem.setIsCheckedItem(false);
            storeSelectedMedicineByItem(activity, staggeredMedicineByItem);
        }
        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> getIsUnCheckAllItemList: " + dbMedicineItems.toString());

    }

    public static void storeSelectedMedicineByItem(Activity activity, StaggeredMedicineByItem medicineByItem) {
        RealmManager mRealmManager = RealmManager.with(activity);
        mRealmManager.insertOrUpdate(medicineByItem);
        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> storeSelectedStaggeredMedicineByItem: " + ((StaggeredMedicineByItem) mRealmManager.getData(StaggeredMedicineByItem.class, AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineByItem.getId())));
    }

    public static void deleteSelectedMedicineByItem(Activity activity, StaggeredMedicineByItem medicineByItem) {
        RealmManager mRealmManager = RealmManager.with(activity);
        mRealmManager.deleteData(StaggeredMedicineByItem.class, AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineByItem.getId());
    }

    //
    public static void deleteAllStoredMedicineByItems(Activity activity) {
        RealmManager mRealmManager = RealmManager.with(activity);
        mRealmManager.deleteAllData(StaggeredMedicineByItem.class);
    }

    public static void deleteAllStoredMedicineByItems(Activity activity, FlavorType mFlavorType) {
        try {
            RealmManager mRealmManager = RealmManager.with(activity);
            List<StaggeredMedicineByItem> mFlavorMedicineByItemData = AppUtil.getAllStoredMedicineByItems(activity, mFlavorType);
            if (mFlavorMedicineByItemData != null && mFlavorMedicineByItemData.size() > 0) {
                Log.d(TAG, "mFlavorMedicineByItemData to deleteData: " + mFlavorMedicineByItemData.toString());
                Log.d(TAG, "size to deleteData: " + mFlavorMedicineByItemData.size());

                for (StaggeredMedicineByItem medicineItem : mFlavorMedicineByItemData) {
                    if (mFlavorType == FlavorType.SUPPLIER) {
                        mRealmManager.deleteData(StaggeredMedicineByItem.class, new HashMap<String, Object>() {{
                            put(AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineItem.getId());
                            put(AllConstants.TABLE_KEY_MEDICINE_ITEM_USER_TYPE, 1);
                        }});
                    } else {
                        mRealmManager.deleteData(StaggeredMedicineByItem.class, new HashMap<String, Object>() {{
                            put(AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineItem.getId());
                            put(AllConstants.TABLE_KEY_MEDICINE_ITEM_USER_TYPE, 0);
                        }});
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to deleteData: " + e.getMessage());
        }

    }

    public static void removedUnselectedStaggeredMedicineByItems(Activity activity) {
        for (StaggeredMedicineByItem medicineItem : getAllStoredMedicineByItems(activity)) {
            if (medicineItem.getItem_quantity() == 0) {
                deleteSelectedMedicineByItem(activity, medicineItem);
            }
        }
    }

    public static StaggeredMedicineByItem getStoredMedicineByItem(Activity activity, StaggeredMedicineByItem medicineItem) {
        RealmManager mRealmManager = RealmManager.with(activity);
        StaggeredMedicineByItem mMedicineItem = ((StaggeredMedicineByItem) mRealmManager.getData(StaggeredMedicineByItem.class, AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineItem.getId()));
        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> getStoredMedicineItem: " + mMedicineItem.toString());
        return mMedicineItem;
    }

    public static List<StaggeredMedicineByItem> getAllStoredMedicineByItems(Activity activity) {
        RealmManager mRealmManager = RealmManager.with(activity);
        List<StaggeredMedicineByItem> data = mRealmManager.getAllListData(StaggeredMedicineByItem.class);
        return (data != null ? data : new ArrayList<StaggeredMedicineByItem>());
    }

    public static List<StaggeredMedicineByItem> getAllStoredMedicineByItems(Activity activity, FlavorType mFlavorType) {
        RealmManager mRealmManager = RealmManager.with(activity);
        List<StaggeredMedicineByItem> data = null;
        if (mFlavorType != null) {
            if (mFlavorType == FlavorType.SUPPLIER) {
                data = mRealmManager.getAllListData(StaggeredMedicineByItem.class, new HashMap<String, Object>() {{
                    put(AllConstants.TABLE_KEY_MEDICINE_ITEM_USER_TYPE, 1);
                }});
            } else {
                data = mRealmManager.getAllListData(StaggeredMedicineByItem.class, new HashMap<String, Object>() {{
                    put(AllConstants.TABLE_KEY_MEDICINE_ITEM_USER_TYPE, 0);
                }});
            }
            Log.d(AppUtil.class.getSimpleName(), "data>>>StaggeredMedicineByItemStored: " + data.size());
            Log.d(AppUtil.class.getSimpleName(), "toString>>>StaggeredMedicineByItemStored: " + data.toString());
        }
        return (data != null ? data : new ArrayList<StaggeredMedicineByItem>());
    }

    //
    public static boolean isMedicineByItemStored(Activity activity, StaggeredMedicineByItem medicineItem) {
        RealmManager mRealmManager = RealmManager.with(activity);
        boolean isExist = mRealmManager.isDataExist(StaggeredMedicineByItem.class, AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineItem.getId());
        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> isStaggeredMedicineByItemStored: " + isExist);
        return isExist;
    }

    public static boolean isMedicineByItemStored(Activity activity, StaggeredMedicineByItem medicineItem, FlavorType mFlavorType) {
        RealmManager mRealmManager = RealmManager.with(activity);
        boolean isExist = false;
        if (mFlavorType != null) {
            if (mFlavorType == FlavorType.SUPPLIER) {
                isExist = mRealmManager.isDataExist(StaggeredMedicineByItem.class, new HashMap<String, Object>() {{
                    put(AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineItem.getId());
                    put(AllConstants.TABLE_KEY_MEDICINE_ITEM_USER_TYPE, 1);
                }});
            } else {
                isExist = mRealmManager.isDataExist(StaggeredMedicineByItem.class, new HashMap<String, Object>() {{
                    put(AllConstants.TABLE_KEY_MEDICINE_ITEM, medicineItem.getId());
                    put(AllConstants.TABLE_KEY_MEDICINE_ITEM_USER_TYPE, 0);
                }});
            }
        }
        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> isStaggeredMedicineByItemStored: " + isExist);
        return isExist;
    }

    //
//    public static boolean hasStoredStaggeredMedicineByItem(Activity activity) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        boolean isExist = mRealmManager.hasData(StaggeredMedicineByItem.class);
//        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> hasStoredStaggeredMedicineByItem: " + isExist);
//        return isExist;
//    }
//
    public static float getItemTotalPrice(StaggeredMedicineByItem medicineItem) {
        float price = 0.0f, totalPrice = 0.0f;
        if (medicineItem != null) {
            try {
                if (!AllSettingsManager.isNullOrEmpty(medicineItem.getDiscount_percent())) {

                    float discountCost = (((!AllSettingsManager.isNullOrEmpty(medicineItem.getDiscount_percent()) && (Float.parseFloat(medicineItem.getDiscount_percent()) > 0))) ? (Float.parseFloat(medicineItem.getDiscount_percent())) : 0.00f);
                    float sellingCost = (((!AllSettingsManager.isNullOrEmpty(medicineItem.getSelling_price()) && (Float.parseFloat(medicineItem.getSelling_price()) > 0))) ? (Float.parseFloat(medicineItem.getSelling_price())) : 0.00f);
                    price = AppUtil.getTotalPromotionalDiscountPrice(sellingCost, discountCost);
                    Log.e("msellingCost", sellingCost + "sellingCost");
                    Log.e("mdiscountCost", discountCost + "discountCost");
                    Log.e("mPrice", price + "price");

                    totalPrice = medicineItem.getItem_quantity() * price;
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + ">> " + "total(totalPrice): " + totalPrice);

                } else {
                    if (!AllSettingsManager.isNullOrEmpty(medicineItem.getSelling_price())) {
                        price = Float.parseFloat(medicineItem.getSelling_price());
                        totalPrice = medicineItem.getItem_quantity() * price;
                        Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + ">> " + "total(totalPrice): " + totalPrice);

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                totalPrice = 0.0f;
            }
        }
        return totalPrice;
    }

    public static float getSubtotalPrice(Activity activity) {
        float subTotalPrice = 0.0f;
        try {
            List<StaggeredMedicineByItem> foodItems = AppUtil.getAllStoredMedicineByItems(activity);
            for (int i = 0; i < foodItems.size(); i++) {
                StaggeredMedicineByItem mStaggeredMedicineByItem = foodItems.get(i);
                float itemPrice = getItemTotalPrice(mStaggeredMedicineByItem);
                Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + ">> " + "total(itemPrice): " + mStaggeredMedicineByItem.getSelling_price());
                Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + ">> " + "total(itemQuantity): " + mStaggeredMedicineByItem.getItem_quantity());
                Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + ">> " + "total(priceWithQuantity): " + itemPrice);
                subTotalPrice = subTotalPrice + itemPrice;
                Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + ">> " + "total(subTotalPrice): " + subTotalPrice);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return subTotalPrice;
    }

    public static float getCheckoutFromSubtotalPrice(List<StaggeredMedicineByItem> medicineItems) {
        float subTotalPrice = 0.0f;
        try {
            if (medicineItems != null && medicineItems.size() > 0) {
                for (int i = 0; i < medicineItems.size(); i++) {
                    StaggeredMedicineByItem mStaggeredMedicineByItem = medicineItems.get(i);
                    float itemPrice = getItemTotalPrice(mStaggeredMedicineByItem);
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(itemPrice): " + mStaggeredMedicineByItem.getSelling_price());
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(itemQuantity): " + mStaggeredMedicineByItem.getItem_quantity());
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(priceWithQuantity): " + itemPrice);
                    subTotalPrice = subTotalPrice + itemPrice;
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(subTotalPrice): " + subTotalPrice);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return subTotalPrice;
    }

    public static float getStoreOrderFromSubtotalPrice(List<OrderItems> orderItemsList) {
        float subTotalPrice = 0.0f;
        try {
            if (orderItemsList != null && orderItemsList.size() > 0) {
                for (int i = 0; i < orderItemsList.size(); i++) {
                    OrderItems mStaggeredMedicineByItem = orderItemsList.get(i);
                    float itemPrice = getStoreOrderItemTotalPrice(mStaggeredMedicineByItem);
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "mStaggeredMedicineByItem: " + mStaggeredMedicineByItem.toString());
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(itemPrice): " + mStaggeredMedicineByItem.getPrices());
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(itemQuantity): " + mStaggeredMedicineByItem.getQuantitys());
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(priceWithQuantity): " + itemPrice);
                    subTotalPrice = subTotalPrice + itemPrice;
                    Log.d(AppUtil.class.getSimpleName(), AppUtil.class.getSimpleName() + "<<>> " + "total(subTotalPrice): " + subTotalPrice);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return subTotalPrice;
    }


    public static float getStoreOrderItemTotalPrice(OrderItems orderItems) {
        float price = 0.0f, totalPrice = 0.0f;
        if (orderItems != null) {
            try {
                if (!AllSettingsManager.isNullOrEmpty(orderItems.getPrices())) {
                    price = Float.parseFloat(orderItems.getPrices());
                    totalPrice = Integer.parseInt(orderItems.getQuantitys()) * price;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                totalPrice = 0.0f;
            }
        }
        return totalPrice;
    }


    public static float getPromotionalDiscountPrice(float subtotal, float promotionalDiscount) {
        float mDiscountPrice = 0.0f;
        try {
            if (promotionalDiscount > 0) {
                mDiscountPrice = ((subtotal * promotionalDiscount) / 100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mDiscountPrice;
    }

    public static float getTotalPrice(float subtotal, float promotionalDiscount, float shippingCost) {
        float tempSubTotalPrice = subtotal - getPromotionalDiscountPrice(subtotal, promotionalDiscount);
//        if (promotionalDiscount > 0) {
//            float discount = ((subtotal * promotionalDiscount) / 100);
//            tempSubTotalPrice = subtotal - discount;
//        } else {
//            tempSubTotalPrice = subtotal;
//        }

        return tempSubTotalPrice + shippingCost;
    }

    public static float formatFloat(float value) {
        float twoDigitsFloat = 0.0f;
        try {
//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            twoDigitsFloat = Float.valueOf(decimalFormat.format(value));
            twoDigitsFloat = Float.parseFloat(String.format("%.2f", value));
        } catch (Exception ex) {
            twoDigitsFloat = 0.0f;
            ex.printStackTrace();
        }
        return twoDigitsFloat;
    }

    public static float getTotalPromotionalDiscountPrice(float subtotal, float promotionalDiscount) {
        float mTotalPrice = 0.0f;
        try {
            if (promotionalDiscount > 0) {
                float mDiscountPrice = ((subtotal * promotionalDiscount) / 100);
                Log.e("mDiscountPrice", mDiscountPrice + "mTotalPrice");

                mTotalPrice = subtotal - mDiscountPrice;
                Log.e("mTotalPrice", mTotalPrice + "mTotalPrice");

            } else {
                mTotalPrice = subtotal;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mTotalPrice;
    }

    public static String formatDoubleString(float value) {
        String twoDigitsDouble = "0.00";
        try {
//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            twoDigitsFloat = Float.valueOf(decimalFormat.format(value));
            double doubleValue = (double) value;
            twoDigitsDouble = String.format("%.2f", doubleValue);

        } catch (Exception ex) {
            twoDigitsDouble = "0.00";
            ex.printStackTrace();
        }
        return twoDigitsDouble;
    }

    public static boolean isDebug(Context context) {
//        return BuildConfig.DEBUG;
        return ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }

    //    public static int getHomeBanner(Context context) {
//        TypedArray images = context.getResources().obtainTypedArray(R.array.home_banner);
//        int image = images.getResourceId(RandomManager.getRandom(1, 10), -1);
//        images.recycle();
//        return image;
//    }
    public static String optStringNullCheckValue(final String myString) {
        if (myString == null || myString.equalsIgnoreCase("") || myString.equalsIgnoreCase("null"))
            return "";
        else
            return myString;
    }

    public static void showPictureDialog(final Context context, final View.OnClickListener onClickListenerCamera, final View.OnClickListener onClickListenerGallery) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.dialog_picture_attachment, null);
        builder.setView(dialogLayout);
        builder.setCancelable(true);
        Button btnCamera = (Button) dialogLayout.findViewById(R.id.dialog_add_attachment_camera_button);
        Button btnGallary = (Button) dialogLayout.findViewById(R.id.dialog_add_attachment_gallery_button);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureDialog.dismiss();
                onClickListenerCamera.onClick(dialogLayout);

            }
        });

        btnGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureDialog.dismiss();
                onClickListenerGallery.onClick(dialogLayout);

            }
        });
        pictureDialog = builder.create();
        pictureDialog.show();

    }

    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    private static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getAppDeviceUniqueIdentifier(Context context) {
        String deviceUniqueIdentifier = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            SessionManager.setStringSetting(context, AllConstants.SESSION_DEVICE_IDENTIFIER, deviceUniqueIdentifier);

        }
        Logger.d(TAG, TAG + " >>> " + "AppUser(deviceUniqueIdentifier): " + deviceUniqueIdentifier);
        return deviceUniqueIdentifier;
    }

    public static List<StaggeredMedicineByItem> getFourMedicineByItemsList(Activity activity, List<StaggeredMedicineByItem> medicineByItemList) {
        List<StaggeredMedicineByItem> data = new ArrayList<>();
        if (medicineByItemList != null && medicineByItemList.size() > 0) {
            for (int i = 0; i < medicineByItemList.size(); i++) {
                data.add(medicineByItemList.get(i));
                if (i == 3) {
                    return data;
                }
            }
            return data;
        }
        return (data != null ? data : new ArrayList<StaggeredMedicineByItem>());
    }

    public static List<Category> getFourCategoryList(Activity activity, List<Category> categoryList) {
        List<Category> data = new ArrayList<>();
        if (categoryList != null && categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                data.add(categoryList.get(i));
                if (i == 3) {
                    return data;
                }
            }
            return data;
        }
        return (data != null ? data : new ArrayList<Category>());
    }


    public static List<CommonData> getFourWhyChooseList(Activity activity, List<CommonData> categoryList) {
        List<CommonData> data = new ArrayList<>();
        if (categoryList != null && categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                data.add(categoryList.get(i));
                if (i == 3) {
                    return data;
                }
            }
            return data;
        }
        return (data != null ? data : new ArrayList<CommonData>());
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
            Log.e("ParseException", "ParseException - dateFormat");
        }

        return outputDate;

    }

    public static Uri getLocalBitmapUri(Bitmap bmp, Context context, String title, String description) {
        Uri bmpUri = null;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, title, description);
            bmpUri = Uri.parse(path);
        } catch (Exception ex) {
            Log.e("ParseException", ex.getMessage() + "Exception ");

        }
        return bmpUri;
    }

    public static int getTimeConvertToSecond(String time) {
        int hour, minute, temp = 0;

        if (!TextUtils.isEmpty(time)) {
            try {
//                if (time.trim().length() > 6) {
//                    String sDate[] = time.split(":");
//                    hour = Integer.parseInt(sDate[0].trim());
//                    String minutes = sDate[1].trim();
//                    String substring = minutes.length() > 2 ? minutes.substring(minutes.length() - 2) : minutes;
//                    minute = Integer.parseInt(minutes.substring(0, 2));
//                    temp = (60 * minute) + (3600 * hour);
//
//                    if (substring.equalsIgnoreCase("PM") || (substring.equalsIgnoreCase("pm"))) {
//                      temp = (temp + 43200);
//                    }
//
//                }

                if (time.trim().length() > 6) {
                    String sDate[] = time.split(":");
                    hour = Integer.parseInt(sDate[0].trim());
                    String minutes = sDate[1].trim();
                    String substring = minutes.length() > 2 ? minutes.substring(minutes.length() - 2) : minutes;
                    minute = Integer.parseInt(minutes.substring(0, 2));
                    if (substring.equalsIgnoreCase("PM") || (substring.equalsIgnoreCase("pm"))) {
                        if (hour < 12) {
                            hour = hour + 12;
                        }
                    } else {
                        if (hour == 12) {
                            hour = 0;
                        }
                    }
                } else {
                    hour = Integer.parseInt(time.trim().substring(0, 2));
                    String substring = time.length() > 2 ? time.substring(time.length() - 2) : time;

                    if (substring.equalsIgnoreCase("PM") || (substring.equalsIgnoreCase("pm"))) {
                        hour = hour + 12;
                    } else {
                        if (hour == 12) {
                            hour = 0;
                        }
                    }
                    minute = 0;
                }
                temp = (60 * minute) + (3600 * hour);
                Log.e("temp>>>>", temp + "temp ");


            } catch (Exception ex) {
                Log.e("ParseException", ex.getMessage() + "Exception ");

            }
        }
        return temp;
    }



    public static List<DayScheduling> getDaySchedulingList(Context context) {
        List<DayScheduling> data = new ArrayList<>();
        data.add(new DayScheduling("1", context.getString(R.string.txt_medicine_remainder_day_one)));
        data.add(new DayScheduling("2", context.getString(R.string.txt_medicine_remainder_day_two)));
        data.add(new DayScheduling("3", context.getString(R.string.txt_medicine_remainder_day_three)));
        data.add(new DayScheduling("4", context.getString(R.string.txt_medicine_remainder_day_four)));
        data.add(new DayScheduling("5", context.getString(R.string.txt_medicine_remainder_day_five)));
        data.add(new DayScheduling("6", context.getString(R.string.txt_medicine_remainder_day_six)));
        return data;
    }

    public static List<DayScheduling> getSchedulingList(Context context) {
        List<DayScheduling> data = new ArrayList<>();
        data.add(new DayScheduling("1", context.getString(R.string.txt_medicine_remainder_schedule_type_daily)));
        data.add(new DayScheduling("2", context.getString(R.string.txt_medicine_remainder_schedule_type_weekly)));
        data.add(new DayScheduling("3", context.getString(R.string.txt_medicine_remainder_schedule_type_monthly)));
        return data;
    }
}