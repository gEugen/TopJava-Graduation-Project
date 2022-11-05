package com.github.geugen.voting.model;

import com.github.geugen.voting.util.validation.NoHtml;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "address", indexes = @Index(name = "unique_address_idx", columnList = "city, street, building_number", unique = true))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {

    @Column(name = "city", nullable = false)
    @NotBlank
    @NoHtml
    private String city;

    @Column(name = "street", nullable = false)
    @NotBlank
    @NoHtml
    private String street;

    @Column(name = "building_number", nullable = false)
    @Range(min = 1)
    private int buildingNumber;

    public Address(Integer id, String city, String street, int buildingNumber) {
        super(id);
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
    }

    @Override
    public String toString() {
        return "Address:" + id + '[' + city + ", " + street + ", " + buildingNumber + ']';
    }
}