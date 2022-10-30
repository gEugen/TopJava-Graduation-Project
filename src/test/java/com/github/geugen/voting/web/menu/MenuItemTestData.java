package com.github.geugen.voting.web.menu;

import com.github.geugen.voting.model.MenuItem;
import com.github.geugen.voting.web.MatcherFactory;


public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "restaurant");

    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH5_ID = 5;
    public static final int DISH6_ID = 6;
    public static final int DISH7_ID = 7;
    public static final int DISH8_ID = 8;
    public static final int DISH9_ID = 9;
    public static final int DISH10_ID = 10;
    public static final int DISH11_ID = 11;

    public static final MenuItem MENU_ITEM_1 = new MenuItem(DISH1_ID, "Escalope", 250);
    public static final MenuItem MENU_ITEM_2 = new MenuItem(DISH2_ID, "Grilled chicken", 110);
    public static final MenuItem MENU_ITEM_3 = new MenuItem(DISH3_ID, "Marinated squid", 50);
    public static final MenuItem MENU_ITEM_4 = new MenuItem(DISH4_ID, "Scrambled eggs", 90);
    public static final MenuItem MENU_ITEM_5 = new MenuItem(DISH5_ID, "Vegetable stew", 105);
    public static final MenuItem MENU_ITEM_6 = new MenuItem(DISH6_ID, "Italian pasta", 60);
    public static final MenuItem MENU_ITEM_7 = new MenuItem(DISH7_ID, "Sponge cake", 310);
    public static final MenuItem MENU_ITEM_8 = new MenuItem(DISH8_ID, "Coconut ice cream", 210);
    public static final MenuItem MENU_ITEM_9 = new MenuItem(DISH9_ID, "Coffee with milk", 25);
    public static final MenuItem MENU_ITEM_10 = new MenuItem(DISH10_ID, "Coffee", 10);
    public static final MenuItem MENU_ITEM_11 = new MenuItem(DISH11_ID, "Tea", 5);

    public static MenuItem getNew() {
        return new MenuItem(null, "Watson soup", 409);
    }

    public static MenuItem getUpdated() {
        return new MenuItem(DISH4_ID, "Holmes oatmeal", 550);
    }
}
