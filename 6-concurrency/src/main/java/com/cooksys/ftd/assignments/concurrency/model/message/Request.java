package com.cooksys.ftd.assignments.concurrency.model.message;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {

    @XmlValue
    private RequestType type;

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }
    
    public Request(){}
    
    public Request(RequestType type)
    {
    	this.type = type;
    }
}
