package com.sky.yb.user.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.*;

@Data
@Builder
@Document(collection = "users")
public class User {
    @MongoId
    private String id;
    @Indexed(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    private List<UserExternalProject> userExternalProjects;
    @DBRef
    private Set<Role> userRoles;

    @JsonIgnore
    public Collection<Role> getRoles() {
        return userRoles == null
                ? Collections.emptySet()
                : userRoles;
    }

    public void addExternalProjects(UserExternalProject projects) {
        if (userExternalProjects == null) {
            userExternalProjects = new LinkedList<>();
        }
        userExternalProjects.add(projects);
    }
}
