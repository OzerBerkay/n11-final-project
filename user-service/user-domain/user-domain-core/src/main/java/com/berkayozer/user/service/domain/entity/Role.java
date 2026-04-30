package com.berkayozer.user.service.domain.entity;

import com.berkayozer.domain.entity.BaseEntity;
import com.berkayozer.domain.valueobject.RoleId;

public class Role extends BaseEntity<RoleId> {
    private final String name;

    private Role(Builder builder) {
        super.setId(builder.roleId);
        name = builder.name;
    }

    public String getName() { return name; }

    public static final class Builder {
        private RoleId roleId;
        private String name;

        private Builder() {}
        public static Builder builder() { return new Builder(); }

        public Builder id(RoleId val) { roleId = val; return this; }
        public Builder name(String val) { name = val; return this; }
        public Role build() { return new Role(this); }
    }
}