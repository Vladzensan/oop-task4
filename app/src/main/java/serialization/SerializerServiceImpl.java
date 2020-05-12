package serialization;

import annotations.Name;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SerializerServiceImpl implements SerializerService {

    @Override
    public Map<String, Class<? extends Serializer>> getSerializers() {
        Map<String, Class<? extends Serializer>> nameSerializers = new HashMap<>();
        Reflections reflections = new Reflections("serialization");

        Set<Class<? extends Serializer>> serializers = reflections.getSubTypesOf(Serializer.class);
        for (Class<? extends Serializer> clazz : serializers) {
            Name annotationName = clazz.getAnnotation(Name.class);
            String name = annotationName != null
                    ? annotationName.value()
                    : clazz.getName();
            nameSerializers.put(name, clazz);
        }

        return nameSerializers;

    }
}
