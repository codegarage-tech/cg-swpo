package com.meembusoft.safewaypharmaonline.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchDataProvider {

    private static List<String> mInitialSearchQueries = new ArrayList<String>();

    public static List<String> getInitialSearchQueries() {
        return mInitialSearchQueries;
    }

    public static List<String> getSuggestionsForQuery(String query) {
        List<String> pickedSuggestions = new ArrayList<>();

        if (TextUtils.isEmpty(query)) {
            pickedSuggestions.addAll(mInitialSearchQueries);
        } else {
            for (String it : mInitialSearchQueries) {
                if (it.toLowerCase().startsWith(query.toLowerCase())) {
                    pickedSuggestions.add(it);
                }
            }
        }

        return pickedSuggestions;
    }

    public static void saveSearchQuery(String searchQuery) {
        mInitialSearchQueries.remove(searchQuery);
        mInitialSearchQueries.add(0, searchQuery);
    }

    public static void removeSearchQuery(String searchQuery) {
        mInitialSearchQueries.remove(searchQuery);
    }
}
