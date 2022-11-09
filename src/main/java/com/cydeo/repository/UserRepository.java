package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
// get user by username
    User findByUserName(String username);
   @Transactional
       //if there are more then one step to perform action,
       // it will be committed only when both steps are successfully performed.
       // If not, it rolls back to how to was before starting the actions.
       // No data will be lost

   void deleteByUserName(String username);

}
