package Utils;

import annotations.Name;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class FieldGenerator {
    private static Set<Class<?>> userClasses = UserClassesServiceImpl.getInstance().getEntityClasses();


    public static ComponentGroup generateComponentGroup(Object obj) {
        Class<?> someClass = obj.getClass();
        boolean isInstantiableClass = !(Modifier.isAbstract(someClass.getModifiers())
                || someClass.isInterface());

        if (!isInstantiableClass) {
            System.out.println("Class is abstract");
            return null;
        }

        ComponentGroup componentGroup = new ComponentGroup(obj, getClassAnnotation(obj));
        if (!userClasses.contains(someClass)) {
            componentGroup.setDescribable(false);
        } else {
            componentGroup.setDescribable(true);
            generate(obj.getClass(), componentGroup);
        }

        return componentGroup;
    }

    private static void generate(Class<?> someClass, ComponentGroup componentGroup) {

        Field[] fields = someClass.getDeclaredFields();
        List<ComponentField> componentFields = new ArrayList<>(fields.length);

        for (Field field : fields) {
            //System.out.println(field.getAnnotation(annotations.Name.class).value() + ": " + field.getName());

            componentFields.add(new ComponentField(field, userClasses));
        }

        componentGroup.getFields().addAll(componentFields);

        Class<?> superClass = someClass.getSuperclass();
        if (superClass != null && UserClassesServiceImpl.getInstance().getAllUserClasses().contains(superClass)) {
            generate(superClass, componentGroup);
        }

        //System.out.println(someClass.getName());

    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new LinkedList<>(Arrays.asList(clazz.getDeclaredFields()));

        Class<?> superClass = clazz;
        Set<Class<?>> userClasses = UserClassesServiceImpl.getInstance().getAllUserClasses();
        while ((superClass = superClass.getSuperclass()) != null && userClasses.contains(superClass)) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }

        return fields;
    }


    private static String getClassAnnotation(Object object) {
        Name annotationName = object.getClass().getAnnotation(Name.class);
        return annotationName != null
                ? annotationName.value()
                : object.getClass().getName();
    }


}
