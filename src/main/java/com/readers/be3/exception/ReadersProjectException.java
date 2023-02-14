package com.readers.be3.exception;

import java.text.Format;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadersProjectException extends RuntimeException{

      private ErrorResponse errorResponse;


       @Override
       public String getMessage(){
        return String.format("%d  message: %s", errorResponse.getHttpStatus().value() ,errorResponse.getErrorMessage());
      }

      public HttpStatus getHttpStatus(){
        return this.errorResponse.getHttpStatus();
      }
  
}

