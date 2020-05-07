import base32.Base32Encoder;
import main.java.service.ObjectEncoder;

module base32codec {
    requires plugin;
    requires commons.codec;
    provides ObjectEncoder with Base32Encoder;
}