package com.example.rstroybackend.entity.views;

import java.util.HashMap;
import java.util.Map;

public class SecurityViews {
    public static final Map<String, Class> MAPPING = new HashMap<>();

    static {
        MAPPING.put("ROLE_ANONYMOUS", Anonymous.class);
        MAPPING.put("ROLE_ADMIN", Admin.class);
        MAPPING.put("ROLE_USER", User.class);
    }

    public static class Anonymous {}
    public static class User extends Anonymous {}
    public static class Admin extends User {}
}
