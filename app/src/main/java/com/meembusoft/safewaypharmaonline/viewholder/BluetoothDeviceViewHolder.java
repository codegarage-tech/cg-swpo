package com.meembusoft.safewaypharmaonline.viewholder;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import com.meembusoft.safewaypharmaonline.R;



public class BluetoothDeviceViewHolder extends BaseViewHolder<BluetoothDevice> {

    private String TAG = BluetoothDeviceViewHolder.class.getSimpleName();
    private TextView tvDeviceName, tvDeviceAddress, tvDeviceStatus;
    private ImageView ivUnpair;
    //
    private static final int MAX_NAME_CHECKS = 3;
    private static final int NAME_CHECK_PERIOD = 1000;
    private int nameChecks;
    Activity mActivity;

//
//    private String lastSent = "";
//    private BTSendReceiveThread btsendrec;

    public BluetoothDeviceViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_bluetooth_device);
        mActivity = ((Activity) getContext());

        tvDeviceName = $(R.id.tv_bluetooth_name);
        tvDeviceAddress = $(R.id.tv_bluetooth_address);
    }

    @Override
    public void setData(final BluetoothDevice data) {

       //tvDeviceName.setText(data.getName());
        resolveName(data, tvDeviceName, getContext());
        tvDeviceAddress.setText(data.getAddress());



    }

    /**
     * Checks for the device name, for a maximum of {@link BluetoothDeviceViewHolder#MAX_NAME_CHECKS}
     * as the name may not have been resolved at binding.
     */
    private void resolveName(final BluetoothDevice device, final TextView deviceName, final Context context) {
        if (device != null) {
            String name = device.getName();
            boolean isEmptyName = TextUtils.isEmpty(name);

            if (isEmptyName) deviceName.setText(R.string.txt_unknown_device);
            else deviceName.setText(name);

            // Check later if device name is resolved
            if (nameChecks++ < MAX_NAME_CHECKS && isEmptyName)
                itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resolveName(device, deviceName, context);
                    }
                }, NAME_CHECK_PERIOD);
        }
    }

//    private void connectBluetooth(BluetoothDevice data, Context context) {
//        Logger.d(TAG, "btnConnect>>setOnClickListener>> data: " + data.toString());
//        BluetoothHandler.getInstance().startBluetoothConnectionTask(context, data, new BaseUpdateListener() {
//            @Override
//            public void onUpdate(Object... update) {
//                Object data = update[0];
//                if (data != null) {
//                    boolean isConnected = (boolean) data;
//                    if (isConnected) {
//                        BTSendReceiveThread btsendrec = new BTSendReceiveThread(BluetoothHandler.getInstance().getBluetoothSocket(), BluetoothHandler.getInstance().mHandler);
//                        btsendrec.start();
//                    }
//                }
//            }
//        });
//    }
}