package com.cydeo.repository;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
 @Query("Select Count(t) From Task t where t.project.projectCode = ?1 and t.taskStatus<>'COMPLETE'")
 int totalNonCompletedTask(String projectCode);

    @Query(value = "Select count(*) from tasks t Join projects p On t.project_id=p.id where p.project_code=?1 And t.task_status='COMPLETE'", nativeQuery = true)
    int totalCompletedTask(String projectCode);

    List<Task> findAllByProject(Project project);// in derived we can pass entity, in others ID or Code
    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);
    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);


}
