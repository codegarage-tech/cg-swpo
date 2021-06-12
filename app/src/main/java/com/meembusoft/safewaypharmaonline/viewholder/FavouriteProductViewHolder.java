package com.meembusoft.safewaypharmaonline.viewholder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.FavouriteListActivity;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.adapter.FavouriteListAdapter;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsFavourite;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FavouriteProductViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = FavouriteProductViewHolder.class.getSimpleName();
    private ImageView ivProduct;
    private TextView tvName,tvSupplierName,tvGenericName,tvAmount,tvQty,tvStrikeAmount;
    private LinearLayout linFavourite;
    private GetRemoveFavouriteListTask getRemoveFavouriteListTask;

    public FavouriteProductViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_my_favourite_item);
        tvName = (TextView) $(R.id.tv_name);
        tvSupplierName = (TextView) $(R.id.tv_supplier_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvStrikeAmount = (TextView) $(R.id.tv_strike_amount);
        ivProduct = (ImageView) $(R.id.iv_product);
        linFavourite = (LinearLayout) $(R.id.lin_favourite);
    }

    @Override
    public void setData(final StaggeredMedicineByItem data) {
        //Set data
        tvName.setText(AppUtil.optStringNullCheckValue(data.getName()) + " " +AppUtil.optStringNullCheckValue(data.getForm_name()));
        tvSupplierName.setText(AppUtil.optStringNullCheckValue(data.getSupplier_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(data.getGeneric_name()));

        // Discount Calculation
        float discountPercent = (((!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && (Float.parseFloat(data.getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getDiscount_percent())) :  0.00f);

        if (!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && discountPercent > 0) {
            tvStrikeAmount.setVisibility(View.VISIBLE);
            try {
                float discountCost = (((!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && (Float.parseFloat(data.getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getDiscount_percent())) : 0.00f);
                float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(data.getSelling_price()) && (Float.parseFloat(data.getSelling_price()) > 0))) ? (Float.parseFloat(data.getSelling_price())) : 0.00f);
                float minusSubtotalToDiscountCost = AppUtil.getTotalPromotionalDiscountPrice(originSellingPrice, discountCost);
                Log.e("minusToDiscountCost", minusSubtotalToDiscountCost + "llMinus");
                String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                tvAmount.setText(getContext().getString(R.string.title_tk)+" "+ finalSubtotalToDiscountCost);
                tvStrikeAmount.setText(getContext().getString(R.string.title_tk)+"  " + AppUtil.formatDoubleString(originSellingPrice));
                tvStrikeAmount.setPaintFlags(tvStrikeAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        } else {
            tvStrikeAmount.setVisibility(View.GONE);
            tvAmount.setText(getContext().getString(R.string.title_tk)+" "+AppUtil.optStringNullCheckValue(data.getSelling_price()));
        }


        AppUtil.loadImage(getContext(), ivProduct, data.getProduct_image(), false, false, true);

        linFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUser mAppUser = SessionUtil.getUser(getContext());
                if (mAppUser!=null) {
                    if (getRemoveFavouriteListTask != null && getRemoveFavouriteListTask.getStatus() == AsyncTask.Status.RUNNING) {
                        getRemoveFavouriteListTask.cancel(true);
                    }
                    ParamsFavourite paramsFavourite = new ParamsFavourite(data.getId(),mAppUser.getId() );
                    getRemoveFavouriteListTask = new GetRemoveFavouriteListTask(getContext(), paramsFavourite);
                    getRemoveFavouriteListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data));
                getContext().startActivity(intentProductDetails);
            }
        });
    }


    /************************
     * Server communication *
     ************************/
    private class GetRemoveFavouriteListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsFavourite mParamsFavourite;

        private GetRemoveFavouriteListTask(Context context,ParamsFavourite paramsFavourite) {
            mContext = context;
            mParamsFavourite = paramsFavourite ;
        }

        @Override
        protected void onPreExecute() {
            ((FavouriteListActivity) getContext()).showPopupDialog();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                APIInterface mApiInterface = APIClient.getClient(getContext()).create(APIInterface.class);

                Call<APIResponse> call = mApiInterface.apiGetAllFavouriteRemoveList(mParamsFavourite.getCustomer_id(),mParamsFavourite.getProduct_id());
                Response response = call.execute();
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                ((FavouriteListActivity) getContext()).dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetRemoveFavouriteListTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("GetRemoveFavouriteListTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetRemoveFavouriteListTask()): onResponse-object = " + data.toString());
                        //Remove from list
                        ((FavouriteListAdapter)getOwnerAdapter()).remove(getAdapterPosition());
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_your_remove_favourite_items), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();

                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}