package com.meembusoft.safewaypharmaonline.util;

import android.util.Log;

import com.meembusoft.safewaypharmaonline.model.Category;

import java.util.List;

public class DataUtil {
    private static String TAG = DataUtil.class.getSimpleName();


    public static String getCategoryID(List<Category> categoryList, String categoryName) {
        if (categoryList!=null && categoryList.size()>0) {
            for (int i = 0; i < categoryList.size(); i++) {
                Log.e( "categoryName", categoryList.get( i ).getName()  + "" );
                if (categoryName.contains( categoryList.get( i ).getName() )) {
                    Log.e( "categoryName>>>>", categoryList.get( i ).getName() + "" );
                    return categoryList.get( i ).getId();
                }
            }
        }
        return "";
    }



    public static final String BANNER_LIST = "{\n" +
            "\t\"status\": \"1\",\n" +
            "\t\"message\": \"Response successful\",\n" +
            "\t\"data\": [{\n" +
            "\t\t\t\"id\": 61,\n" +
            "\t\t\t\"user_id\": \"9\",\n" +
            "\t\t\t\"name\": \"Single 2 Pin Socket\",\n" +
            "\t\t\t\"image\": \"https://d2hpkjovixmy47.cloudfront.net/697-large_default/porcelain-single-2-pin-socket-white.jpg\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"id\": 63,\n" +
            "\t\t\t\"user_id\": \"9\",\n" +
            "\t\t\t\"name\": \"Single 2 Pin Socket\",\n" +
            "\t\t\t\"image\": \"https://images-na.ssl-images-amazon.com/images/I/41osJl4rz9L.jpg\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"id\": 63,\n" +
            "\t\t\t\"user_id\": \"9\",\n" +
            "\t\t\t\"name\": \"Single 2 Pin Socket\",\n" +
            "\t\t\t\"image\": \"https://images-na.ssl-images-amazon.com/images/I/41osJl4rz9L.jpg\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"id\": 65,\n" +
            "\t\t\t\"user_id\": \"9\",\n" +
            "\t\t\t\"name\": \"Single 2 Pin Socket\",\n" +
            "\t\t\t\"image\": \"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQv0M8kTPLP5oJ4y2A0KhSrdGMoJ-iAtkhZtCsHrnsv26lhKQ8T\"\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}";


}
