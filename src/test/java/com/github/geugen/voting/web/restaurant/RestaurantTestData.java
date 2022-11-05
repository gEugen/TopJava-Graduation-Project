package com.github.geugen.voting.web.restaurant;

import com.github.geugen.voting.model.Address;
import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.RestaurantTo;
import com.github.geugen.voting.to.VoteMarkRestaurantTo;
import com.github.geugen.voting.web.MatcherFactory;
import com.github.geugen.voting.web.menu.MenuItemTestData;
import com.github.geugen.voting.web.vote.VoteTestData;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "address.id");

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_UPDATE_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields(
                                    "votes.voteDate", "votes.voteTime", "votes.restaurant.votes", "votes.user.password",
                                    "votes.user.registered", "votes.user.votes", "votes.restaurant.address.id", "address.id", "menuItems")
                            .isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_GET_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("menuItems", "users").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final MatcherFactory.Matcher<RestaurantTo> USER_RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class);

    public static final MatcherFactory.Matcher<VoteMarkRestaurantTo> VOTE_MARK_RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteMarkRestaurantTo.class);

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT4_ID = 4;
    public static final int RESTAURANT5_ID = 5;
    public static final int NOT_FOUND = 200;

    public static final Address address = new Address(1, "CITY", "STREET", 100);

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "ASTORIA", address);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "CONTINENTAL", address);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "PRAGUE", address);
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT4_ID, "SUSHI BAR", address);
    public static final Restaurant restaurant5 = new Restaurant(RESTAURANT5_ID, "NIAM-NIAM", address);

    public static final List<Restaurant> restaurants;
    public static final List<Restaurant> restaurantsWithUserVotes;

    static {
        restaurants = getListOfRestaurants();
        restaurantsWithUserVotes = getListOfRestaurants();
        restaurant1.setMenuItems(List.of(MenuItemTestData.MENU_ITEM_1, MenuItemTestData.MENU_ITEM_2, MenuItemTestData.MENU_ITEM_3));
        restaurant2.setMenuItems(List.of(MenuItemTestData.MENU_ITEM_4, MenuItemTestData.MENU_ITEM_5, MenuItemTestData.MENU_ITEM_6));
        restaurant3.setMenuItems(List.of(MenuItemTestData.MENU_ITEM_7, MenuItemTestData.MENU_ITEM_8, MenuItemTestData.MENU_ITEM_9));
        restaurant4.setMenuItems(Collections.emptyList());
        restaurant5.setMenuItems(List.of(MenuItemTestData.MENU_ITEM_10, MenuItemTestData.MENU_ITEM_11));
    }

    public static Restaurant getUpdated() {
        Restaurant updatedRestaurant = new Restaurant(restaurant3);
        updatedRestaurant.setName("ASTORIA_NEW");
        updatedRestaurant.setAddress(new Address(null, "TULA", "LENINA", 25));
        return updatedRestaurant;
    }

    public static Restaurant getUpdatedForCompare() {
        Restaurant updatedForCompare = new Restaurant(getUpdated());
        Vote updatedVote8 = new Vote(VoteTestData.user8Vote);
        Vote updatedVote9 = new Vote(VoteTestData.user9Vote);
        updatedVote8.setRestaurant(getUpdated());
        updatedVote9.setRestaurant(getUpdated());
        return updatedForCompare;
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "NEW_RESTAURANT", new Address(null, "TULA", "LENINA", 25));
    }

    public static List<Restaurant> getListOfRestaurants() {
        return List.of(
                new Restaurant(restaurant1),
                new Restaurant(restaurant2),
                new Restaurant(restaurant3),
                new Restaurant(restaurant4),
                new Restaurant(restaurant5));
    }
}
