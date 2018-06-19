package xyz.view.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        primaryStage.setTitle("AutoTest");
        initRootLayout();
        showMainWindow();
        showAddWindow();
    }
    private void initRootLayout(){
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(Main.class.getResource("/xyz/view/controller/RootLayout.fxml"));
            rootLayout=loader.load();
            Scene scene=new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showMainWindow(){
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(Main.class.getResource("/xyz/view/controller/MainWindow.fxml"));
            AnchorPane mainView=loader.load();
            rootLayout.setCenter(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showAddWindow(){
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(Main.class.getResource("/xyz/view/controller/AddWindow.fxml"));
            AnchorPane addView=loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(addView);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
