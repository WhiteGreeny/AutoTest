package xyz.view.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xyz.entity.Operation;
import xyz.entity.ScriptObject;

import java.io.IOException;
import java.util.Map;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        primaryStage.setTitle("AutoTest");
        initRootLayout();
        showMainWindow();
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
            MainWindowController controller=loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean showEditWindow(Map<String,Operation> map, boolean edit, ScriptObject script){
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(Main.class.getResource("/xyz/view/controller/AddWindow.fxml"));
            AnchorPane addView=loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Operation");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(addView);
            dialogStage.setScene(scene);
            AddWindowController controller=loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMap(map);
            controller.setScript(script);
            controller.setEdit(edit);
            dialogStage.showAndWait();
            return controller.isFinish();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
