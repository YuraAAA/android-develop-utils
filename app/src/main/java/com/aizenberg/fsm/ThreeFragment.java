package com.aizenberg.fsm;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.R;
import com.aizenberg.event.BackService;
import com.aizenberg.event.Events;
import com.aizenberg.support.cache.MemCache;
import com.aizenberg.support.cache.config.ChangeConfig;
import com.aizenberg.support.cache.config.ICacheChangeListener;
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IEventIdentificationReceiver;
import com.aizenberg.support.event.IEventReceiver;
import com.aizenberg.support.fsm.Switcher;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yuriy Aizenberg
 */
public class ThreeFragment extends Fragment implements IEventReceiver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment, container, false);


        MemCache.cache(UUID.class).put("uuid", UUID.randomUUID());

        MemCache.cache(UUID.class).setExpire("uuid", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));


        inflate.findViewById(R.id.f).setBackgroundColor(Color.BLACK);

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putLong("id", 1);
                Switcher
                        .obtainSwitcher(MainActivity.class)
                        .withoutAnimation()
                        .switchTo(OneFragment.class, args);


            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus
                .getBus()
                .addListener("computation", new IEventIdentificationReceiver() {
                    @Override
                    public Long getIdentifier() {
                        return IEventIdentificationReceiver.ANY;
                    }

                    @Override
                    public void onReceiveAction(String action, Object... args) {

                    }
                });


        EventBus.getBus().addListeners(this, "computation", "camera_image", "file_copy");
//        EventBus.getBus().addListener("computation", this);
        EventBus.getBus().addListener(Events.COMPUTATION, this);
        getActivity().startService(new Intent(getActivity(), BackService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
//        EventBus.getBus().removeListeners("computation");
        EventBus.getBus().removeListener("computation", this);
    }

    @Override
    public void onReceiveAction(String action, Object... args) {
        UUID uuid = (UUID) args[0];
    }
}
