package main.java.service;

import java.io.File;

public interface ObjectEncoder {
    void encode(File file);

    void decode(File file);
}
