package com.cydeo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isDeleted=false;
 @Column(nullable = false,updatable = false)
    private LocalDateTime insertDateTime;// the field cannot be null/ whenever update, do not do action on this field
 @Column(nullable = false,updatable = false)
    private Long insertUserId;
@Column(nullable = false)
    private LocalDateTime lastUpdateDateTime;
@Column(nullable = false)
    private Long lastUpdateUserId;
    @PrePersist// will be executed every time you create a user
    private void onPrePersist(){// method to initialize those fields automatically
        this.insertDateTime=LocalDateTime.now();
        this.lastUpdateDateTime=LocalDateTime.now();
        this.insertUserId=1L;//hardcoded before security
        this.lastUpdateUserId=1L;
    }
    @PreUpdate//will be executed every time you do any updates
    private void onPreUpdate(){// for later updates ingo that will be saved to keep track on changes
        this.lastUpdateDateTime=LocalDateTime.now();
        this.lastUpdateUserId=1L;
    }


}
