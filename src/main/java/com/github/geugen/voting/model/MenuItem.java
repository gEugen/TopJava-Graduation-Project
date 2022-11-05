package com.github.geugen.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Table(
        name = "menu_item",
        indexes = {
                @Index(name = "uk_registered_name_restaurant_idx", columnList = "registered, name, restaurant_id", unique = true),
                @Index(name = "registered_restaurant_idx", columnList = "registered, restaurant_id"),
                @Index(name = "restaurant_menu_item_idx", columnList = "restaurant_id, id")
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class MenuItem extends NamedEntity {

    @Column(name = "price")
    @Range(min = 1)
    private long price;

    @Column(name = "registered", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate registered = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public MenuItem(Integer id, String name, long price) {
        super(id, name);
        this.price = price;
    }
}
