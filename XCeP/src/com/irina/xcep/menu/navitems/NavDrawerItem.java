package com.irina.xcep.menu.navitems;

/**
 *
 */
public interface NavDrawerItem {

    public static final int SECTION_TITLE_ITEM = 0;
    public static final int SECTION_MENU_ITEM = 1;
    public static final int SECTION_LIST_ITEM = 2;


    public int getId();
    public int getLabel();
    public int getType();
    public boolean isClickable();
    public boolean updateActionBarTitle();

    public void setLabel(int label);
}
