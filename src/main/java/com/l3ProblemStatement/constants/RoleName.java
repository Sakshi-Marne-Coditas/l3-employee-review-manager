package com.l3ProblemStatement.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleName {
    ADMIN("Admin"),
    MANAGER("Manager"),
    EMPLOYEE("Employee");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RoleName fromValue(String value) {
        for (RoleName roleName : values()) {
            if (roleName.getValue().equalsIgnoreCase(value)) {
                return roleName;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + value);
    }
    }




