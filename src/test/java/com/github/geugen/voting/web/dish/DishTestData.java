package com.github.geugen.voting.web.dish;

import com.github.geugen.voting.model.Dish;
import com.github.geugen.voting.web.MatcherFactory;


public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");

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

    public static final Dish dish1 = new Dish(DISH1_ID, "Escalope", 250);
    public static final Dish dish2 = new Dish(DISH2_ID, "Grilled chicken", 110);
    public static final Dish dish3 = new Dish(DISH3_ID, "Marinated squid", 50);
    public static final Dish dish4 = new Dish(DISH4_ID, "Scrambled eggs", 90);
    public static final Dish dish5 = new Dish(DISH5_ID, "Vegetable stew", 105);
    public static final Dish dish6 = new Dish(DISH6_ID, "Italian pasta", 60);
    public static final Dish dish7 = new Dish(DISH7_ID, "Sponge cake", 310);
    public static final Dish dish8 = new Dish(DISH8_ID, "Coconut ice cream", 210);
    public static final Dish dish9 = new Dish(DISH9_ID, "Coffee with milk", 25);
    public static final Dish dish10 = new Dish(DISH10_ID, "Coffee", 10);
    public static final Dish dish11 = new Dish(DISH11_ID, "Tea", 5);

    public static Dish getNew() {
        return new Dish(null, "Watson soup", 409);
    }

    public static Dish getUpdated() {
        return new Dish(DISH4_ID, "Holmes oatmeal", 550);
    }
}
