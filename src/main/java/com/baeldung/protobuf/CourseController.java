package com.baeldung.protobuf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.protobuf.BaeldungTraining.Course;

import java.awt.*;

@RestController
public class CourseController {

    @Autowired
    CourseRepository courseRepo;

    @GetMapping(value = "/courses/{id}", produces = "application/x-protobuf")
    Course customer(@PathVariable Integer id) {
        return courseRepo.getCourse(id);
    }
}
