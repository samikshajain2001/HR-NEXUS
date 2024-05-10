package com.nri.ems.Exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyInputException extends RuntimeException{
    private String field;

    public EmptyInputException(String field) {
        super(String.format("%s is Empty. Please fill the Information.", field));
        this.field = field;
    }

}
