package com.aizenberg.support.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.aizenberg.support.common.collection.NullExcludeSafetyList;

import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> data = new NullExcludeSafetyList<>();
    private IEmptyAdapterListener emptyAdapterListener;
    protected LayoutInflater inflater;

    public CommonAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setEmptyAdapterListener(IEmptyAdapterListener emptyAdapterListener) {
        this.emptyAdapterListener = emptyAdapterListener;
    }

    public void setData(List<T> data) {
        setData(data, true);
    }

    public void setData(List<T> data, boolean clear) {
        if (clear) this.data.clear();
        this.data.addAll(data);
        if (emptyAdapterListener != null) emptyAdapterListener.onPipelineChange(data.isEmpty());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
