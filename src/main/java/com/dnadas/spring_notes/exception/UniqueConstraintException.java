package com.dnadas.spring_notes.exception;

public class UniqueConstraintException
  extends DaoException {
  private final String fieldName;
  private final String value;

  public UniqueConstraintException(String message,String fieldName, String value) {
    super(message);
    this.fieldName=fieldName;
    this.value = value;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getValue() {
    return value;
  }
}
