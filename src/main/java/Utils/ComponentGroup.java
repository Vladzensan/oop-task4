package Utils;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
public class ComponentGroup {
    private boolean isDescribable;
    private String caption;
    private Object target;
    private List<ComponentField> fields = new ArrayList<>();

    public ComponentGroup(Object target, String caption) {
        this.caption = caption;
        this.target = target;
    }

    public void removeLink(Object object) throws IllegalAccessException {
        for (ComponentField componentField : fields) {
            Field field = componentField.getField();
            field.setAccessible(true);
            if (field.get(target) == object) {
                field.set(target, null);
            }

            field.setAccessible(false);
        }
    }
}
