package com.github.geugen.voting.to;

import com.github.geugen.voting.model.Address;
import com.github.geugen.voting.model.MenuItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;


@Schema(title = "VoteMarkRestaurantTo - used to restaurant data with menu items and vote mark getting additionally")
@Value
@EqualsAndHashCode(callSuper = true)
public class VoteMarkRestaurantTo extends NamedTo {

    @NotNull
    Address address;

    List<MenuItem> menuItems;

    boolean voteMark;

    public VoteMarkRestaurantTo(Integer id, String name, Address address, List<MenuItem> menuItems, boolean voteMark) {
        super(id, name);
        this.address = address;
        this.menuItems = menuItems;
        this.voteMark = voteMark;
    }

    @Override
    public String toString() {
        return "VoteMarkRestaurantTo:" + id + '[' + address.toString() + ']';
    }
}