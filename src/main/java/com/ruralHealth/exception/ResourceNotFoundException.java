package com.ruralHealth.exception;

public class ResourceNotFoundException extends RuntimeException {
    private long fieldId;
    private String field;
    private String fieldName;
    private String resourceName;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String field, String fieldName, String resourceName) {
        super(String.format( "%s not found with %s : %s ", field, fieldName, resourceName ));
        this.field = field;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException(String resourceName, String fieldName,long fieldId ) {
        super(String.format( "%s not found with %s : %s ", resourceName, fieldName, fieldId ));
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }
}
