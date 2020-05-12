package serialization;

public interface Serializer {
    void serialize(String filename, Object[] objects);

    Object[] deserialize(String fileName);
}
