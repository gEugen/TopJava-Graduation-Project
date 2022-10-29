package com.github.geugen.voting.web.restaurant;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.AdminRestaurantTo;
import com.github.geugen.voting.web.MatcherFactory;
import com.github.geugen.voting.web.dish.DishTestData;
import com.github.geugen.voting.web.vote.VoteTestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class RestaurantTestData {

    public static final MatcherFactory.Matcher<AdminRestaurantTo> RESTAURANT_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(AdminRestaurantTo.class);

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_UPDATE_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields(
                                    "votes.voteDate", "votes.voteTime", "votes.restaurant.votes", "votes.user.password",
                                    "votes.user.registered", "votes.user.votes")
                            .isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_GET_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes", "users").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT4_ID = 4;
    public static final int RESTAURANT5_ID = 5;
    public static final int NOT_FOUND = 200;

    public static final String RESTAURANT1_MAIL = "astoria@yandex.ru";
    public static final String RESTAURANT2_MAIL = "continental@yandex.ru";
    public static final String RESTAURANT3_MAIL = "prague@gmail.com";
    public static final String RESTAURANT4_MAIL = "sushibar@gmail.com";
    public static final String RESTAURANT5_MAIL = "niam-niam@gmail.com";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "ASTORIA", RESTAURANT1_MAIL);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "CONTINENTAL", RESTAURANT2_MAIL);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "PRAGUE", RESTAURANT3_MAIL);
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT4_ID, "SUSHI BAR", RESTAURANT4_MAIL);
    public static final Restaurant restaurant5 = new Restaurant(RESTAURANT5_ID, "NIAM-NIAM", RESTAURANT5_MAIL);

    public static final List<Restaurant> restaurants;
    public static final List<Restaurant> restaurantsWithUserVotes;

    static {
        restaurants = getListOfRestaurants();
        restaurantsWithUserVotes = getListOfRestaurants();
        restaurant1.setDishes(List.of(DishTestData.dish1, DishTestData.dish2, DishTestData.dish3));
        restaurant2.setDishes(List.of(DishTestData.dish4, DishTestData.dish5, DishTestData.dish6));
        restaurant3.setDishes(List.of(DishTestData.dish7, DishTestData.dish8, DishTestData.dish9));
        restaurant5.setDishes(List.of(DishTestData.dish10, DishTestData.dish11));
    }

    public static Restaurant getUpdated() {
        Restaurant updatedRestaurant = new Restaurant(restaurant1);
        updatedRestaurant.setName("ASTORIA_NEW");
        updatedRestaurant.setEmail("astoria_new@yandex.ru");
        return updatedRestaurant;
    }

    public static Restaurant getUpdatedForCompare() {
        Restaurant updatedForCompare = new Restaurant(getUpdated());
        Vote updatedVote1 = new Vote(VoteTestData.user1Vote);
        Vote updatedVote2 = new Vote(VoteTestData.adminVote);
        updatedVote1.setRestaurant(getUpdated());
        updatedVote2.setRestaurant(getUpdated());
        updatedForCompare.setVotes(List.of(updatedVote1, updatedVote2));
        return updatedForCompare;
    }

    public static AdminRestaurantTo getNew() {
        return new AdminRestaurantTo(null, "NEW_RESTAURANT", "new_restaurant@mail.ru");
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
