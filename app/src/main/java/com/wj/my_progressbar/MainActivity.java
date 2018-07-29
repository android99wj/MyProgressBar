package com.wj.my_progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.wj.my_progressbar.view.HorizontalProgressWithProgressbar;

public class MainActivity extends AppCompatActivity {

    private static final int UPDATE = 0x111;
    private HorizontalProgressWithProgressbar bar1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = bar1.getProgress();
            if (progress >= 100) {
                handler.removeMessages(UPDATE);
            } else {
                bar1.setProgress(++progress);
                handler.sendEmptyMessageDelayed(UPDATE, 100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar1 = findViewById(R.id.pro_1);
        handler.sendEmptyMessage(UPDATE);
    }
}
