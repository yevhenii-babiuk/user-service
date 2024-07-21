package com.sky.yb.user.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.*;

@Builder
@Document(collection = "users")
public class User {
    @Getter
    @Setter
    @MongoId
    private String id;

    @Getter
    @Setter
    @Indexed(unique = true)
    private String email;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private List<UserExternalProject> userExternalProjects;

    private Set<Role> userRoles;

    public void addExternalProjects(UserExternalProject projects) {
        if (userExternalProjects == null) {
            userExternalProjects = new LinkedList<>();
        }
        userExternalProjects.add(projects);
    }

    public Collection<Role> getUserRoles() {
        return userRoles == null
                ? Collections.emptySet()
                : Set.copyOf(userRoles);
    }

    public void addUserRoles(Role role) {
        if (userRoles == null) {
            userRoles = new HashSet<>();
        }
        userRoles.add(role);
    }

    public void removeUserRoles(Role role) {
        if (userRoles != null) {
            userRoles.remove(role);
        }
    }
}
