package com.springbootapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "roles")
public class Role {
    @Id
    String name ;
    String description;
    @ManyToMany
    Set<Permission> permissions;


}
