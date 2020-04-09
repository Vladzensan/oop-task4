package Utils;

import java.util.Map;
import java.util.Set;

public interface UserClassesService {
    Set<Class<?>> getEntityClasses();

    Set<Class<?>> getAllUserClasses();

    Map<String, Class<?>> getClassesCaptions();
}
