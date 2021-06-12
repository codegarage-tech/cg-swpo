package com.meembusoft.safewaypharmaonline.geocoding;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.meembusoft.safewaypharmaonline.util.Logger;

import java.util.List;
import java.util.Locale;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ReverseGeocoderAddressTask extends AsyncTask<Location, Void, UserLocationAddress> {
    private static String TAG = ReverseGeocoderAddressTask.class.getSimpleName();

    Context mContext;
    LocationAddressListener mLocationAddressListener;
    String streetAddress;


    public ReverseGeocoderAddressTask(Context context, String strAddress, LocationAddressListener locationAddressListener) {
        mContext = context;
        streetAddress = strAddress;
        mLocationAddressListener = locationAddressListener;
    }

    @Override
    protected UserLocationAddress doInBackground(Location... params) {

        try {

            if (params.length > 0) {
                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                Location location = params[0];

                List<Address> addresses = geocoder.getFromLocationName(streetAddress, 1);
                Logger.d(TAG, "addresses: " + addresses.toString());

                if (addresses != null && addresses.size() > 0) {

                    Address address = addresses.get(0);

                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();
                    String addressLine = (address.getMaxAddressLineIndex() >= 0) ? address.getAddressLine(0) : "";
                    String streetAddress = (address.getMaxAddressLineIndex() > 0) ? address.getAddressLine(0) : "___";
                    String city = (address.getLocality().length() > 0) ? address.getLocality() : "___";
                    String state = (address.getAdminArea().length() > 0) ? address.getAdminArea() : "___";
                    String country = (address.getCountryName().length() > 0) ? address.getCountryName() : "___";
                    String countryCode = (address.getCountryCode().length() > 0) ? address.getCountryCode() : "___";
                //    String postalCode = (address.getPostalCode().length() > 0) ? address.getPostalCode() : "___";
                    String knownName = (address.getFeatureName().length() > 0) ? address.getFeatureName() : "___";

                    UserLocationAddress locationAddress = new UserLocationAddress(latitude, longitude, addressLine, streetAddress, city, state, country, countryCode, "", knownName);

                    return locationAddress;
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(UserLocationAddress address) {
        mLocationAddressListener.getLocationAddress(address);

    }
}
