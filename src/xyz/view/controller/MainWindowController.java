package xyz.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import xyz.entity.ScriptObject;
import xyz.functions.robot.AutoTest;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainWindowController {
    @FXML
    private TableView<ScriptObject> scriptTable;
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
        scriptList=new ArrayList<>();
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
                scriptDirField.setText(scriptDir.getPath());
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
    private void handleStart(){
        if(!emptyCheck()){

        }
    }
    @FXML
    private void handleAdd(){

    }
    @FXML
    private void handleDelete(){

    }
    @FXML
    private void handleRefresh(){
        if(scriptDir==null){
            showAlert(Alert.AlertType.ERROR,null,"脚本目录不可为空");
        }else{
            String[] extentions={"json"};
            Collection<File> fileList= FileUtils.listFiles(scriptDir,extentions,false);
            try {
                for(File file:fileList){
                    scriptList.add(new ScriptObject(file.getName(),file.getCanonicalPath()));
                }
                obsScriptList=FXCollections.observableList(scriptList);
                scriptTable.setItems(obsScriptList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
            return false;
        }else{
            showAlert(Alert.AlertType.ERROR,null,sb.toString());
            return true;
        }
    }
    private void showAlert(Alert.AlertType type,String header,String content){
        Alert alert =new Alert(type);
        alert.setHeaderText(header==null?"请检查输入":header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
