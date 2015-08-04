package com.irina.xcep.menu.navitems;

/**
 * 
 */
public class NavTitleItem implements NavDrawerItem{

    private int id;
    private int label;

    private NavTitleItem(){}

    public static NavTitleItem create(int id,int label){
        NavTitleItem navDrawerItem = new NavTitleItem();
        navDrawerItem.setLabel(label);
        return navDrawerItem;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getLabel() {
        return label;
    }

    @Override
    public int getType() {
        return SECTION_TITLE_ITEM;
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public void setLabel(int label) {
        this.label = label;
    }
}
