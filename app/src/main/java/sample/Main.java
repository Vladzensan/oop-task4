package sample;

import Utils.ComponentGroup;
import Utils.FieldGenerator;
import commoncipher.Cipher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BusStop;
import model.CityTransport;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

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
//        launch(args);
          List<Cipher> ciphers = getAvailablePlugins("/home/vladzensan/IdeaProjects/OOP_Task4/out/artifacts");
        System.out.println(ciphers.size());

    }

    private static List<Cipher> loadPlugins(ModuleLayer moduleLayer) {
        return ServiceLoader
                .load(moduleLayer, Cipher.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }


    private static List getAvailablePlugins(String searchPath) {
        Path pluginsDir = Paths.get(searchPath);
        ModuleFinder pluginsFinder = ModuleFinder.of(pluginsDir);
        List<String> plugins = pluginsFinder
                .findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .collect(Collectors.toList());
        Configuration pluginsConfiguration = ModuleLayer
                .boot()
                .configuration()
                .resolve(pluginsFinder, ModuleFinder.of(), plugins);
        ModuleLayer layer = ModuleLayer
                .boot()
                .defineModulesWithOneLoader(pluginsConfiguration, ClassLoader.getSystemClassLoader());
        return loadPlugins(layer);
    }


}
