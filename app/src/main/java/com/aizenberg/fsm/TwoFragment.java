package com.aizenberg.fsm;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.R;
import com.aizenberg.support.fsm.Switcher;

/**
 * Created by Yuriy Aizenberg
 */
public class TwoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment, container, false);
        inflate.findViewById(R.id.f).setBackgroundColor(Color.RED);

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switcher.obtainSwitcher(MainActivity.class)
                        .withAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                        .switchTo(Fragments.THREE);
            }
        });
        return inflate;
    }
}
