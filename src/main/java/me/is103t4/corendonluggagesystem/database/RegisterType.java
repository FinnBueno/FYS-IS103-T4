package me.is103t4.corendonluggagesystem.database;

import java.util.Arrays;
import java.util.Optional;

public enum RegisterType {

    LOST(0), FOUND(1), DAMAGED(2), FOUND_DESTROYED(3);

    private final int id;

    RegisterType(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public RegisterType fromId(int id) {
        Optional<RegisterType> optional = Arrays.stream(values()).filter(v -> v.getId() == id).findFirst();
        return optional.orElse(null);
    }
}
