import main.java.base64.Base64Encoder;
import main.java.service.ObjectEncoder;

module base64codec {
    requires plugin;
    provides ObjectEncoder with Base64Encoder;
}