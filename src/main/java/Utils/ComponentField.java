package Utils;

import annotations.Name;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Set;

@Data
public class ComponentField {
    private FieldType fieldType;
    private String caption;
    private String canonicalName;
    private Field field;

    public ComponentField(Field field, Set<Class<?>> userClasses) {
        this.field = field;
        Name annotationName = field.getAnnotation(Name.class);
        caption = annotationName != null
                ? annotationName.value()
                : field.getName();

        canonicalName = field.getName();

        Class<?> fieldClass = field.getType();
        if (fieldClass.isEnum()) {
            fieldType = FieldType.ENUM;
        } else if (fieldClass.equals(String.class)) {
            fieldType = FieldType.STRING;
        } else if (fieldClass.isPrimitive()) {

            if (fieldClass.equals(boolean.class)) {
                fieldType = FieldType.BOOLEAN;
            } else if (fieldClass.equals(int.class) || fieldClass.equals(long.class) || fieldClass.equals(byte.class)) {
                fieldType = FieldType.INT;
            } else if (fieldClass.equals(float.class) || fieldClass.equals(double.class)){
                fieldType = FieldType.FLOAT;
            } else {
                fieldType = FieldType.CHAR;
            }
        } else if (userClasses.contains(fieldClass)) {
            fieldType = FieldType.USER_OBJECT;
        } else {
            fieldType = FieldType.UNDEFINED_OBJECT;
        }

    }
}
