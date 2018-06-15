package xyz.view.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import xyz.entity.ScriptObject;
import xyz.functions.robot.AutoTest;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainWindowController {
    @FXML
    private TableView<String> scriptTable;
    @FXML
    private TableColumn<ScriptObject,String> nameCol;
    @FXML
    private TextField timesField;
    @FXML
    private TextField outpathField;
    @FXML
    private TextField scriptDirField;
    @FXML
    private Button scriptDirChooseButton;
    @FXML
    private Button outpathChooseButton;
    @FXML
    private Button startButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;

    private List<ScriptObject> scriptList;
    private ObservableList<ScriptObject> obsScriptList;
    private File outpath;
    private File scriptDir;
    private AutoTest robot;

    @FXML
    private void initialize(){
        nameCol.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        outpathField.setEditable(false);
        scriptDirField.setEditable(false);
        try {
            robot=new AutoTest();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void outpathChooser(){
        try {
            DirectoryChooser chooser=new DirectoryChooser();
            chooser.setTitle("选择输出目录");
            chooser.setInitialDirectory(new File("."));
            outpath=chooser.showDialog(new Stage());
            if(outpath!=null){
                outpathField.setText(outpath.getPath());
            }else{
                outpath=new File(".");
                outpathField.setText(outpath.getCanonicalPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void scriptDirChooser(){
        try {
            DirectoryChooser chooser=new DirectoryChooser();
            chooser.setTitle("选择脚本所在目录");
            chooser.setInitialDirectory(new File("."));
            scriptDir=chooser.showDialog(new Stage());
            if(scriptDir!=null){
                scriptDirField.setText(outpath.getPath());
            }else{
                scriptDir=new File("./script");
                if(!scriptDir.exists()){
                    scriptDir.mkdir();
                }
                scriptDirField.setText(scriptDir.getCanonicalPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void startHandler(){
        if(emptyCheck()){

        }
    }
    @FXML
    private void addHandler(){

    }
    @FXML
    private void deleteHandler(){

    }
    private boolean emptyCheck(){
        StringBuilder sb=new StringBuilder();
        if(outpath==null){
            sb.append("输出路径不可为空\n");
        }
        if(scriptDir==null){
            sb.append("脚本目录不可为空\n");
        }
        if(timesField.getText()==null||timesField.getText().trim().length()<=0){
            sb.append("执行次数不可为空\n");
        }
        if(sb.length()<=0){
            return true;
        }else{
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("请检查输入");
            alert.setContentText(sb.toString());
            alert.showAndWait();
            return false;
        }
    }

}
