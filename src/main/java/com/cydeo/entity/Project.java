package com.cydeo.entity;

import com.cydeo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="projects")
@Where(clause = "is_deleted=false")
public class Project extends BaseEntity {

@Column(unique = true)//will not allow us to create another project with same id
    private String projectCode;
    private String projectName;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;
    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status projectStatus;

    private String projectDetail;

    @ManyToOne(fetch=FetchType.LAZY)// one manager many projects
    @JoinColumn(name="manager_id")
    private User assignedManager;

    private Boolean isDeleted=false;

}
