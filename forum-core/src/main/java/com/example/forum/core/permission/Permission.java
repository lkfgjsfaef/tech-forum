package com.example.forum.core.permission;

public @interface Permission {
    UserRole role() default UserRole.LOGIN_USER;
}
