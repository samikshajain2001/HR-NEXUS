package com.nri.ems.Exceptions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnAuthorizedAccessException extends RuntimeException{

    public UnAuthorizedAccessException(){
        super(String.format("Invalid Credentials"));
    }
    public UnAuthorizedAccessException(String msg){
        super(String.format(msg));
    }

}
