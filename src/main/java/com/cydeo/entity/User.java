package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
//@Where(clause = "is_deleted=false")
//Any repository which uses THIS ENTITY will add, concate  @where condition to EACH query in repository
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;
    @ManyToOne//many users can have one role. Create annotation on many side, because otherwise Spring will create extra table
    private Role role;
    @Enumerated(EnumType.STRING)
    private Gender gender;


}
