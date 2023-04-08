package com.food.ordering.system.order.service.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class StreetAddress {
    private final UUID id;
    private final String street;
    private final String postcode;
    private final String city;

    public StreetAddress(UUID id, String street, String postcode, String city) {
        this.id = id;
        this.street = street;
        this.postcode = postcode;
        this.city = city;
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetAddress that = (StreetAddress) o;
        return street.equals(that.street) && postcode.equals(that.postcode) && city.equals(that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postcode, city);
    }
}
