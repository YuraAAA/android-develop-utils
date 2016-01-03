package com.aizenberg.fsm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.support.common.adapter.CommonAdapter;

/**
 * Created by Yuriy Aizenberg
 */
public class FriendsAdapter extends CommonAdapter<User> {

    public FriendsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return inflater.inflate(android.R.layout.activity_list_item, parent, false);
    }
}
