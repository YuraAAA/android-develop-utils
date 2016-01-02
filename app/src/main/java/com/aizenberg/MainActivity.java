package com.aizenberg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.aizenberg.support.cache.MemCache;
import com.aizenberg.support.cache.config.ChangeConfig;
import com.aizenberg.support.event.IAction;
import com.aizenberg.support.fsm.IActivityBackPressListener;
import com.aizenberg.support.fsm.ISwitcher;
import com.aizenberg.support.fsm.Switcher;
import com.aizenberg.support.logger.Logger;
import com.aizenberg.support.logger.LoggerFactory;
import com.aizenberg.support.validation.Validator;

import java.util.UUID;

public class MainActivity extends Activity implements IActivityBackPressListener {

    private Logger log = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean emailValid = Validator.isEmail("yuriy");
        boolean emailValid1 = Validator.isEmail("yuriy@mail.ru");
        final ISwitcher switcher = Switcher.createSwitcher(this, R.id.container);
        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.switchTo(Fragments.ONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        ChangeConfig<UUID> eventConfig = ChangeConfig.createEventConfig(null, (IAction) null);
        MemCache.addConfig(UUID.class, eventConfig);

        if (!Switcher.obtainSwitcher(this).overrideBack())
            getBack();

    }

    @Override
    public void getBack() {
        super.onBackPressed();
    }
}
