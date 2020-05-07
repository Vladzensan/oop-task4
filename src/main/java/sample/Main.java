package sample;

import Utils.ComponentGroup;
import Utils.FieldGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BusStop;
import model.CityTransport;

public class Main extends Application {
    CityTransport transport;

    @Override
    public void start(Stage primaryStage) throws Exception {
          Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("/fxml/sample.fxml"));
//        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1133, 755));
        transport = new CityTransport();
        Object obj = transport;
        System.out.println(obj.getClass());

        primaryStage.show();

        ComponentGroup componentGroup = FieldGenerator.generateComponentGroup(transport);

        BusStop[] strs = new BusStop[5];
        System.out.println(strs.getClass());


    }


    public static void main(String[] args) {
        launch(args);
//        CityTransport cityTransport = new CityTransport();
//        cityTransport.setPresentConductor(true);
//        cityTransport.setStandingPlaces(3);
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> map = objectMapper.convertValue(cityTransport, Map.class);


    }


}
