package com.sky.yb.user.service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
public class UserExternalProject {
    @MongoId
    private String id;
    private String name;
}
