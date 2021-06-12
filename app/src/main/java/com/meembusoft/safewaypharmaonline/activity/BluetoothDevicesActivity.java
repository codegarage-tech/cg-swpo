package com.meembusoft.safewaypharmaonline.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.BluetoothDeviceAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BluetoothDevicesActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack, ivRightMenu;
    private CanaroTextView tvTitle;
    private LinearLayout llLeftBack, llRightMenu;

    private RecyclerView rvBluetoothDevice;
    private BluetoothDeviceAdapter mBluetoothDeviceAdapter;

    private static String TAG = "---BluetoothDevicesActivity";
    public static final int REQUEST_COARSE_LOCATION = 200;
    static public final int REQUEST_CONNECT_BT = 0 * 2300;
    static private final int REQUEST_ENABLE_BT = 0 * 1000;
    static private BluetoothAdapter mBluetoothAdapter = null;
    private static final UUID SPP_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    static private BluetoothSocket mbtSocket = null;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_bluetooth_devices;
    }

    @Override
    public void initStatusBarView() {
        // StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivRightMenu = (ImageView) findViewById(R.id.iv_right_menu);
        ivRightMenu.setVisibility(View.VISIBLE);
        ivRightMenu.setImageResource(R.drawable.refresh);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_bluetooth_devices));
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        llRightMenu = (LinearLayout) findViewById(R.id.lin_right_view);
        rvBluetoothDevice = (RecyclerView) findViewById(R.id.rv_bluetooth_device);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(getActivity());
        rvBluetoothDevice.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvBluetoothDevice.setAdapter(mBluetoothDeviceAdapter);

        try {
            if (initDevicesList() != 0) {
                finish();
            }
        } catch (Exception ex) {
            finish();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
        } else {
            proceedDiscovery();
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        llLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        llRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDevicesList();
            }
        });
        mBluetoothDeviceAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (mBluetoothAdapter == null) {
                    return;
                }

                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }

                BluetoothDevice bluetoothDevice = mBluetoothDeviceAdapter.getItem(position);
               // Toast.makeText(getApplicationContext(), "Connecting to " +bluetoothDevice.getName() + "," + bluetoothDevice.getAddress(), Toast.LENGTH_SHORT).show();
                Log.e("bluetoothDevice",bluetoothDevice.getAddress()+"");
                Log.e("getUuids",bluetoothDevice.getUuids()[0].getUuid()+"");
                Thread connectThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            boolean gotuuid = bluetoothDevice.fetchUuidsWithSdp();
                            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
                            mbtSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                            mbtSocket.connect();
                        } catch (IOException ex) {
                            runOnUiThread(socketErrorRunnable);
                            try {
                                mbtSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mbtSocket = null;
                        } finally {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    finish();

                                }
                            });
                        }
                    }
                });

                connectThread.start();
            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {
                    Set<BluetoothDevice> btDeviceList = mBluetoothAdapter
                            .getBondedDevices();
                    try {
                        if (btDeviceList.size() > 0) {
                            for (BluetoothDevice device : btDeviceList) {
                                if (!mBluetoothDeviceAdapter.getAllData().contains(device)) {
                                    mBluetoothDeviceAdapter.add(device);
                                    mBluetoothDeviceAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }

                break;
        }
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void initActivityBackPress() {
        finish();

    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedDiscovery();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Permission is not granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }


    protected void proceedDiscovery() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        registerReceiver(mBTReceiver, filter);

        mBluetoothAdapter.startDiscovery();
    }

    public static BluetoothSocket getSocket() {
        return mbtSocket;
    }

    private void flushData() {
        try {
            if (mbtSocket != null) {
                mbtSocket.close();
                mbtSocket = null;
            }

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
            }

            if (mBluetoothDeviceAdapter != null) {
                mBluetoothDeviceAdapter.clear();
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

    }

    private int initDevicesList() {
        try {
            flushData();

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(),
                        "Bluetooth not supported!!", Toast.LENGTH_LONG).show();
                return -1;
            }

            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }

            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            try {
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } catch (Exception ex) {

                return -2;
            }
            Toast.makeText(getApplicationContext(),
                    "Getting all available Bluetooth Devices", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        return 0;
    }

    private Runnable socketErrorRunnable = new Runnable() {

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(),
                    "Cannot establish connection", Toast.LENGTH_SHORT).show();
            mBluetoothAdapter.startDiscovery();

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedDiscovery();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Permission is not granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(mBTReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                try {

                    if (!mBluetoothDeviceAdapter.getAllData().contains(device)) {
                        mBluetoothDeviceAdapter.add(device);
                        mBluetoothDeviceAdapter.notifyDataSetChanged();

                    }


//                    if (btDevices == null) {
//                        btDevices = new ArrayAdapter<BluetoothDevice>(
//                                getApplicationContext(), R.layout.row_blutooth_list);
//                    }
//
//
//                    if (btDevices.getPosition(device) < 0) {
//                        btDevices.add(device);
//                        mArrayAdapter.add(device.getName() + "\n"
//                                + device.getAddress() + "\n");
//                        mArrayAdapter.notifyDataSetInvalidated();
//                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
        }
    };
}