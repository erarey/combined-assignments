package com.cooksys.ftd.assignments.file.model;

import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlType(propOrder = {"location", "startDate", "instructor", "students"})
public class Session {
    private String location;
    private String startDate;
    private Instructor instructor;
    private List<Student> students;
    
@XmlElement
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
@XmlElement
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
@XmlElement
    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
@XmlElement
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
