package com.fiserv.hibernatesearchdemo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private Set<Post> posts;
}
