package com.liuchen.platenumber;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.printer.AidlPrinterStateChangeListener;
import com.centerm.smartpos.aidl.printer.PrintDataObject;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterActivity extends AppCompatActivity {

    @BindView(R.id.printerBtn)
    Button printerBtn;
    private static final String TAG = "PrinterActivity";

    private AidlPrinter printDev = null;
    public AidlDeviceManager manager = null;
    // 打印机回调对象
    private AidlPrinterStateChangeListener callback = new PrinterCallback(); // 打印机回调

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(conn);
    }

    public void bindService() {
        Intent intent = new Intent();
        intent.setPackage("com.centerm.smartposservice");
        intent.setAction("com.centerm.smartpos.service.MANAGER_SERVICE");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 服务连接桥
     */
    public ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            manager = null;
            Log.d(TAG, "onServiceDisconnected: 服务绑定失败");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            manager = AidlDeviceManager.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: 服务绑定成功");
            if (null != manager) {
                onDeviceConnected(manager);
            }
        }
    };

    public void onDeviceConnected(AidlDeviceManager deviceManager) {
        try {
            printDev = AidlPrinter.Stub.asInterface(deviceManager
                    .getDevice(Constant.DEVICE_TYPE.DEVICE_TYPE_PRINTERDEV));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印机回调类
     */
    private class PrinterCallback extends AidlPrinterStateChangeListener.Stub {

        @Override
        public void onPrintError(int arg0) {
            Log.d(TAG, "onPrintError: 打印失败 错误码:" + arg0);
        }

        @Override
        public void onPrintFinish() {
            Log.d(TAG, "onPrintFinish: 打印完成");
        }

        @Override
        public void onPrintOutOfPaper() {
            Log.d(TAG, "onPrintOutOfPaper: 打印机缺纸");
        }
    }

    @OnClick(R.id.printerBtn)
    public void onViewClicked() {
        List<PrintDataObject> list = new ArrayList<>();
        list.add(new PrintDataObject("哈哈哈"));
        list.add(new PrintDataObject("哈哈哈哈哈哈哈哈哈哈哈哈"));

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.pic);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        try {
            if (printDev.setPrintQueue(true)) {
                //打印文字
                printDev.printText(list, callback);
                //吐纸
                printDev.spitPaper(200);
                //打印条形码
                printDev.printBarCode("123456789", callback);
                printDev.spitPaper(200);
                //打印二维码
                printDev.printQRCodeFast("小赵在我后面", 600, Constant.ALIGN.CENTER, callback);
                printDev.spitPaper(200);
                //打印图片
                printDev.printBmpFast(bitmap, Constant.ALIGN.CENTER, callback);
                printDev.spitPaper(200);
            } else {
                Log.d(TAG, "onViewClicked: 开启打印队列失败");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
