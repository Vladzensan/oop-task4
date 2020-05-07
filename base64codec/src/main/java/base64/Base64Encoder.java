package main.java.base64;

import main.java.service.ObjectEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class Base64Encoder implements ObjectEncoder {
    @Override
    public void encode(File file) {
        try {
            byte[] encodedSequence = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
            Files.write(file.toPath(), encodedSequence);
            file.renameTo(new File(file.getCanonicalPath() + ".b64"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decode(File file) {
        try {
            byte[] decodedSequence = Base64.getDecoder().decode(Files.readAllBytes(file.toPath()));
            Files.write(file.toPath(), decodedSequence);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
