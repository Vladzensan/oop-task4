package serialization;

import annotations.Name;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;


@Name("Json")
public class JsonSerializer implements Serializer {

    private Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private final String SEPARATOR = "---";

    @Override
    public void serialize(String filename, Object[] objects) {
        try (FileWriter writer = new FileWriter(new File(filename
        ))) {
            writer.write(Arrays.stream(objects)
                    .map(object -> gson.toJson(new JsonTypeWrapper(object.getClass().getName(), gson.toJson(object))))
                    .collect(Collectors.joining("\n" + SEPARATOR + "\n")));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object[] deserialize(String fileName) {
        try (FileReader reader = new FileReader(new File(fileName))) {
            String[] strings = new BufferedReader(reader)
                    .lines()
                    .collect(Collectors.joining(""))
                    .split(SEPARATOR);
            Object[] objects = new Object[strings.length];
            for (int i = 0; i < strings.length; i++) {
                JsonTypeWrapper jsonWrapper = gson.fromJson(strings[i], JsonTypeWrapper.class);
                objects[i] = gson.fromJson(jsonWrapper.getJsonValue(), Class.forName(jsonWrapper.getClassName()));
            }
            return objects;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
