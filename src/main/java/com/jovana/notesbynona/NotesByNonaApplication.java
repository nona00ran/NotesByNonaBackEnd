package com.jovana.notesbynona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotesByNonaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesByNonaApplication.class, args);
    }

}
