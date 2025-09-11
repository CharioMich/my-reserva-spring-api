package gr.aueb.cf.myreserva.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER,
    ADMIN,
    SUPER_ADMIN;


    /**
     * Tells Jackson to use this method when deserializing JSON. Case-insensitive conversion achieved because Enums are UPPERCASE
     */
    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }


    /**
     * Tells Jackson to use this method when serializing JSON. Role.USER → "user" because our frontend in React expects lowercase
     */
    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
