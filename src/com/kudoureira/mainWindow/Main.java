package com.kudoureira.mainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("htmlWindow.fxml"));
        primaryStage.setTitle("Local HTML Scraper");
        primaryStage.setScene(new Scene(root, 900, 350));
        primaryStage.show();

//        primaryStage.setTitle("Amazon Kindle Scraper");
//
//        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
//        Pane kindlePane = myLoader.load();
//        Controller controller = myLoader.getController();
//        controller.setPrevStage(primaryStage);
//        Scene kindleScene = new Scene(kindlePane, 900, 350);
//        primaryStage.setScene(kindleScene);
//        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
