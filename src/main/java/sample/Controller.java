package sample;

import Utils.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

    private Map<String, ComponentGroup> objects = new HashMap<>();
    private Map<String, Class<?>> classes = new HashMap<>();
    private Object currentObject;
    private ComponentGroup componentGroup;

    @FXML
    private Label lblObjType;



    @FXML
    private ScrollPane componentLayout;

    @FXML
    private TextField editObjName;

    @FXML
    private GridPane gridComponents;

    @FXML
    private Button btnDeleteObj;

    @FXML
    private Button btnSaveObj;

    @FXML
    private Button btnCreateObj;

    @FXML
    private ComboBox<String> cboxCurrent;

    @FXML
    private ComboBox<String> cboxCreate;

    @FXML
    void onCreateObject(ActionEvent event) throws IllegalAccessException, InstantiationException {
        String name = editObjName.getText();
        if (objects.containsKey(name)) {
            alert("Object with such name already exists");
            return;
        }
        if (cboxCreate.getValue() == null || cboxCreate.getValue().isEmpty()) {
            alert("Choose the type of the object");
            return;
        }

        Object obj = classes.get(cboxCreate.getValue()).newInstance();
        ComponentGroup compGroup = FieldGenerator.generateComponentGroup(obj);
        objects.put(name, compGroup);

        updateCboxCurrent();

    }


    @FXML
    void onCurrentObjectChange(ActionEvent event) {
        String currentObjName;
        if ((currentObjName = cboxCurrent.getValue()) != null) {
            currentObject = objects.get(currentObjName).getTarget();
            componentGroup = FieldGenerator.generateComponentGroup(currentObject);
            updateControls(componentGroup);
            lblObjType.setText("Object type: " + componentGroup.getCaption());
        }
    }

    @FXML
    void onDeleteObject(ActionEvent event) throws IllegalAccessException {
        String objName = cboxCurrent.getValue();
        if(objName == null) {
            alert("Chose current object");
            return;
        }

        Object object = objects.get(objName).getTarget();

        for (Map.Entry<String, ComponentGroup> entry :
                objects.entrySet()) {
           entry.getValue().removeLink(object);
        }

        objects.remove(objName);
        updateCboxCurrent();




    }

    @FXML
    void onSaveObj(ActionEvent event) throws IllegalAccessException {

        for (Node node : gridComponents.getChildren()) {
            if (!(node instanceof Label) && !(node instanceof Button)) {
                String fieldName = node.getId();
                ComponentField compField = componentGroup.getFields().stream()
                        .filter(fl -> fl.getCanonicalName().equals(fieldName))
                        .findAny()
                        .get();
                Field field = compField.getField();
                field.setAccessible(true);
                String value;
                if ((node instanceof CheckBox)) {
                    boolean boolValue = ((CheckBox) node).isSelected();

                    field.set(currentObject, boolValue);
                } else if (node instanceof TextField) {
                    value = ((TextField) node).getText();
                    field.set(currentObject, toObject(field.getType(), value, compField.getFieldType()));
                } else if (node instanceof ComboBox) {
                    Enum<?> value1 = ((ComboBox<Enum<?>>) node).getValue();
                    field.set(currentObject, value1);
                } else if (node instanceof ChoiceBox) {
                    String objName = ((ChoiceBox<String>) node).getValue();
                    Object target = null;
                    if (!objName.equals("No")) {
                        target = objects.get(objName).getTarget();
                    }
                    field.set(currentObject, target);
                }
                field.setAccessible(false);
            }
        }


    }

    @FXML
    public void initialize() {
        classes = UserClassesServiceImpl.getInstance().getClassesCaptions();

        cboxCreate.setItems(FXCollections.observableArrayList(classes.keySet()));


    }

    private void updateCboxCurrent() {
        cboxCurrent.setItems(FXCollections.observableArrayList(objects.keySet()));
    }

    private void updateControls(ComponentGroup group) {
        gridComponents.getChildren().clear();

        int rowCount = 0;
        for (ComponentField componentField :
                group.getFields()) {
            Node node = null;
            try {
                componentField.getField().setAccessible(true);
                switch (componentField.getFieldType()) {
                    case STRING:
                    case CHAR:
                    case FLOAT:
                    case INT:
                        TextField txtNode = new TextField();

                        Object value = componentField.getField().get(group.getTarget());

                        String strValue = value != null ? value.toString() : "";
                        txtNode.setText(strValue);

                        node = txtNode;
                        break;
                    case ENUM:
                        ComboBox<Enum<?>> cbox = new ComboBox<>();
                        Enum<?>[] enumValues = getEnumValues(componentField.getField().getType());
                        cbox.setValue((Enum<?>) componentField.getField().get(group.getTarget()));
                        cbox.setItems(FXCollections.observableArrayList(enumValues));
                        node = cbox;
                        break;
                    case BOOLEAN:
                        CheckBox checkBox = new CheckBox();
                        checkBox.setSelected((boolean) componentField.getField().get(group.getTarget()));
                        node = checkBox;

                        break;
                    case USER_OBJECT:
                        ChoiceBox<String> choiceBox = new ChoiceBox<>();
                        List<String> objNames = new ArrayList<>();
                        String targetValue = null;
                        for (Map.Entry<String, ComponentGroup> entry :
                                objects.entrySet()) {
                            if (entry.getValue().getTarget().getClass().equals(componentField.getField().getType())) {
                                objNames.add(entry.getKey());
                            }
                            if (entry.getValue().getTarget() == componentField.getField().get(currentObject)) {
                                targetValue = entry.getKey();
                            }
                        }
                        objNames.add("No");
                        choiceBox.setValue(targetValue);
                        choiceBox.setItems(FXCollections.observableArrayList(objNames));
                        node = choiceBox;
                        break;
                    case UNDEFINED_OBJECT:
                        node = new Label("Undefined object");
                        break;

                }


            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            //node.get
            componentField.getField().setAccessible(false);
            node.setId(componentField.getCanonicalName());
            gridComponents.add(new Label(componentField.getCaption()), 0, rowCount);
            gridComponents.add(node, 1, rowCount);
            rowCount++;

        }


    }

    private static <E extends Enum> E[] getEnumValues(Class<?> enumClass)
            throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method method = enumClass.getDeclaredMethod("values");
        Object obj = method.invoke(null);
        return (E[]) obj;
    }

    public static Object toObject(Class clazz, String value, FieldType fieldType) {
        if (fieldType.equals(FieldType.STRING)) return value;
        if (fieldType.equals(FieldType.BOOLEAN)) return Boolean.parseBoolean(value);
        if (fieldType.equals(FieldType.CHAR)) return value.isEmpty() ? '_' : value.charAt(0);
        if (fieldType.equals(FieldType.ENUM)) return Enum.valueOf(clazz, value);
        if (fieldType.equals(FieldType.FLOAT)) return Float.parseFloat(value);
        if (fieldType.equals(FieldType.INT)) return Integer.parseInt(value);

        return value;
    }

    private void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setContentText(text);
        alert.showAndWait();
    }

}
