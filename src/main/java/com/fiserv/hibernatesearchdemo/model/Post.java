package com.fiserv.hibernatesearchdemo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Entity
@Indexed
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Field
    private String content;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private User user;
}
