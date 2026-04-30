package com.berkayozer.user.service.domain.entity;

import com.berkayozer.domain.entity.BaseEntity;
import com.berkayozer.user.service.domain.valueobject.AddressId;

public class Address extends BaseEntity<AddressId> {
    private final String street;
    private final String city;
    private final String postalCode;

    private Address(Builder builder) {
        super.setId(builder.addressId);
        street = builder.street;
        city = builder.city;
        postalCode = builder.postalCode;
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }

    public static final class Builder {
        private AddressId addressId;
        private String street;
        private String city;
        private String postalCode;

        private Builder() {}
        public static Builder builder() { return new Builder(); }

        public Builder id(AddressId val) { addressId = val; return this; }
        public Builder street(String val) { street = val; return this; }
        public Builder city(String val) { city = val; return this; }
        public Builder postalCode(String val) { postalCode = val; return this; }
        public Address build() { return new Address(this); }
    }
}