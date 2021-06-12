//package com.meembusoft.safewaypharmaonline.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.view.View;
//import android.widget.Button;
//
//import com.meembusoft.safewaypharmaonline.R;
//import com.meembusoft.safewaypharmaonline.base.BaseFragment;
//import com.meembusoft.safewaypharmaonline.model.Store;
//import com.meembusoft.safewaypharmaonline.util.AllConstants;
//import com.meembusoft.safewaypharmaonline.util.Logger;
//
//import org.parceler.Parcels;
//
//
///**
// * @author Md. Rashadul Alam
// * Email: rashed.droid@gmail.com
// */
//public class SupplierDetailsFragment extends BaseFragment {
//
//    private Button btnSkipOrder;
//    //Image slider
//    Store mStore;
//    public static SupplierDetailsFragment newInstance() {
//        SupplierDetailsFragment fragment = new SupplierDetailsFragment();
//        return fragment;
//    }
//
//    @Override
//    public int initFragmentLayout() {
//        return R.layout.activity_supplier_order_details_screen;
//    }
//
//    @Override
//    public void initFragmentBundleData(Bundle bundle) {
//        if (bundle != null) {
//            Parcelable mParcelableStore = bundle.getParcelable(AllConstants.INTENT_KEY_STORE);
//            if (mParcelableStore != null) {
//                mStore = Parcels.unwrap(mParcelableStore);
//                Logger.d(TAG, TAG + " >>> " + "mParcelableStore: " + mStore.toString());
//            }
//        }
//    }
//
//    @Override
//    public void initFragmentViews(View parentView) {
//        btnSkipOrder = (Button) parentView.findViewById(R.id.btn_skip_order);
//
//    }
//
//    @Override
//    public void initFragmentViewsData() {
//        //Set dummy json user
////        AppUser user = new AppUser("1", "Md. Rashadul Alam", "01794620787", "rashed.droid@gmail.com", "Ka-13, South Kuril", "");
////        SessionUtil.setUser(getActivity(),APIResponse.getResponseString(user));
////        ((HomeActivity)getActivity()).initNavigationDrawer();
//
//    }
//
//    @Override
//    public void initFragmentActions() {
//
//        btnSkipOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//
//    }
//
//
//
//        @Override
//    public void initFragmentBackPress() {
//      getActivity().finish();
//    }
//
//    @Override
//    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
//
//    }
//
//    @Override
//    public void initFragmentUpdate(Object object) {
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //Image slider
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
//}