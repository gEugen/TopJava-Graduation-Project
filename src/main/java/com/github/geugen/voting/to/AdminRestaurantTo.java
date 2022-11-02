package com.github.geugen.voting.to;

import com.github.geugen.voting.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;


@Schema(title = "AdminRestaurantTo - DTO used by Admin Restaurant Controller")
@Value
@EqualsAndHashCode(callSuper = true)
public class AdminRestaurantTo extends NamedTo {

    @NotNull
    Address address;

    public AdminRestaurantTo(Integer id, String name, Address address) {
        super(id, name);
        this.address = address;
    }

    @Override
    public String toString() {
        return "AdminRestaurantTo:" + id + '[' + address.toString() + ']';
    }
}