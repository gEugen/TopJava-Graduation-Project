package com.github.geugen.voting.to;

import com.github.geugen.voting.model.Address;
import com.github.geugen.voting.model.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;


@Value
@EqualsAndHashCode(callSuper = true)
public class UserRestaurantTo extends NamedTo {

    @NotNull
    Address address;

    List<MenuItem> menuItems;

    boolean voteMark;

    public UserRestaurantTo(Integer id, String name, Address address, List<MenuItem> menuItems, boolean voteMark) {
        super(id, name);
        this.address = address;
        this.menuItems = menuItems;
        this.voteMark = voteMark;
    }

    @Override
    public String toString() {
        return "UserRestaurantTo:" + id + '[' + address.toString() + ']';
    }
}
