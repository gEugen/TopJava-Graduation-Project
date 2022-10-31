package com.github.geugen.voting.to;

import com.github.geugen.voting.model.Address;
import com.github.geugen.voting.model.Restaurant;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;


@Value
@EqualsAndHashCode(callSuper = true)
public class AdminRestaurantTo extends NamedTo {

    @NotNull
    Address address;

    public AdminRestaurantTo(Integer id, String name, Address address) {
        super(id, name);
        this.address = address;
    }

    public AdminRestaurantTo(Restaurant restaurant) {
        super(restaurant.getId(), restaurant.getName());
        this.address = restaurant.getAddress();
    }

    @Override
    public String toString() {
        return "AdminRestaurantTo:" + id + '[' + address.toString() + ']';
    }
}
