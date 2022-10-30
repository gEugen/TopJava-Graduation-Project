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
        name = "menu_item", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name =
        "uk_restaurant_name"),
        indexes = {
                @Index(name = "restaurant_date_idx", columnList = "restaurant_id, date"),
                @Index(name = "dish_restaurant_idx", columnList = "id, restaurant_id")
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class MenuItem extends NamedEntity {

    @Column(name = "date", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date = LocalDate.now();

    @Column(name = "price")
    @Range(min = 1)
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public MenuItem(Integer id, String name, long price) {
        super(id, name);
        this.price = price;
    }
}
