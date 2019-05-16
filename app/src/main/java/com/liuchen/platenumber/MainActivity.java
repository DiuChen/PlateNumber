package com.liuchen.platenumber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scanBtn)
    Button scanBtn;
    @BindView(R.id.printerBtn)
    Button printerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.scanBtn, R.id.printerBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scanBtn:
                startActivity(new Intent(this,PlateNumberActivity.class));
                break;
            case R.id.printerBtn:
                startActivity(new Intent(this,PrinterActivity.class));
                break;
        }
    }
}
