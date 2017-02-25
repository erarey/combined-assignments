package com.cooksys.ftd.assignments.file.model;

import java.io.Serializable;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "contact") 
@XmlType(propOrder = {"firstName","lastName","email","phoneNumber"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact {
	
	@XmlAttribute(name = "first-name")
    private String firstName;
	
	@XmlAttribute(name = "last-name")
    private String lastName;
	
    private String email;
    
    private String phoneNumber;
    
    //@XmlElement
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    //@XmlElement
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //@XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    //@XmlElement
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
