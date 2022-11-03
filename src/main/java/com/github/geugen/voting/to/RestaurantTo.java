package com.github.geugen.voting.to;

import com.github.geugen.voting.model.Address;
import com.github.geugen.voting.model.MenuItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;


@Schema(title = "RestaurantTo - DTO used by User Restaurant Controller")
@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    @NotNull
    Address address;

    List<MenuItem> menuItems;

    public RestaurantTo(Integer id, String name, Address address, List<MenuItem> menuItems) {
        super(id, name);
        this.address = address;
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        return "VoteMarkRestaurantTo:" + id + '[' + address.toString() + ']';
    }
}