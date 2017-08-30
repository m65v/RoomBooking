package com.team9.bookingsystem;

import com.team9.bookingsystem.Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.awt.Desktop;
import java.io.File;
//import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
//import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
//import javafx.stage.Stage;

public class Ui extends Application {

    private Stage thestage;
    private Parent ui;
    private Scene uiScene;
    private MainController controller;
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        thestage = primaryStage;
        try{
            initializeScenes();
        }catch(IOException e){
            e.printStackTrace();
        }

        thestage.setScene(uiScene);
        thestage.show();

    }

    /**
     * @throws IOException
     */
    public void initializeScenes() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/view/Root.fxml"));
        ui = loader.load();

        controller= loader.getController();
        controller.init(thestage);

        uiScene = new Scene(ui,1100,700);




    }
}


       