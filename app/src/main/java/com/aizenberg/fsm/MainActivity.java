package com.aizenberg.fsm;

import android.app.Activity;
import android.os.Bundle;

import com.aizenberg.R;
import com.aizenberg.support.fsm.IActivityBackPressListener;
import com.aizenberg.support.fsm.ISwitcher;
import com.aizenberg.support.fsm.Switcher;
import com.aizenberg.support.fsm.model.Alias;
import com.aizenberg.support.utils.FileUtils;

public class MainActivity extends Activity implements IActivityBackPressListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create switcher for this activity
        ISwitcher switcher = Switcher.createSwitcher(this, R.id.container);
        registerAliases();
        switcher.switchTo("one");
//        switcher.switchTo(OneFragment.class);
//        switcher.switchTo(Fragments.ONE);

    }

    private void registerAliases() {
        Switcher.obtainSwitcher(this).addAliases(
                new Alias(OneFragment.class, "one"),
                new Alias(TwoFragment.class, "two"),
                new Alias(ThreeFragment.class, "three")
        );
    }

    @Override
    public void onBackPressed() {
        if (!Switcher.obtainSwitcher(this).overrideBack())
            getBack();

    }

    @Override
    public void getBack() {
        super.onBackPressed();
    }
}
