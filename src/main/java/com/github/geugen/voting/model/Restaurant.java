package com.github.geugen.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.geugen.voting.util.validation.NoHtml;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.github.geugen.voting.HasIdAndEmail;
import com.github.geugen.voting.to.AdminRestaurantTo;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Table(name = "restaurants", indexes = @Index(name = "restaurant_unique_email_idx", columnList = "email"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasIdAndEmail {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @Schema(hidden = true)
    private List<Dish> dishes;

    @OneToMany(mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Vote> vote;

    public Restaurant(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.email);
    }

    public Restaurant(Integer id, String name, String email, List<Dish> dishes) {
        super(id, name);
        this.email = email;
        this.dishes = dishes;
    }

    public Restaurant(AdminRestaurantTo restaurantTo) {
        super(restaurantTo.getId(), restaurantTo.getName());
        this.email = restaurantTo.getEmail();
    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + email + ']';
    }
}
