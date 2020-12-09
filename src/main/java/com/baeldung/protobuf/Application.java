package com.baeldung.protobuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.baeldung.protobuf.BaeldungTraining.Course;
import com.baeldung.protobuf.BaeldungTraining.Student;
import com.baeldung.protobuf.BaeldungTraining.NullableString;
import com.baeldung.protobuf.BaeldungTraining.Student.PhoneNumber;
import com.baeldung.protobuf.BaeldungTraining.Student.PhoneType;
import com.baeldung.protobuf.BaeldungTraining.ComplexObject;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    RestTemplate restTemplate(ProtobufHttpMessageConverter hmc) {
        return new RestTemplate(Arrays.asList(hmc));
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Bean
    public CourseRepository createTestCourses() throws InvalidProtocolBufferException {
        Map<Integer, Course> courses = new HashMap<>();

        NullableString nullableNull = NullableString.newBuilder().setNull(NullValue.NULL_VALUE).build();

        NullableString nullableData = NullableString.newBuilder().setData("bogdan").build();

        Course course1 = Course.newBuilder()
                .setId(1)
                .setCourseName("REST with Spring")
                .setCourseAuthor(nullableNull)
                .setCourseAssistant(StringValue.of("Bogdan"))
                .addAllStudent(createTestStudents())
                .build();

        Course course2 = Course.newBuilder()
                .setId(2)
                .setCourseName("Learn Spring Security")
                .setCourseAuthor(nullableData)
                .addAllStudent(new ArrayList<>())
                .build();

        Struct structKeyAndValue = Struct.newBuilder()
                .putFields("structKeyAndValue", Value.newBuilder().setStringValue("bogdan").build())
                .build();

        ComplexObject complex1 = ComplexObject.newBuilder()
                .putMap("username", "bogdan")
                .setStruct(structKeyAndValue)
                .build();

        Map<String, Value> mapValue = new HashMap<>();
        mapValue.put("structFromMap", Value.newBuilder().setStringValue("bogdan").build());

        Struct structMap = Struct.newBuilder().putAllFields(mapValue).build();

        Map<String, String> mapString = new HashMap<>();
        mapString.put("usernameMap", "bogdan");

        ComplexObject complex2 = ComplexObject.newBuilder()
                .putAllMap(mapString)
                .setStruct(structMap)
                .build();

        Struct.Builder structJson = Struct.newBuilder();
        JsonFormat.parser().merge("{\"structFromJson\":\"bogdan\"}", structJson);

        ComplexObject complex3 = ComplexObject.newBuilder()
                .putAllMap(mapString)
                .setStruct(structJson)
                .build();

        ComplexObject complex4 = ComplexObject.newBuilder()
                .putAllMap(mapString)
                .setStruct(structJson)
                .setAny(Any.pack(complex3))
                .build();

        Course course3 = Course.newBuilder()
                .setId(3)
                .setCourseName("Complex course")
                .setCourseAuthor(nullableData)
                .addAllStudent(createTestStudents())
                .addAllComplexObject(Arrays.asList(complex1, complex3, complex2, complex4))
                .build();

        courses.put(course1.getId(), course1);
        courses.put(course2.getId(), course2);
        courses.put(course3.getId(), course3);

        return new CourseRepository(courses);
    }

    private List<Student> createTestStudents() {
        PhoneNumber phone1 = createPhone("123456", PhoneType.MOBILE);
        Student student1 = createStudent(1, "John", "Doe", "john.doe@baeldung.com", Arrays.asList(phone1));

        PhoneNumber phone2 = createPhone("234567", PhoneType.LANDLINE);
        Student student2 = createStudent(2, "Richard", "Roe", "richard.roe@baeldung.com", Arrays.asList(phone2));

        PhoneNumber phone3_1 = createPhone("345678", PhoneType.MOBILE);
        PhoneNumber phone3_2 = createPhone("456789", PhoneType.LANDLINE);
        Student student3 = createStudent(3, "Jane", "Doe", "jane.doe@baeldung.com", Arrays.asList(phone3_1, phone3_2));

        return Arrays.asList(student1, student2, student3);
    }

    private Student createStudent(int id, String firstName, String lastName, String email, List<PhoneNumber> phones) {
        return Student.newBuilder().setId(id).setFirstName(firstName).setLastName(lastName).setEmail(email).addAllPhone(phones).build();
    }

    private PhoneNumber createPhone(String number, PhoneType type) {
        return PhoneNumber.newBuilder().setNumber(number).setType(type).build();
    }
}