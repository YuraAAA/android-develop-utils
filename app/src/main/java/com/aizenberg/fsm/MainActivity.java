package com.aizenberg.fsm;

import android.app.Activity;
import android.os.Bundle;

import com.aizenberg.R;
import com.aizenberg.support.fsm.Switcher;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switcher
                .createSwitcher(this, R.id.container)
                .setAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_left,
                        R.anim.slide_out_right,
                        R.anim.slide_in_right)
                .switchTo(OneFragment.class);
    }

}
