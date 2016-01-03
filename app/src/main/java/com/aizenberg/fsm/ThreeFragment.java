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
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IEventReceiver;
import com.aizenberg.support.fsm.Switcher;

import java.util.UUID;

/**
 * Created by Yuriy Aizenberg
 */
public class ThreeFragment extends Fragment implements IEventReceiver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment, container, false);
        inflate.findViewById(R.id.f).setBackgroundColor(Color.BLACK);

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switcher.obtainSwitcher(MainActivity.class).clearBackStack().switchTo(OneFragment.class);
            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getBus().addListeners(this, "a", "b", "c");
        EventBus.getBus().addListener("computation", this);
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
