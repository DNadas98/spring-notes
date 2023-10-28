package com.dnadas.spring_notes.exception;

public class DaoException extends RuntimeException{
  public DaoException(String message) {
    super(message);
  }
}
