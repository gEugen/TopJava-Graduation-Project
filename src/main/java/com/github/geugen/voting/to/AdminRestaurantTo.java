package com.github.geugen.voting.to;

import com.github.geugen.voting.HasIdAndEmail;
import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.util.validation.NoHtml;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Value
@EqualsAndHashCode(callSuper = true)
public class AdminRestaurantTo extends NamedTo implements HasIdAndEmail {
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String email;

    public AdminRestaurantTo(Integer id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    public AdminRestaurantTo(Restaurant restaurant) {
        super(restaurant.getId(), restaurant.getName());
        this.email = restaurant.getEmail();
    }

    @Override
    public String toString() {
        return "AdminRestaurantTo:" + id + '[' + email + ']';
    }
}
