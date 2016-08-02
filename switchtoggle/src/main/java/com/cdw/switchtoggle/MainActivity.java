package com.cdw.switchtoggle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SwitchToggleView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = (SwitchToggleView) findViewById(R.id.stv);

        mView.setSwitchBackground(R.drawable.switch_background);
        mView.setSwichSlide(R.drawable.slide_button_background);

        mView.setOnSwitchListener(new SwitchToggleView.OnSwitchListener() {
            @Override
            public void onSwitchChange(boolean isOpened) {
                Toast.makeText(getApplicationContext(), isOpened ? "打开" : "关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
