package com.banking.user.domain.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final UserStatus status;
    private final Set<RoleName> roles;
    private final Instant createdAt;
    private final Instant updatedAt;

    public User(UUID id, String firstname, String lastname, String email, UserStatus status, Set<RoleName> roles, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.status = status;
        this.roles = Set.copyOf(roles);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createClient(String firstname, String lastname, String email) {
        Instant now = Instant.now();
        return new User(UUID.randomUUID(), firstname, lastname, email, UserStatus.ACTIVE, Set.of(RoleName.ROLE_CLIENT), now, now);
    }

    public UUID id() { return id; }
    public String firstname() { return firstname; }
    public String lastname() { return lastname; }
    public String email() { return email; }
    public UserStatus status() { return status; }
    public Set<RoleName> roles() { return roles; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
