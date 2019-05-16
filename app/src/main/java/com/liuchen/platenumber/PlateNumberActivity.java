package com.liuchen.platenumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fosung.libeasypr.view.EasyPRPreSurfaceView;
import com.fosung.libeasypr.view.EasyPRPreView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlateNumberActivity extends AppCompatActivity {

    @BindView(R.id.preSurfaceView)
    EasyPRPreView preSurfaceView;
    @BindView(R.id.btnShutter)
    Button btnShutter;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_number);
        ButterKnife.bind(this);

        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preSurfaceView != null) {
            preSurfaceView.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (preSurfaceView != null) {
            preSurfaceView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preSurfaceView != null) {
            preSurfaceView.onDestroy();
        }
    }

    private void initListener() {
        preSurfaceView.setRecognizedListener(new EasyPRPreSurfaceView.OnRecognizedListener() {
            @Override
            public void onRecognized(String result) {
                if (result == null || result.equals("0")) {
                    Toast.makeText(PlateNumberActivity.this, "换个姿势试试!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlateNumberActivity.this, "识别成功", Toast.LENGTH_SHORT).show();
                    text.setText(result);
                }
            }
        });
    }

    @OnClick(R.id.btnShutter)
    public void onViewClicked() {
        preSurfaceView.recognize();//开始识别
    }
}
