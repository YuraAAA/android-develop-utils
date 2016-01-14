package com.aizenberg.fsm;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aizenberg.R;
import com.aizenberg.support.fsm.IFragmentBackPressListener;
import com.aizenberg.support.fsm.Switcher;
import com.aizenberg.support.geo.LifecycleHook;
import com.aizenberg.support.geo.SupportLocationManager;
import com.aizenberg.support.utils.FileUtils;
import com.aizenberg.support.utils.IOUtils;
import com.aizenberg.support.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        new AlertDialog.Builder(getActivity())

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Switcher.obtainSwitcher(MainActivity.class).getBack();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        return true;

    }

}
