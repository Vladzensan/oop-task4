package serialization;

import java.util.Map;

public interface SerializerService {
    Map<String, Class<? extends Serializer>> getSerializers();
}
