package com.ecommerce.project.exception;

public class ResourceNotFoundException extends RuntimeException {
   String resourceName;
   String field;
   String fieldName;
   long fieldId;

   public ResourceNotFoundException(String resourceName, String field, String fieldName) {
       super(String.format("%s not found for %s :%s",resourceName,field,fieldName));
       this.resourceName = resourceName;
       this.field = field;
       this.fieldName = fieldName;

   }
   public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
       super(String.format("%s not found for %s :%s",resourceName,field,fieldId));
       this.resourceName = resourceName;
       this.field = field;
       this.fieldId = fieldId;
   }

}
