package com.aizenberg;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.support.fsm.IFragmentBackPressListener;
import com.aizenberg.support.fsm.Switcher;

/**
 * Created by Yuriy Aizenberg
 */
public class OneFragment extends Fragment implements IFragmentBackPressListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment, container, false);
        inflate.findViewById(R.id.f).setBackgroundColor(Color.BLUE);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switcher.obtainSwitcher(MainActivity.class).switchTo(Fragments.TWO);
            }
        });
        return inflate;
    }

    @Override
    public boolean onBackPressed() {
        if (true) {
            Switcher.obtainSwitcher(getActivity()).getBack();
        }
        return false;

    }
}
