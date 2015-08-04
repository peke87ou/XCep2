package com.irina.xcep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.irina.xcep.R;
import com.irina.xcep.menu.navitems.NavDrawerItem;
import com.irina.xcep.menu.navitems.NavListItem;
import com.irina.xcep.menu.navitems.NavMenuItem;
import com.irina.xcep.menu.navitems.NavTitleItem;

/**
 * 
 */
public class MenuAdapter extends ArrayAdapter<NavDrawerItem> implements View.OnClickListener {

    private static final int NUM_TYPE_ITEMS = 3;

    private Context context;
    private NavDrawerItem[] items;
    private SelectedListButton listener;

    public interface SelectedListButton{
        void onSelectedButton(int type);
    }
    public MenuAdapter(Context context, int resource, NavDrawerItem[] objects, SelectedListButton listener) {
        super(context, resource, objects);
        this.context = context;
        items = objects;
        context.getResources();

        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public NavDrawerItem getItem(int position) {
        return this.items[position];
    }

    @Override
    public long getItemId(int position) {
        return this.items[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        NavDrawerItem menuItem = getItem(position);

        switch (menuItem.getType()){
            case NavDrawerItem.SECTION_TITLE_ITEM:
                view = getTitleItemView(convertView,parent,menuItem);
                return view;
            case NavDrawerItem.SECTION_MENU_ITEM:
                view = getMenuItemView(convertView, parent, menuItem);
                return view;
            case NavDrawerItem.SECTION_LIST_ITEM:
                view = getListItemView(convertView,parent,menuItem);
                return view;
        }
        return null;
    }

    private View getTitleItemView(View convertView , ViewGroup parent , NavDrawerItem item) {
        NavTitleItem menuSection = (NavTitleItem) item ;
        NavMenuSectionHolder navMenuItemHolder = null;

        if (convertView == null) {
            convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.navdrawer_title, parent, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.nav_drawer_title);

            navMenuItemHolder = new NavMenuSectionHolder();
            navMenuItemHolder.labelView = labelView ;
            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
        }

        navMenuItemHolder.labelView.setText(context.getString(menuSection.getLabel()));


        convertView.setEnabled(false);
        convertView.setOnClickListener(null);

        return convertView ;
    }

    private View getMenuItemView(View convertView , ViewGroup parent , NavDrawerItem item) {
        NavMenuItem menuItem = (NavMenuItem) item ;
        NavMenuItemHolder navMenuItemHolder = null;

        if (convertView == null) {
            convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.item_list_menu, parent, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.menu_title );
            ImageView iconView = (ImageView) convertView
                    .findViewById( R.id.menu_image );

            navMenuItemHolder = new NavMenuItemHolder();
            navMenuItemHolder.labelView = labelView ;
            navMenuItemHolder.iconView = iconView ;

            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
        }

        navMenuItemHolder.labelView.setText(context.getString(menuItem.getLabel()));
        navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());

        return convertView ;
    }

    private View getListItemView(View convertView , ViewGroup parent , NavDrawerItem item) {
        NavListItem menuItem = (NavListItem) item ;
        NavMenuListHolder navMenuListHolder = null;

        if (convertView == null) {
            convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.navdrawer_list, parent, false);

            TextView labelView = (TextView) convertView
                    .findViewById( R.id.list_title );
            ImageButton iconView = (ImageButton) convertView
                    .findViewById( R.id.list_image );

            navMenuListHolder = new NavMenuListHolder();
            navMenuListHolder.labelView = labelView ;
            navMenuListHolder.iconView = iconView ;

            convertView.setTag(navMenuListHolder);
        }

        if ( navMenuListHolder == null ) {
            navMenuListHolder = (NavMenuListHolder) convertView.getTag();
        }

        navMenuListHolder.labelView.setText(context.getString(menuItem.getLabel()));
        navMenuListHolder.iconView.setImageResource(menuItem.getIcon());

        if(item.isClickable()){
            navMenuListHolder.iconView.setOnClickListener(this);
        }else{
            /*Para que no haga nada*/
            navMenuListHolder.iconView.setOnClickListener(null);
        }

        return convertView ;
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return NUM_TYPE_ITEMS;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_image:
                /*TODO cambiar el tipo*/
                listener.onSelectedButton(0);
                break;
        }
    }


    private static class NavMenuItemHolder {
        private TextView labelView;
        private ImageView iconView;
    }

    private class NavMenuSectionHolder {
        private TextView labelView;
    }

    private static class NavMenuListHolder {
        private TextView labelView;
        private ImageButton iconView;
    }
}
