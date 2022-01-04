package com.ender.globalmarket.user;

import java.util.UUID;

public class Storage {
    public static UUID user;

    public void loadUser(UUID uuid) {
        user = uuid;
    }
}
