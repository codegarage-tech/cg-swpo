package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.model.StaggeredListItem;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.viewholder.StaggeredListViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static com.meembusoft.safewaypharmaonline.util.AppUtil.chunkList;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StaggeredListAdapter extends RecyclerArrayAdapter<StaggeredListItem> {

    private String TAG = StaggeredListAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_REGULAR = 1;
    Context mContext;
    CategoryType mMedicineType;
    FlavorType mFlavorType;

    public StaggeredListAdapter(Context context,CategoryType medicineType, FlavorType flavorType) {
        super(context);
        mContext = context;
        mMedicineType = medicineType;
        mFlavorType = flavorType;
        Log.d(TAG, "mMedicineType: " + mMedicineType.toString());
        Log.d(TAG, "mFlavorType<<<: " + mFlavorType.toString());

    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new StaggeredListViewHolder(parent, mContext,mMedicineType,mFlavorType);
            default:
                throw new InvalidParameterException();
        }
    }

    public void setAllData(List<StaggeredMedicineByItem> staggeredItems, int chunkSize) {
        if (staggeredItems != null && staggeredItems.size() > 0) {
            List<List<StaggeredMedicineByItem>> chunkList = chunkList(staggeredItems, chunkSize);
            Log.d(TAG, "Chuncked list size: " + chunkList.size());

            for (List<StaggeredMedicineByItem> itemList : chunkList) {
                add(new StaggeredListItem(itemList));
            }
            notifyDataSetChanged();
        }
    }

    public void addData(List<StaggeredMedicineByItem> staggeredItems, int chunkSize) {
        if (staggeredItems != null && staggeredItems.size() > 0) {

            List<StaggeredListItem> totalExistingItems = getAllData();
            if (totalExistingItems != null) {
                List<StaggeredMedicineByItem> tempItems = new ArrayList<>();
                for (StaggeredListItem items : totalExistingItems) {
                    tempItems.addAll(items.getItems());
                }
                tempItems.addAll(staggeredItems);
                Log.d(TAG, "Total data: " + tempItems.size());

                clear();
                notifyDataSetChanged();
                setAllData(tempItems, chunkSize);
            }

//            List<StaggeredListItem> totalExistingItems = getAllData();
//            int totalExistingItemsCount = totalExistingItems.size();
//            if (totalExistingItemsCount > 0) {
//                Log.d(TAG, "The list already has some data");
//
//                int totalItem = 0;
//                for (StaggeredListItem items : totalExistingItems) {
//                    totalItem += items.getItems().size();
//                }
//                Log.d(TAG, "The list has total data: " + totalExistingItemsCount);
//                Log.d(TAG, "The list has total items: " + totalItem);
//
//                int neededItems = chunkSize - (totalItem % chunkSize);
//                if (neededItems == chunkSize || neededItems == 0) {
//                    Log.d(TAG, "There are no needed items");
//                    Log.d(TAG, "Adding as new items");
//                    setAllData(staggeredItems, chunkSize);
//                } else {
//                    Log.d(TAG, "There are needed items: " + neededItems);
//
//                    Log.d(TAG, "Total new items: " + staggeredItems.size());
//                    int reviewedAddCount = 0;
//                    if (staggeredItems.size() >= neededItems) {
//                        Log.d(TAG, "New items are greater than needed items");
//                        reviewedAddCount = neededItems;
//
//                        Log.d(TAG, "reviewedAddCount: " + reviewedAddCount);
//                        StaggeredListItem lastItem = totalExistingItems.get(totalExistingItemsCount - 1);
//                        Log.d(TAG, "existing item is: " + lastItem.toString());
//                        for (int i = 0; i < reviewedAddCount; i++) {
//                            Log.d(TAG, "adding: " + staggeredItems.get(i).getName());
//                            lastItem.getItems().add(staggeredItems.get(i));
//                        }
//                        Log.d(TAG, "After adding data is: " + lastItem.toString());
//
//                        Log.d(TAG, "Calculating remaining items");
//                        List<StaggeredItem> remainingItems = new ArrayList<>();
//                        for (int i = reviewedAddCount; i < staggeredItems.size(); i++) {
//                            remainingItems.add(staggeredItems.get(i));
//                        }
//
//                        int remainingDataCount = remainingItems.size();
//                        Log.d(TAG, "Remaining data need to be added: " + remainingDataCount);
//                        if (remainingDataCount > 0) {
//                            Log.d(TAG, "Adding as remaining input");
////                            addData(remainingItems, chunkSize);
//                        }
//                    } else {
//                        Log.d(TAG, "New items are not greater than needed items");
//                        reviewedAddCount = staggeredItems.size();
//
//                        Log.d(TAG, "reviewedAddCount: " + reviewedAddCount);
//                        StaggeredListItem lastItem = totalExistingItems.get(totalExistingItemsCount - 1);
//                        for (int i = 0; i < reviewedAddCount; i++) {
//                            lastItem.getItems().add(staggeredItems.get(i));
//                        }
//                    }
//                }
//            } else {
//                Log.d(TAG, "Adding as fresh input");
//                setAllData(staggeredItems, chunkSize);
//            }
        }
    }

    public void addData(StaggeredMedicineByItem staggeredItems) {
//        if (staggeredItems != null && staggeredItems.size() > 0) {
//            List<List<StaggeredItem>> chunkList = chunkList(staggeredItems, 6);
//            Log.d(TAG, "Chuncked list size: " + chunkList.size());
//
//            for (List<StaggeredItem> itemList : chunkList) {
//                add(new StaggeredListItem(itemList));
//            }
//            notifyDataSetChanged();
//        }
    }
}