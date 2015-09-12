package com.irina.xcep.menu.navitems;

import android.content.Context;

/**
 * 
 */
public class NavListItem implements NavDrawerItem{

    private int id;
    private int label;
    private int icon;
    private boolean updateActionBarTitle;


    private NavListItem() {
    }

    public static NavListItem create( int id, int label, int resIdIcon, boolean updateActionBarTitle, Context context ) {
        NavListItem item = new NavListItem();
        item.setId(id);
        item.setLabel(label);
        item.setIcon(resIdIcon);
        item.setUpdateActionBarTitle(updateActionBarTitle);
        return item;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getLabel() {
        return label;
    }

    public int getIcon(){
        return icon;
    }
    @Override
    public int getType() {
        return SECTION_LIST_ITEM;
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public boolean updateActionBarTitle() {
        return updateActionBarTitle;
    }

    @Override
    public void setLabel(int label) {
        this.label = label;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setIcon(int icon){
        this.icon=icon;
    }

    public void setUpdateActionBarTitle(boolean updateActionBarTitle){
        this.updateActionBarTitle = updateActionBarTitle;
    }
}
