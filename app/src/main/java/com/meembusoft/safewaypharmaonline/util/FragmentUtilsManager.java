package com.meembusoft.safewaypharmaonline.util;

import android.app.Activity;
import android.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.meembusoft.safewaypharmaonline.R;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FragmentUtilsManager {

    public enum TransitionType {
        SlideHorizontal,
        SlideVertical
    }

    public static void changeSupportFragmentWithAnim(AppCompatActivity activity, Fragment fragment, boolean addToBackstack, boolean isAnimation, TransitionType transitionType) {
       androidx.fragment.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        String tag = fragment.getClass().getSimpleName();

        if (addToBackstack) {
            ft.addToBackStack(tag);
        }

        if (isAnimation) {
            int enter = 0, exit = 0, popEnter = 0, popExit = 0;
            switch (transitionType) {
                case SlideHorizontal:
                    enter = R.anim.slide_in_right;
                    exit = R.anim.slide_out_left;
                    popEnter = R.anim.slide_in_left;
                    popExit = R.anim.slide_out_right;
                    break;
                case SlideVertical:
                    enter = R.anim.slide_in_bottom;
                    exit = R.anim.slide_out_bottom;
                    popEnter = R.anim.slide_in_bottom;
                    popExit = R.anim.slide_out_bottom;
                    break;
            }
            ft.setCustomAnimations(enter, exit, popEnter, popExit);
        }

        ft.replace(R.id.container, fragment, tag).commitAllowingStateLoss();
    }

    public static void changeFragment(Activity activity, int containerViewId, android.app.Fragment targetFragment) {
        activity.getFragmentManager()
                .beginTransaction()
                .replace(containerViewId, targetFragment, targetFragment.getClass().getSimpleName())
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static void changeSupportFragment(AppCompatActivity activity, Fragment targetFragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, targetFragment, targetFragment.getClass().getSimpleName())
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static void changeSupportFragment(AppCompatActivity activity, int containerViewId, Fragment targetFragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, targetFragment, targetFragment.getClass().getSimpleName())
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static android.app.Fragment getVisibleFragment(Activity activity, String fragmentTag) {
        android.app.FragmentManager fm = activity.getFragmentManager();
        android.app.Fragment fragment = (android.app.Fragment) fm.findFragmentByTag(fragmentTag);
        return fragment;
    }

    public static android.app.Fragment getVisibleFragment(Activity activity, int containerResID) {
        android.app.FragmentManager fm = activity.getFragmentManager();
        android.app.Fragment fragment = (android.app.Fragment) fm.findFragmentById(containerResID);
        return fragment;
    }

    public static Fragment getVisibleSupportFragment(AppCompatActivity activity, String fragmentTag) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentByTag(fragmentTag);
        return fragment;
    }

    public static Fragment getVisibleSupportFragment(AppCompatActivity activity, int containerResID) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentById(containerResID);
        return fragment;
    }

    public static android.app.Fragment getVisibleViewPagerFragment(Activity activity, ViewPager viewPager) {
        android.app.Fragment fragment = activity.getFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + viewPager.getCurrentItem());
        return fragment;
    }

    public static Fragment getVisibleViewPagerSupportFragment(AppCompatActivity activity, ViewPager viewPager) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + viewPager.getCurrentItem());
        return fragment;
    }
}