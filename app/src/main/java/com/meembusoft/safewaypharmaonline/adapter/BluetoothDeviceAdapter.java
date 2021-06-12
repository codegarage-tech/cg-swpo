package com.meembusoft.safewaypharmaonline.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.viewholder.BluetoothDeviceViewHolder;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BluetoothDeviceAdapter extends RecyclerArrayAdapter<BluetoothDevice> {

    private static final int VIEW_TYPE_REGULAR = 1;
    Context mContext;

    public BluetoothDeviceAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new BluetoothDeviceViewHolder(parent, mContext);
            default:
                throw new InvalidParameterException();
        }
    }

    public void addDevice(BluetoothDevice bluetoothDevice) {
        // Avoid duplicate input
        List<BluetoothDevice> bluetoothDevices = getAllData();
        for (BluetoothDevice mBluetoothDevice : bluetoothDevices) {
            if (mBluetoothDevice.getAddress().equalsIgnoreCase(bluetoothDevice.getAddress())) {
                return;
            }
        }
        add(bluetoothDevice);
        notifyDataSetChanged();
    }

    public void removeItem(BluetoothDevice device) {
        int position = getItemPosition(device);
        if (position != -1) {
            remove(position);
            notifyDataSetChanged();
        }
    }

    private int getItemPosition(BluetoothDevice device) {
        List<BluetoothDevice> devices = getAllData();
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getAddress().equalsIgnoreCase(device.getAddress())) {
                return i;
            }
        }
        return -1;
    }
}