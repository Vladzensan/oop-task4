package Utils;

import annotations.Entity;
import annotations.Name;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserClassesServiceImpl implements UserClassesService {
    private Set<Class<?>> entityClasses;
    private Set<Class<?>> allClasses;


    // somehow load from property file
    private UserClassesServiceImpl() {
        Reflections reflections = new Reflections("model");

        allClasses = reflections.getTypesAnnotatedWith(Entity.class);
        entityClasses = allClasses.stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .collect(Collectors.toSet());

    }

    @Override
    public Set<Class<?>> getEntityClasses() {
        return entityClasses;
    }

    @Override
    public Set<Class<?>> getAllUserClasses() {
        return allClasses;
    }

    @Override
    public Map<String, Class<?>> getClassesCaptions() {
        Map<String, Class<?>> namedClasses = new HashMap<>();

        for (Class<?> userClass : entityClasses) {
            Name annotationName = userClass.getAnnotation(Name.class);
            String name = annotationName != null
                    ? annotationName.value()
                    : userClass.getName();
            namedClasses.put(name, userClass);
        }

        return namedClasses;
    }


    public static UserClassesServiceImpl getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        static UserClassesServiceImpl instance = new UserClassesServiceImpl();

    }
}
