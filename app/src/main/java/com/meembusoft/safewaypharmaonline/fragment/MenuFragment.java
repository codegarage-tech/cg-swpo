package com.meembusoft.safewaypharmaonline.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity2;
import com.meembusoft.safewaypharmaonline.activity.EmergencyHelpActivity;
import com.meembusoft.safewaypharmaonline.activity.FavouriteListActivity;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.activity.LoginActivity;
import com.meembusoft.safewaypharmaonline.activity.MedicineRemainderActivity;
import com.meembusoft.safewaypharmaonline.activity.PlaceOrderViewActivity;
import com.meembusoft.safewaypharmaonline.activity.PrescriptionListActivity;
import com.meembusoft.safewaypharmaonline.activity.UserProfileActivity;
import com.meembusoft.safewaypharmaonline.base.BaseFragment;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.enumeration.ShippingType;
import com.meembusoft.safewaypharmaonline.medicinereminder.service.ForeverRunningService;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.Home;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.SystemSetting;
import com.meembusoft.safewaypharmaonline.model.UserProfile;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.FragmentUtilsManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.attributionpresenter.activity.LicenseActivity;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.controller.DialogControllable;
import cn.ymex.popup.dialog.PopupDialog;

import static android.app.Activity.RESULT_OK;
import static com.meembusoft.safewaypharmaonline.activity.HomeActivity.ID_HOME;
import static com.meembusoft.safewaypharmaonline.activity.HomeActivity.ID_NOTIFICATION;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_SHIPPING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;
import static com.meembusoft.safewaypharmaonline.util.SessionUtil.SESSION_KEY_USER_TAG;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MenuFragment extends BaseFragment {

    private AppUser mAppUser;
    private UserProfile mUserProfile;

    //Menu row
    private LinearLayout linMenuHome, linMenuCategory, linMenuAccount,linMenuProfile, linMenuOrder, linMenuFavourite, linMenuNotifyMgs, linMenuNotification,
            linMenuMessage, linMenuHotline, linMenuEmergencyHelp, linMenuPrescription, linMenuLicense, linMenuMrpLogOut,linMenuMedicineRemainder,linMenuTermsAndConditions,linMenuLogOut;
    private View viewNotifyMgs;
    private SystemSetting systemSetting;
    private ShippingAddress mShippingAddress;

    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.layout_menu_screen;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        linMenuHome = (LinearLayout) parentView.findViewById(R.id.lin_menu_home);
        linMenuCategory = (LinearLayout) parentView.findViewById(R.id.lin_menu_category);
        linMenuAccount = (LinearLayout) parentView.findViewById(R.id.lin_menu_account);
        linMenuProfile= (LinearLayout) parentView.findViewById(R.id.lin_menu_profile);
        linMenuOrder = (LinearLayout) parentView.findViewById(R.id.lin_menu_order);
        linMenuFavourite = (LinearLayout) parentView.findViewById(R.id.lin_menu_favourite);
        linMenuNotifyMgs = (LinearLayout) parentView.findViewById(R.id.lin_menu_notify_mgs);
        linMenuNotification = (LinearLayout) parentView.findViewById(R.id.lin_menu_notification);
        linMenuMessage = (LinearLayout) parentView.findViewById(R.id.lin_menu_message);
        linMenuHotline = (LinearLayout) parentView.findViewById(R.id.lin_menu_hotline);
        linMenuEmergencyHelp = (LinearLayout) parentView.findViewById(R.id.lin_menu_emergency_help);
        linMenuPrescription = (LinearLayout) parentView.findViewById(R.id.lin_menu_prescription);
        linMenuLicense = (LinearLayout) parentView.findViewById(R.id.lin_menu_license);
        linMenuTermsAndConditions = (LinearLayout) parentView.findViewById(R.id.lin_menu_terms_and_conditions);
        linMenuMrpLogOut = (LinearLayout) parentView.findViewById(R.id.lin_menu_mrp_log_out);
        linMenuMedicineRemainder = (LinearLayout) parentView.findViewById(R.id.lin_menu_medicine_remainder);
        linMenuLogOut = (LinearLayout) parentView.findViewById(R.id.lin_menu_log_out);

        mUserProfile = SessionUtil.getUserProfile(getActivity());
        // Get User Tag
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.CUSTOMER;
        }
    }

    @Override
    public void initFragmentViewsData() {
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME))) {
            String offlineHomeData = SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME);
            Logger.d(TAG, "HomeData(Session): " + offlineHomeData);
            Home responseHOme = APIResponse.getResponseObject(offlineHomeData, Home.class);
            systemSetting = responseHOme.getSystem_setting();
        }


        if (mFlavorType != null) {
            switch (mFlavorType) {
                case CUSTOMER:
                    mAppUser = SessionUtil.getUser(getActivity());
                    updateMenu();
                    break;

                case SUPPLIER:
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    linMenuLogOut.setVisibility(View.VISIBLE);
                    linMenuProfile.setVisibility(View.VISIBLE);
                    linMenuOrder.setVisibility(View.VISIBLE);
                    linMenuNotifyMgs.setVisibility(View.VISIBLE);
                    linMenuFavourite.setVisibility(View.GONE);

                    if (mAppSupplier != null && mAppSupplier.getUser_id() != null) {
                        linMenuAccount.setVisibility(View.GONE);
                        linMenuMrpLogOut.setVisibility(View.GONE);

                    }
                    break;

            }

        }



    }

    @Override
    public void initFragmentActions() {
        linMenuHome.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                FragmentUtilsManager.changeSupportFragment((HomeActivity) getActivity(), new HomeFragment());
                ((HomeActivity) getActivity()).getBottomNavigation().show(ID_HOME, true);
            }
        });
        linMenuCategory.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Toast.makeText(getActivity(), getResources().getString(R.string.toast_order_under_development), Toast.LENGTH_SHORT).show();
                Intent intentCategory = new Intent(getActivity(), CategoryActivity2.class);
                getActivity().startActivity(intentCategory);
            }
        });

        linMenuOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentMyOrder = new Intent(getActivity(), PlaceOrderViewActivity.class);
                startActivity(intentMyOrder);
//
            }
        });

        linMenuAccount.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                Intent iLoginActivity = new Intent(getActivity(), LoginActivity.class);
                iLoginActivity.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.MENU_TO_LOGIN.name());
                getActivity().startActivityForResult(iLoginActivity, INTENT_REQUEST_CODE_USER_LOGIN);

            }
        });

        linMenuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iUserProfile = new Intent(getActivity(), UserProfileActivity.class);
                iUserProfile.putExtra(SESSION_KEY_USER_TAG, mFlavorType.name());
                startActivity(iUserProfile);
            }
        });

        linMenuNotification.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ((HomeActivity) getActivity()).profileIconInvisible();
                FragmentUtilsManager.changeSupportFragment((HomeActivity) getActivity(), new NotificationFragment());
                ((HomeActivity) getActivity()).getBottomNavigation().show(ID_NOTIFICATION, true);
            }
        });

        linMenuMessage.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                Intent iSupplierActivity = new Intent(getActivity(), SupplierStoreDetailActivity.class);
//                getActivity().startActivity(iSupplierActivity);
            }
        });

        linMenuFavourite.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iFavouriteActivity = new Intent(getActivity(), FavouriteListActivity.class);
                getActivity().startActivity(iFavouriteActivity);
            }
        });
        linMenuMedicineRemainder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iRemainderActivity = new Intent(getActivity(), MedicineRemainderActivity.class);
                getActivity().startActivity(iRemainderActivity);
            }
        });

        linMenuPrescription.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iPrescriptionActivity = new Intent(getActivity(), PrescriptionListActivity.class);
                getActivity().startActivity(iPrescriptionActivity);
            }
        });


        linMenuEmergencyHelp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iEmergencyHelp = new Intent(getActivity(), EmergencyHelpActivity.class);
                getActivity().startActivity(iEmergencyHelp);
            }
        });

        linMenuHotline.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (systemSetting != null) {
                    onHotLineClick();
                }
            }
        });

        linMenuLicense.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentLicense = new Intent(getActivity(), LicenseActivity.class);
                startActivity(intentLicense);
            }
        });

        linMenuTermsAndConditions.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_and_condition_url)));
                startActivity(browserIntent);
            }
        });

        linMenuLogOut.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                logOutDialog(getResources().getString(R.string.dialog_logout_title), getResources().getString(R.string.dialog_logout_mgs));
            }
        });
    }

    @Override
    public void initFragmentBackPress() {

    }

    private void updateMenu() {
        if (mAppUser != null) {
            ((HomeActivity) getActivity()).logOutView(mAppUser,mUserProfile);
            linMenuAccount.setVisibility(View.GONE);
            linMenuProfile.setVisibility(View.VISIBLE);
            linMenuOrder.setVisibility(View.VISIBLE);
            linMenuFavourite.setVisibility(View.VISIBLE);
            linMenuNotifyMgs.setVisibility(View.VISIBLE);
            linMenuMrpLogOut.setVisibility(View.VISIBLE);

        } else {
           ((HomeActivity) getActivity()).logOutView(null,null);
            linMenuAccount.setVisibility(View.VISIBLE);
            linMenuProfile.setVisibility(View.GONE);
            linMenuOrder.setVisibility(View.GONE);
            linMenuFavourite.setVisibility(View.GONE);
            linMenuNotifyMgs.setVisibility(View.GONE);
            linMenuMrpLogOut.setVisibility(View.GONE);


        }
    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case INTENT_REQUEST_CODE_USER_LOGIN:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_LOGIN: " + requestCode);
                LoginSettingType loginSettingType;
                if (data != null && resultCode == RESULT_OK) {

                    if (SessionUtil.getUser(getActivity()) != null) {
                        mAppUser = SessionUtil.getUser(getActivity());
                        ((HomeActivity) getActivity()).initGetTagValue();
                        updateMenu();

                    }
                }
        }
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

    /**
     * Log out
     */
    public void logOutDialog(String title, String message) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(title + "\n")
                        .message(message)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mFlavorType == FlavorType.SUPPLIER){
                                    SessionUtil.setSupplier(getActivity(), "");
                                    SessionManager.setStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG, FlavorType.CUSTOMER.toString());
                                    linMenuLogOut.setVisibility(View.GONE);
                                    linMenuProfile.setVisibility(View.GONE);
                                    linMenuOrder.setVisibility(View.GONE);
                                    linMenuNotifyMgs.setVisibility(View.GONE);
                                    linMenuAccount.setVisibility(View.VISIBLE);
                                    linMenuFavourite.setVisibility(View.GONE);
                                    ((HomeActivity) getActivity()).initGetTagValue();
                                    ((HomeActivity) getActivity()).resetCounterView();
                                    ((HomeActivity) getActivity()).logOutView(null,null);

                                } else if(mFlavorType == FlavorType.CUSTOMER){
                                    SessionUtil.setUser(getActivity(), "null");
                                    SessionUtil.setUserProfile(getActivity(), "null");
                                    mAppUser = SessionUtil.getUser(getActivity());
                                    mUserProfile = SessionUtil.getUserProfile(getActivity());
                                    updateMenu();

                                    //Stop background service
//                                    getActivity().stopService(new Intent(getActivity(), ForeverRunningService.class));
                                }
                            }
                        }))
                .show();
    }

    public void onHotLineClick() {
        PopupDialog.create(getActivity())
                .animationIn(R.anim.push_in)
                .animationOut(R.anim.push_out)
                .controller(new CustomDialogController()).show();
    }

    class CustomDialogController implements DialogControllable {
        @Override
        public View createView(Context context, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.dialog_view_hotline, parent, false);
        }

        @Override
        public PopupDialog.OnBindViewListener bindView() {
            return new PopupDialog.OnBindViewListener() {
                @Override
                public void onCreated(final PopupDialog dialog, final View layout) {

                    dialog.backPressedHide(true);
                    dialog.outsideTouchHide(false);

                    dialog.backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
                    TextView tvSiteName = (TextView) layout.findViewById(R.id.tv_sitename);
                    TextView tvAddress = (TextView) layout.findViewById(R.id.tv_address);
                    TextView tvEmail = (TextView) layout.findViewById(R.id.tv_email);
                    LinearLayout linContact = (LinearLayout) layout.findViewById(R.id.lin_safeway_contact);

                    tvSiteName.setText(AppUtil.optStringNullCheckValue(systemSetting.getSitename()));
                    tvAddress.setText(AppUtil.optStringNullCheckValue(systemSetting.getAddress()));
                    tvEmail.setText(AppUtil.optStringNullCheckValue(systemSetting.getEmail()));

                    tvEmail.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/html");
                                intent.putExtra(Intent.EXTRA_EMAIL, AppUtil.optStringNullCheckValue(systemSetting.getEmail()));
                                intent.putExtra(Intent.EXTRA_SUBJECT, AppUtil.optStringNullCheckValue(systemSetting.getSitename()));
                                intent.putExtra(Intent.EXTRA_TEXT, "");

                                startActivity(Intent.createChooser(intent, "Send Email"));
                            } catch (ActivityNotFoundException e) {
                                //TODO: Handle case where no email app is available
                            }
                        }
                    });

                    linContact.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + AppUtil.optStringNullCheckValue(systemSetting.getHotline())));
                                startActivity(intent);
                            } catch (Exception ex) {

                            }
                        }
                    });
                    layout.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            };
        }
    }
}