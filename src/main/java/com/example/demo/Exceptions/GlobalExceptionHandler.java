
  package com.example.demo.Exceptions;
  
  import org.springframework.http.HttpStatus; import
  org.springframework.http.ResponseEntity; import
  org.springframework.web.bind.annotation.ControllerAdvice; import
  org.springframework.web.bind.annotation.ExceptionHandler;
  
  /**
   * GlobalExceptionHandler handles application-wide exceptions
   * and returns appropriate HTTP responses with meaningful messages.
   */
  @ControllerAdvice 
  public class GlobalExceptionHandler {
  
 //Handles custom exception ResourceNotFoundException and returns 404 status code
  @ExceptionHandler(ResourceNotFoundException.class) 
  public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) { 
	  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  
  } }
 