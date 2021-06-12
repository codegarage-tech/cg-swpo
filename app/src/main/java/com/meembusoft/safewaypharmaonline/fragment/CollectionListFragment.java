package com.meembusoft.safewaypharmaonline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseFragment;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CollectionListFragment extends BaseFragment {

    //Image slider

    public static CollectionListFragment newInstance() {
        CollectionListFragment fragment = new CollectionListFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.activity_collection_screen;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {

//        parentView.findViewById(R.id.lin_login_next).setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                FragmentUtilsManager.changeSupportFragment(((HomeActivity)getActivity()), new RegistrationFragment());
//            }
//        });
    }

    @Override
    public void initFragmentViewsData() {
        //Set dummy json user
//        AppUser user = new AppUser("1", "Md. Rashadul Alam", "01794620787", "rashed.droid@gmail.com", "Ka-13, South Kuril", "");
//        SessionUtil.setUser(getActivity(),APIResponse.getResponseString(user));
//        ((HomeActivity)getActivity()).initNavigationDrawer();

    }

    @Override
    public void initFragmentActions() {

    }


    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Image slider
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}