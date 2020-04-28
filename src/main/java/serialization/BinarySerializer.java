package serialization;

import annotations.Name;

import java.io.*;

@Name("Binary")
public class BinarySerializer implements Serializer {
    @Override
    public void serialize(String fileName, Object[] objects) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(objects);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public Object[] deserialize(String fileName) {
        Object[] objects = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            objects = (Object[])in.readObject();
            in.close();
            fileIn.close();

        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return objects;
    }
}
