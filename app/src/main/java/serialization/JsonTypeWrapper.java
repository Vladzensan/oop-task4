package serialization;

import lombok.Data;

@Data
public class JsonTypeWrapper {
    private String jsonValue;
    private String className;

    public JsonTypeWrapper(String className, String jsonValue) {
        this.jsonValue = jsonValue;
        this.className = className;
    }
}
