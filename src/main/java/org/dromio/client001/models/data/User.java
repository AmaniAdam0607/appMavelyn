package org.dromio.client001.models.data;

import java.time.LocalDateTime;

/**
 * Model everything about a user here
 * */
public class User {
    String userId;
    String username; // used for login
    String passwordHash;
    String roleId; // a user can have a role
    LocalDateTime createdAt;
    boolean isBlocked;
}
