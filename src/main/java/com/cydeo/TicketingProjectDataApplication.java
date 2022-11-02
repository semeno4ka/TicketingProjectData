package com.cydeo;

import com.cydeo.dto.RoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication// contains @Configuration
public class TicketingProjectDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataApplication.class, args);
    }


    //Adding bean from outside class to container
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

}
