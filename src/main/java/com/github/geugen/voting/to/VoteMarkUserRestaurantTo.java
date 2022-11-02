package com.github.geugen.voting.to;

import com.github.geugen.voting.model.Address;
import com.github.geugen.voting.model.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;


@Value
@EqualsAndHashCode(callSuper = true)
public class VoteMarkUserRestaurantTo extends NamedTo {

    @NotNull
    Address address;

    List<MenuItem> menuItems;

    boolean voteMark;

    public VoteMarkUserRestaurantTo(Integer id, String name, Address address, List<MenuItem> menuItems, boolean voteMark) {
        super(id, name);
        this.address = address;
        this.menuItems = menuItems;
        this.voteMark = voteMark;
    }

    @Override
    public String toString() {
        return "VoteMarkUserRestaurantTo:" + id + '[' + address.toString() + ']';
    }
}