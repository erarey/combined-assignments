package com.cooksys.ftd.assignments.file.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

//@XmlType(propOrder = {"location", "startDate", "instructor", "students"})
public class Session {
	@XmlAttribute
    private String location;
	@XmlAttribute(name = "start-date")
    private String startDate;
	
    private Instructor instructor;
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    private List<Student> students;
    
    
    public Session()
    {
    	students = new ArrayList<>();
    }
    
    
    
//@XmlElement
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
//@XmlElement
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
//@XmlElement
    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
//@XmlElement
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
