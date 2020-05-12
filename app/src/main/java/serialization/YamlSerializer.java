package serialization;

import Utils.ComponentField;
import Utils.ComponentGroup;
import Utils.FieldGenerator;
import annotations.Name;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("Yaml")
public class YamlSerializer implements Serializer {

    private String toYaml(Object object) {
        StringBuilder yamlValue = new StringBuilder("!" + object.getClass().getName());
        List<Field> fields = FieldGenerator.getAllFields(object.getClass());
        try {
            for (Field field : fields) {
                yamlValue.append("\r\n");

                field.setAccessible(true);

                if (field.getType().isPrimitive()) {
                    yamlValue
                            .append(field.getName())
                            .append(": ")
                            .append(field.get(object));
                } else if (String.class.equals(field.getType())) {
                    yamlValue
                            .append(field.getName())
                            .append(": \"")
                            .append(field.get(object))
                            .append("\"");

                } else if (field.getType().isEnum()) {
                    yamlValue
                            .append(field.getName())
                            .append(":")
                            .append("\r\n\t")
                            .append("!")
                            .append(field.getType().getName())
                            .append("\r\n\t")
                            .append("value: ")
                            .append(field.get(object));
                } else if (!field.getType().isPrimitive()) {
                    if (field.get(object) != null) {
                        String subValue = toYaml(field.get(object));
                        yamlValue
                                .append(field.getName())
                                .append(": \r\n\t")
                                .append(subValue.replaceAll("\r\n", "\r\n\t"));
                    } else {
                        yamlValue
                                .append(field.getName())
                                .append(": null");
                    }
                }

                field.setAccessible(false);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return yamlValue.toString();
    }

    private Object fromYaml(ArrayDeque<String> lines) {
        try {
            String className = lines.poll().substring(1);
            Class clazz = Class.forName(className);
            Object object = clazz.newInstance();
            ComponentGroup componentGroup = FieldGenerator.generateComponentGroup(object);
            List<Field> fields = FieldGenerator.getAllFields(clazz);
            if (clazz.isEnum()) {
                return Enum.valueOf(clazz, lines.poll().split(" ")[1]);
            }

            int i = 0;
            while (!lines.isEmpty()) {
                String line = lines.poll();
                String[] splitted = line.split(" ");
                Object value;

                String fieldName = splitted[0].substring(0, splitted[0].length() - 1);
                ComponentField compField = componentGroup.getFields().stream()
                        .filter(fl -> fl.getCanonicalName().equals(fieldName))
                        .findAny()
                        .get();
                Field field = compField.getField();
                if (splitted.length == 1) {
                    ArrayDeque<String> sublines = new ArrayDeque<>();
                    while ((lines.peek() != null) && lines.peek().startsWith("\t")) {
                        sublines.add(lines.poll().substring(1));
                    }
                    value = fromYaml(sublines);

                } else {

                    if (splitted[1].equals("null")) {
                        value = null;
                    } else if (splitted[1].startsWith("\"")) {
                        String argument = splitted[1];
                        value = argument.substring(1, argument.length() - 1);

                    } else {
                        String val = splitted[1];
                        Class fieldType = fields.get(i).getType();
                        value = parsePrimitive(val, fieldType);
                    }
                }

                field.setAccessible(true);
                field.set(object, value);
                field.setAccessible(false);
                i++;
            }
            return object;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object parsePrimitive(String value, Class clazz) {
        if (byte.class.equals(clazz)) {
            return Byte.parseByte(value);
        } else if (short.class.equals(clazz)) {
            return Short.parseShort(value);
        } else if (int.class.equals(clazz)) {
            return Integer.parseInt(value);
        } else if (long.class.equals(clazz)) {
            return Long.parseLong(value);
        } else if (float.class.equals(clazz)) {
            return Float.parseFloat(value);
        } else if (double.class.equals(clazz)) {
            return Double.parseDouble(value);
        } else if (char.class.equals(clazz)) {
            return value.charAt(0);
        } else if (boolean.class.equals(clazz)) {
            return Boolean.parseBoolean(value);
        } else {
            return null;
        }
    }

    @Override
    public void serialize(String filename, Object[] objects) {
        try (FileWriter writer = new FileWriter(new File(filename))) {
            writer.write(Arrays.stream(objects)
                    .map(this::toYaml).collect(Collectors.joining("\n---\n")));
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
                    .collect(Collectors.joining("\n"))
                    .split("\n---\n");
            return Arrays.stream(strings)
                    .map(string -> fromYaml(new ArrayDeque<>(Arrays.asList(string.split("\n")))))
                    .toArray(Object[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
