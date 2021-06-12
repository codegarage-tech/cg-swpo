package com.meembusoft.safewaypharmaonline.activity;

import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.StoreDetailsOrderListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderActionType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderStatusType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.ParamsStoreAcceptOrder;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.PrinterCommands;
import com.meembusoft.safewaypharmaonline.util.PrintsUtils;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FULL_SCREEN_IMAGE_URL;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_STORE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SupplierOrderDetailsActivity extends BaseActivity {

    //Toolbar
    private CanaroTextView tvTitle;
    private LinearLayout linLeftBack;
    private ImageView ivBack, ivPicture;

    private Button btnSkipOrder, btnAcceptOrder, btnOrderStatus;
    private TextView tvTotalsAmount, tvSubtotalsAmount, tvDeliveryFeeAmount, tvDiscountAmount,tvAddAddress;
    private ImageView ivPrinter;
    private RelativeLayout relSkipAcceptedView, relOrderStatusView,relMainSupplierDetails;
    private LinearLayout linShippingAddress;
    private PlaceOrderByItem mStore;
    private CardView cvPictureView;
    private String mImagePath = "", mBase64 = "";
    private RecyclerView rvStoreDetailsOrder;
    private StoreDetailsOrderListAdapter mStoreDetailsOrderListAdapter;
    //Background task
    private DoOrderAcceptTask doOrderAcceptTask;
    private DoOrderDeliveryStatusTask doOrderDeliveryStatusTask;
    private APIInterface mApiInterface;
    float subTotalAmount = 0.0f, shippingAmount = 0.0f, discountAmount = 0.0f, netTotalAmount = 0.0f;
    private AppSupplier mAppSupplier;
    private DetailType mDetailType;
    public static String INTENT_ACTION_SUPPLIER_ORDER_DETAIL = "INTENT_ACTION_SUPPLIER_ORDER_DETAIL";
    // Print instance
    byte FONT_TYPE;
    private static BluetoothSocket _bluetoothSocket;
    private static OutputStream outputStream;
    private int CUSTOM_CODE_SUPPLIER_PRINT_REQUEST = 38;
    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_supplier_order_details_screen;
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
        if (intent != null) {
            String mParcelableDetailType = intent.getStringExtra(AllConstants.INTENT_KEY_DETAIL_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableDetailType)) {
                mDetailType = DetailType.valueOf(mParcelableDetailType);
                Logger.d(TAG, TAG + " >>> " + "mDetailType: " + mDetailType);

                switch (mDetailType) {
                    case REGULAR:
                        Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_STORE);
                        if (mParcelable != null) {
                            mStore = Parcels.unwrap(mParcelable);
                            Logger.d(TAG, TAG + " >>> " + "placeOrderByItem: " + mStore.toString());
                        }
                        break;
                    case FCM:
                        Parcelable mFCMParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_FCM_ORDER_ITEM);
                        if (mFCMParcelable != null) {
                            mStore = Parcels.unwrap(mFCMParcelable);
                            Logger.d(TAG, TAG + " >>> " + "placeOrderByItem: " + mStore.toString());
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void initActivityViews() {
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_order_details));
        linLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        btnSkipOrder = (Button) findViewById(R.id.btn_skip_order);
        btnAcceptOrder = (Button) findViewById(R.id.btn_accept_order);
        btnOrderStatus = (Button) findViewById(R.id.btn_order_status);
        ivPicture = (ImageView) findViewById(R.id.iv_picture);
        ivPrinter = (ImageView) findViewById(R.id.iv_store_details_printer);
        cvPictureView = (CardView) findViewById(R.id.cv_picture_view);
        rvStoreDetailsOrder = (RecyclerView) findViewById(R.id.rv_store_details_order);
        tvSubtotalsAmount = (TextView) findViewById(R.id.tv_subtotals_amt);
        tvDeliveryFeeAmount = (TextView) findViewById(R.id.tv_delivery_fee);
        tvDiscountAmount = (TextView) findViewById(R.id.tv_discount_amt);
        tvTotalsAmount = (TextView) findViewById(R.id.tv_totals_amount);
        tvAddAddress = (TextView) findViewById(R.id.tv_add_address);
        relSkipAcceptedView = (RelativeLayout) findViewById(R.id.rel_skip_accepted_view);
        relOrderStatusView = (RelativeLayout) findViewById(R.id.rel_order_status_view);
        relMainSupplierDetails = (RelativeLayout) findViewById(R.id.rel_main_supplier_details);
        linShippingAddress = (LinearLayout) findViewById(R.id.lin_order_shipping_address);
        linShippingAddress.setVisibility(View.VISIBLE);
        relMainSupplierDetails.setVisibility(View.VISIBLE);
    }

    private void setStatusView() {
        if (mStore != null) {
            if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.PENDING.toString())) {
                relSkipAcceptedView.setVisibility(View.VISIBLE);
                btnOrderStatus.setVisibility(View.GONE);
                relOrderStatusView.setVisibility(View.GONE);
            } else if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.ACCEPTED.toString())) {
                relSkipAcceptedView.setVisibility(View.GONE);
                btnOrderStatus.setVisibility(View.VISIBLE);
                relOrderStatusView.setVisibility(View.VISIBLE);
                btnOrderStatus.setText(getResources().getString(R.string.txt_delivery_order_status));
            } else if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.DELIVERED_TO_DELIVERY_MAN.toString())) {
                relSkipAcceptedView.setVisibility(View.GONE);
                btnOrderStatus.setVisibility(View.VISIBLE);
                relOrderStatusView.setVisibility(View.VISIBLE);
                btnOrderStatus.setText(getResources().getString(R.string.txt_order_delivery_on_the_way_status));
            } else if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.RECEIVED_BY_USER.toString())) {
                relSkipAcceptedView.setVisibility(View.GONE);
                btnOrderStatus.setVisibility(View.VISIBLE);
                relOrderStatusView.setVisibility(View.VISIBLE);
                btnOrderStatus.setText(getResources().getString(R.string.txt_order_received_payment_and_completed_status));
            } else {
                relSkipAcceptedView.setVisibility(View.GONE);
                btnOrderStatus.setVisibility(View.GONE);
                relOrderStatusView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (mStore != null) {
                mAppSupplier = SessionUtil.getSupplier(getActivity());
                setStatusView();

                initItemList(mStore);

                // Register BroadCast
                BroadcastManager.registerBroadcastUpdate(getActivity(), notificationUpdate);
            }
        }
    }

    // Set Data
    private void initItemList(PlaceOrderByItem mStoreDetails) {
        Logger.d(TAG, TAG + " >>> " + "order>>mStoreDetails: " + mStoreDetails.toString());

        if (mStoreDetails != null) {
            if (mStoreDetails.getItems() != null && mStoreDetails.getItems().size() > 0) {
                subTotalAmount = AppUtil.formatFloat(AppUtil.getStoreOrderFromSubtotalPrice(mStoreDetails.getItems()));

                mStoreDetailsOrderListAdapter = new StoreDetailsOrderListAdapter(getActivity());
                rvStoreDetailsOrder.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvStoreDetailsOrder.setAdapter(mStoreDetailsOrderListAdapter);
                mStoreDetailsOrderListAdapter.addAll(mStoreDetails.getItems());
            } else {
                Toast.makeText(getActivity(), getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
            }
            shippingAmount = (((!AllSettingsManager.isNullOrEmpty(mStoreDetails.getShiping_charge()) && (Float.parseFloat(mStoreDetails.getShiping_charge()) > 0.0f))) ? (Float.parseFloat(mStoreDetails.getShiping_charge())) : 0.0f);
            discountAmount = (((!AllSettingsManager.isNullOrEmpty(mStoreDetails.getDiscount()) && (Float.parseFloat(mStoreDetails.getDiscount()) > 0.0f))) ? (Float.parseFloat(mStoreDetails.getDiscount())) : 0.0f);
            netTotalAmount = AppUtil.getTotalPrice(subTotalAmount, discountAmount, shippingAmount);

            tvAddAddress.setText(AppUtil.optStringNullCheckValue(mStoreDetails.getShiping_address()));
            tvSubtotalsAmount.setText(subTotalAmount + " " + getResources().getString(R.string.title_BDT));
            tvDeliveryFeeAmount.setText(shippingAmount + " " + getResources().getString(R.string.title_BDT));
            tvDiscountAmount.setText(discountAmount + " " + getResources().getString(R.string.title_BDT));
            tvTotalsAmount.setText(netTotalAmount + " " + getResources().getString(R.string.title_BDT));

            if (mStoreDetails.getImage() != null) {
                AppUtil.loadImage(getActivity(), ivPicture, mStoreDetails.getImage(), false, false, true);
            }
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        linLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        ivPrinter.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mStore != null){
                    connectBluetoothDevice(mStore);
                }

            }
        });

        btnSkipOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    if (doOrderAcceptTask != null && doOrderAcceptTask.getStatus() == AsyncTask.Status.RUNNING) {
                        doOrderAcceptTask.cancel(true);
                    }
                    //Ship Order
                    if (mStore != null && mAppSupplier != null) {
                        doOrderAcceptTask = new DoOrderAcceptTask(getActivity(), OrderActionType.SKIP);
                        doOrderAcceptTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });


        btnAcceptOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    if (doOrderAcceptTask != null && doOrderAcceptTask.getStatus() == AsyncTask.Status.RUNNING) {
                        doOrderAcceptTask.cancel(true);
                    }
                    //Accept Order
                    if (mStore != null && mAppSupplier != null) {
                        doOrderAcceptTask = new DoOrderAcceptTask(getActivity(), OrderActionType.ACCEPT);
                        doOrderAcceptTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });

        btnOrderStatus.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    // Supplier will not stay/delay for user reception. Supplier can change status
                    if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.DELIVERED_TO_DELIVERY_MAN.toString())) {
                        mStore.setStatus(OrderStatusType.RECEIVED_BY_USER.toString());
                        setStatusView();
                        return;
                    }

                    if (doOrderDeliveryStatusTask != null && doOrderDeliveryStatusTask.getStatus() == AsyncTask.Status.RUNNING) {
                        doOrderDeliveryStatusTask.cancel(true);
                    }
                    // Order Delivery Status
                    if (mStore != null && mAppSupplier != null) {
                        doOrderDeliveryStatusTask = new DoOrderDeliveryStatusTask(getActivity());
                        doOrderDeliveryStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });

        cvPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStore.getImage() != null) {
                    Intent iFullImageActivity = new Intent(getApplicationContext(), FullScreenActivity.class);
                    iFullImageActivity.putExtra(INTENT_KEY_FULL_SCREEN_IMAGE_URL, mStore.getImage());
                    startActivity(iFullImageActivity);
                }
                // showPicture();
            }
        });
    }

//    private void showPicture() {
//        Matisse.from(getActivity())
//                .choose(MimeType.ofImage())
//                .theme(R.style.Matisse_Dracula)
//                .capture(true)
//                .setDefaultCaptureStrategy()
//                .countable(false)
//                .maxSelectable(1)
//                .imageEngine(new GlideEngine())
//                .forResult(AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER);
//    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent != null) {
//            Parcelable mFCMParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_FCM_ORDER_ITEM);
//            if (mFCMParcelable != null) {
//                mStore = Parcels.unwrap(mFCMParcelable);
//                Logger.d(TAG, TAG + " >>>FCM " + "mStore: " + mStore.toString());
//                setStatusView();
//                initItemList(mStore);
//            }
//        }
//    }

private void connectBluetoothDevice (PlaceOrderByItem mStoreDetails){
    if (_bluetoothSocket == null){

        Intent bluetoothIntent = new Intent(getApplicationContext(), BluetoothDevicesActivity.class);
        this.startActivityForResult(bluetoothIntent, CUSTOM_CODE_SUPPLIER_PRINT_REQUEST);
        //relMainSupplierDetails.setVisibility(View.GONE);
    } else  {
        printSupplierBillReceive(mStoreDetails);
    }
}
    /*All Printer method*/
    protected void printSupplierBillReceive(PlaceOrderByItem mStoreDetails) {
        try {
            if (mStoreDetails !=null) {
                String getStatus = OrderStatusType.getMessage(mStoreDetails.getStatus());
                String address = AppUtil.optStringNullCheckValue(mStoreDetails.getShiping_address());

                if (_bluetoothSocket == null) {
                    Intent BTIntent = new Intent(getApplicationContext(), BluetoothDevicesActivity.class);
                    this.startActivityForResult(BTIntent, BluetoothDevicesActivity.REQUEST_CONNECT_BT);
                } else {
                    OutputStream opstream = null;
                    try {
                        opstream = _bluetoothSocket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    outputStream = opstream;

                    //print command
                    try {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        outputStream = _bluetoothSocket.getOutputStream();
                        byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
                        outputStream.write(printformat);


                        printCustom("Safeway Pharma", 3, 1);
                        printPhoto(R.mipmap.print_logo);
                      //  printNewLine();
                        // Header
                        printCustom("La-58, Moddha Badda, Progati Sharani, Gulshan,\nDhaka-1212 Bangladesh", 0, 1);
                        printCustom("Hot Line: +8801779-883969 \n         +8801831-801494",0, 1);
                        printNewLine();
                        // End: Header
                        String totalItem = "";
                        if (mStoreDetails.getItems() != null && mStoreDetails.getItems().size() > 0) {
                            totalItem = String.valueOf(mStoreDetails.getItems().size());
                        }
                        printCustom("Delivery ", 2, 0);
                        printCustom("Order Date    : "+mStoreDetails.getOrder_date(), 0, 0);
                        printCustom("Customer name : "+mStoreDetails.getCustomer_name(), 0, 0);
                        printCustom("Item        : "+totalItem, 0, 0);
                        printCustom("Status        : "+getStatus, 0, 0);
                        printNewLine();
                        printNewLine();
                        printCustom("Order ", 2, 0);

                        if (mStoreDetails.getItems() != null && mStoreDetails.getItems().size() > 0) {
                            for (int i = 0; i <  mStoreDetails.getItems().size(); i++) {
                                printCustom(mStoreDetails.getItems().get(i).getName() , 0, 0);
                                String subTotals = AppUtil.optStringNullCheckValue(mStoreDetails.getItems().get(i).getSub_total_prices());
                                printCustom("Qty : "  +mStoreDetails.getItems().get(i).getQuantitys() +"            "+"Price :   " + mStoreDetails.getItems().get(i).getPrices() +"          "+"Total Tk :  " + subTotals, 0, 0);
//                                printNewLine();
                            }
                        }
                       // printNewLine();
                      //  printCustom("Order Summary", 2, 2);
                        printCustom(new String(new char[64]).replace("\u0000", "."),0,1);

                        printCustom("Sub Totals                                           " + subTotalAmount + " " + getResources().getString(R.string.title_BDT), 0, 0);
                        printCustom("Delivery Fee                                         " + shippingAmount + " " + getResources().getString(R.string.title_BDT), 0, 0);
                        printCustom(new String(new char[64]).replace("\u0000", "."),0,1);
                        printCustom("Total Bill                                           " + netTotalAmount + " " + getResources().getString(R.string.title_BDT), 0, 0);

                        printNewLine();
                        printCustom("Your Delivery Address", 1, 1);
                        printCustom("      " +address+"     ",0, 1);
                        printNewLine();
                        printCustom("Payment Method", 1, 0);
                        printCustom("Cash on Delivery (COD)", 1, 0);
                        printCustom("Total Taka : " + netTotalAmount + " " + getResources().getString(R.string.title_BDT), 1, 0);
                        printNewLine();
                        printNewLine();
                        printCustom(".....................         ..................", 0, 0);
                        printCustom("Receiver\'s signature         Supplier signature", 0, 0);
                        printNewLine();
                        printNewLine();
                        printCustom("Thank you for coming & we look", 0, 1);
                        printCustom("forward to serve you again", 0, 1);
                        printCustom("Email : safewaypharma18@gmail.com", 0, 1);
                        printNewLine();
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Print Bill error", e.getMessage()+"");

        }

    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        byte[]  xs = new byte[]{0x1B, 0x21, 0x01};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(xs);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
                case 4:
                    outputStream.write(cc);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = PrintsUtils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(PrintsUtils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try{
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }



    /*****************************
     * Order notification update *
     *****************************/
    BroadcastReceiver notificationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                PlaceOrderByItem order = BroadcastManager.getBroadcastData(intent);
                if (order != null) {
                    mStore = order;
                    setStatusView();
                    Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Updated store order detail");
                }
            }
        }
    };

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
//            List<String> mData = Matisse.obtainPathResult(data);
//
//            if (mData.size() == 1) {
//                mImagePath = mData.get(0);
//                Logger.d(TAG, "MatisseImage: " + mImagePath);
//
//                AppUtil.loadImage(getActivity(), ivPicture, mImagePath, false, false, false);
//
//                try {
//                    File imageZipperFile = new ImageZipper(getActivity())
//                            .setQuality(100)
//                            .setMaxWidth(200)
//                            .setMaxHeight(200)
//                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                            .compressToFile(new File(mImagePath));
//                    mBase64 = AllConstants.PREFIX_BASE64_STRING + ImageZipper.getBase64forImage(imageZipperFile);
//                    Logger.d(TAG, "MatisseImage(mBase64): " + mBase64);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
          if(requestCode == CUSTOM_CODE_SUPPLIER_PRINT_REQUEST ) {
              try {
                //  _bluetoothSocket = BluetoothDeviceList.getSocket();
                  _bluetoothSocket = BluetoothDevicesActivity.getSocket();
                  if (_bluetoothSocket != null) {
                      //printText(message.getText().toString())
                      if (mStore != null) {
                          printSupplierBillReceive(mStore);
                      } else {
                          Toast.makeText(getApplicationContext(),
                                  "Data not found!!", Toast.LENGTH_LONG).show();
                      }
                  }

              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
    }

    @Override
    public void initActivityBackPress() {
        if (!mStore.getStatus().equalsIgnoreCase(OrderStatusType.PENDING.toString())) {
            Intent intentAccepted = new Intent();
            intentAccepted.putExtra(INTENT_KEY_STORE, Parcels.wrap(mStore));
            intentAccepted.putExtra(INTENT_KEY_ORDER_SETTING_TYPE, OrderActionType.STATUS.name());
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
            setResult(RESULT_OK, intentAccepted);
        }
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();
        // Unregister BroadCast
        BroadcastManager.unregisterBroadcastUpdate(getActivity(), notificationUpdate);

        if (doOrderAcceptTask != null && doOrderAcceptTask.getStatus() == AsyncTask.Status.RUNNING) {
            doOrderAcceptTask.cancel(true);
        }

        if (doOrderDeliveryStatusTask != null && doOrderDeliveryStatusTask.getStatus() == AsyncTask.Status.RUNNING) {
            doOrderDeliveryStatusTask.cancel(true);
        }
        try {
            if(_bluetoothSocket!= null){
                outputStream.close();
                _bluetoothSocket.close();
                _bluetoothSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /************************
     * Server communication *
     ************************/
    private class DoOrderAcceptTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsStoreAcceptOrder mParamsStoreAcceptOrder;
        OrderActionType mOrderActionType;

        public DoOrderAcceptTask(Context context, OrderActionType orderActionType) {
            mContext = context;
            mOrderActionType = orderActionType;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected void onPreExecute() {
            showPopupDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse> call = null;
                switch (mOrderActionType) {
                    case ACCEPT:
                        ParamsStoreAcceptOrder mParamsStoreAcceptOrder = new ParamsStoreAcceptOrder(mAppSupplier.getUser_id(), mStore.getId());
                        Logger.d(TAG, TAG + " AcceptOrder>>> " + "DoOrderAcceptTask: " + mParamsStoreAcceptOrder.toString());
                        call = mApiInterface.apiStoreAcceptedOrder(mParamsStoreAcceptOrder);
                        break;

                    case SKIP:
                        ParamsStoreAcceptOrder mParamsStoreSkipOrder = new ParamsStoreAcceptOrder(mAppSupplier.getUser_id(), mStore.getId());
                        Logger.d(TAG, TAG + " SkipOrder>>> " + "DoOrderAcceptTask: " + mParamsStoreSkipOrder.toString());
                        call = mApiInterface.apiStoreSkipdOrder(mParamsStoreSkipOrder);
                        break;
                }

                if (call != null) {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        return response;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissPopupDialog();

                if (result != null) {
                    Logger.d(TAG, "APIResponse(DoOrderAcceptTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoOrderAcceptTask()): onResponse-object = " + data.toString());
                        switch (mOrderActionType) {
                            case ACCEPT:
                                mStore.setStatus(OrderStatusType.ACCEPTED.toString());
                                if (mDetailType == DetailType.REGULAR) {
                                    Intent intentAccepted = new Intent();
                                    intentAccepted.putExtra(INTENT_KEY_STORE, Parcels.wrap(mStore));
                                    intentAccepted.putExtra(INTENT_KEY_ORDER_SETTING_TYPE, OrderActionType.ACCEPT.name());
                                    overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                                    setResult(RESULT_OK, intentAccepted);
                                } else if (mDetailType == DetailType.FCM) {
                                    BroadcastManager.registerBroadcastUpdate(getActivity(), new IntentFilter(INTENT_ACTION_SUPPLIER_ORDER_DETAIL), notificationUpdate);
                                    BroadcastManager.sendBroadcast(getActivity(), new Intent(INTENT_ACTION_SUPPLIER_ORDER_DETAIL), mStore);
                                }
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_order_accept_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                                break;

                            case SKIP:
                                mStore.setStatus(OrderStatusType.CANCELLED.toString());
                                if (mDetailType == DetailType.REGULAR) {
                                    Intent intentSkip = new Intent();
                                    intentSkip.putExtra(INTENT_KEY_STORE, Parcels.wrap(mStore));
                                    intentSkip.putExtra(INTENT_KEY_ORDER_SETTING_TYPE, OrderActionType.SKIP.name());
                                    overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                                    setResult(RESULT_OK, intentSkip);
                                } else if (mDetailType == DetailType.FCM) {
                                    BroadcastManager.registerBroadcastUpdate(getActivity(), new IntentFilter(INTENT_ACTION_SUPPLIER_ORDER_DETAIL), notificationUpdate);
                                    BroadcastManager.sendBroadcast(getActivity(), new Intent(INTENT_ACTION_SUPPLIER_ORDER_DETAIL), mStore);
                                }
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_order_skip_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                        }
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class DoOrderDeliveryStatusTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        public DoOrderDeliveryStatusTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected void onPreExecute() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopupDialog();
                }
            }, 50);
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse> call = null;
                ParamsStoreAcceptOrder mParamsDeliveryOnTheWay = new ParamsStoreAcceptOrder(mStore.getId());
                Logger.d(TAG, TAG + " mParamsDeliveryOnTheWay>>> " + "DoOrderDeliveryStatusTask: " + mParamsDeliveryOnTheWay.toString());
                if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.ACCEPTED.toString())) {
                    call = mApiInterface.apiDeliveryOnTheWayOrder(mParamsDeliveryOnTheWay);
                } else if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.RECEIVED_BY_USER.toString())) {
                    call = mApiInterface.apiPaymentReceivedBySupplier(mParamsDeliveryOnTheWay);
                }

                if (call != null) {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        return response;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissPopupDialog();

                if (result != null) {
                    Logger.d(TAG, "APIResponse(DoOrderDeliveryStatusTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoOrderDeliveryStatusTask()): onResponse-object = " + data.toString());
                        if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.ACCEPTED.toString())) {
                            mStore.setStatus(OrderStatusType.DELIVERED_TO_DELIVERY_MAN.toString());
                        } else if (mStore.getStatus().equalsIgnoreCase(OrderStatusType.RECEIVED_BY_USER.toString())) {
                            mStore.setStatus(OrderStatusType.DONE.toString());
                        }

                        Intent intentAccepted = new Intent();
                        intentAccepted.putExtra(INTENT_KEY_STORE, Parcels.wrap(mStore));
                        intentAccepted.putExtra(INTENT_KEY_ORDER_SETTING_TYPE, OrderActionType.STATUS.name());
                        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                        setResult(RESULT_OK, intentAccepted);
                        finish();
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}