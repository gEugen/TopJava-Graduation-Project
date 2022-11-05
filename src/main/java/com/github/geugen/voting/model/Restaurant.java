package com.github.geugen.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;


@Entity
@Table(
        name = "restaurant",
        indexes = @Index(name = "restaurant_unique_name_address_idx", columnList = "name, address_id", unique = true))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    @Valid
    private Address address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonIgnore
    private List<MenuItem> menuItems;

    public Restaurant(Integer id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.address);
    }

    public Restaurant(Integer id, String name, Address address, List<MenuItem> menuItems) {
        super(id, name);
        this.address = address;
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + address.toString() + ']';
    }
}
