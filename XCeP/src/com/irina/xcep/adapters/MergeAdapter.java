package com.irina.xcep.adapters;

import java.util.ArrayList;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MergeAdapter extends BaseAdapter {

    private List<BaseAdapter> adapters = new ArrayList<BaseAdapter>();
    protected String noItemsText;

    public MergeAdapter() {
        super();
    }

    public void addAdapter(BaseAdapter adapter){
        adapters.add(adapter);
        adapter.registerDataSetObserver(new CascadeDataSetObserver());
    }

    public void setNoItemsText(String text){
        noItemsText = text;
    }

    @Override
    public int getCount() {
        int total = 0;

        for (BaseAdapter adapter : adapters) {
            total += adapter.getCount();
        }

        if(total == 0 && noItemsText != null){
            total = 1;
        }

        return (total);
    }

    @Override
    public Object getItem(int position) {
        for (BaseAdapter adapter : adapters) {
            int size = adapter.getCount();

            if (position < size) {
                return (adapter.getItem(position));
            }

            position -= size;
        }

        return (null);
    }

    public BaseAdapter getAdapter(int position) {
        for (BaseAdapter adapter : adapters) {
            int size = adapter.getCount();

            if (position < size) {
                return (adapter);
            }

            position -= size;
        }

        return (null);
    }

    @Override
    public int getViewTypeCount() {
        int total = 0;

        for (BaseAdapter adapter : adapters) {
            total += adapter.getViewTypeCount();
        }

        return (Math.max(total, 1)); // needed for setListAdapter() before
        // content add'
    }

    @Override
    public int getItemViewType(int position) {
        int typeOffset = 0;
        int result = -1;

        for (BaseAdapter adapter : adapters) {
            int size = adapter.getCount();

            if (position < size) {
                result = typeOffset + adapter.getItemViewType(position);
                break;
            }

            position -= size;
            typeOffset += adapter.getViewTypeCount();
        }

        return (result);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return (false);
    }

    @Override
    public boolean isEnabled(int position) {
        for (BaseAdapter adapter : adapters) {
            int size = adapter.getCount();

            if (position < size) {
                return (adapter.isEnabled(position));
            }

            position -= size;
        }

        return (false);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        for (BaseAdapter adapter : adapters) {
            int size = adapter.getCount();

            if (position < size) {

                return (adapter.getView(position, convertView, parent));
            }

            position -= size;
        }

        if(noItemsText != null){
            TextView text = new TextView(parent.getContext());
            text.setText(noItemsText);
            return text;
        }

        return (null);
    }

    public long getItemId(int position) {
        for (BaseAdapter adapter : adapters) {
            int size = adapter.getCount();

            if (position < size) {
                return (adapter.getItemId(position));
            }

            position -= size;
        }

        return (-1);
    }

    private class CascadeDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            notifyDataSetInvalidated();
        }
    }
}
