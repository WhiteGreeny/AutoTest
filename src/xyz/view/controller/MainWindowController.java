package xyz.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import xyz.entity.Operation;
import xyz.entity.ScriptObject;
import xyz.functions.json.JSONObject;
import xyz.functions.robot.AutoTest;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
    private Main mainApp;

    public MainWindowController(){}
    public void setMainApp(Main mainApp){
        this.mainApp=mainApp;
    }

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
                this.handleRefresh();
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
            try {
                ScriptObject script=scriptTable.getSelectionModel().getSelectedItem();
                if(script!=null){
                    for(int i=0;i<Integer.parseInt(timesField.getText());i++){
                        File file=new File(script.getFilePath());
                        String content=FileUtils.readFileToString(file,"UTF-8");
                        JSONObject jsnObj=new JSONObject(content);
                        List<Operation> list=Operation.getInstListByJSONObj(jsnObj);
                        AutoTest robot=new AutoTest();
                        robot.setOutPath(outpath);
                        String message=robot.executeOperations(list,true);
                        if(message.length()>0){
                            showAlert(Alert.AlertType.ERROR,"错误信息如下：",message);
                        }
                    }
                }else{
                    showAlert(Alert.AlertType.ERROR,"需要选择一个脚本",null);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (AWTException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleAdd(){
        if(scriptDir==null){
            showAlert(Alert.AlertType.ERROR,"脚本路径不可为空","");
            return;
        }
        Map<String,Operation> map=new HashMap<>();
        ScriptObject script=new ScriptObject();
        if(mainApp.showEditWindow(map,false,script)){
            String fileName=script.getName();
            try {
                script.setFilePath(scriptDir.getCanonicalPath()+"\\"+fileName);
                if(fileName!=null){
                    JSONObject jsnObj=new JSONObject();
                    for(String key:map.keySet()){
                        jsnObj.put(key,map.get(key).operationToJSONObj());
                    }
                    FileUtils.write(new File(script.getFilePath()),jsnObj.toString(),"UTF-8",false);
                    handleRefresh();
                }else{
                    System.out.println("null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }/*else{
            showAlert(Alert.AlertType.ERROR,"新增失败","");
        }*/

    }
    @FXML
    private void handleDelete(){

    }
    @FXML
    private void handleEdit(){
        try {
            ScriptObject script=scriptTable.getSelectionModel().getSelectedItem();
            if(script!=null){
                String content=FileUtils.readFileToString(new File(script.getFilePath()),"UTF-8");
                JSONObject jsnObj=new JSONObject(content);
                Map<String ,Operation> map=Operation.getMapByJSONObj(jsnObj);
                if(mainApp.showEditWindow(map,true,script)){
                    if(map!=null){
                        jsnObj=new JSONObject();
                        for(String key:map.keySet()){
                            jsnObj.put(key,map.get(key).operationToJSONObj());
                        }
                        FileUtils.write(new File(script.getFilePath()),jsnObj.toString(),"UTF-8",false);
                    }else{
                        showAlert(Alert.AlertType.ERROR,"映射为空","");
                    }
                }/*else{
                    showAlert(Alert.AlertType.ERROR,"修改失败","");
                }*/
                handleRefresh();
            }else{
                showAlert(Alert.AlertType.WARNING,"请选中一条记录","");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleRefresh(){
        if(scriptDir==null){
            showAlert(Alert.AlertType.ERROR,null,"脚本目录不可为空");
        }else{
            String[] extentions={"json"};
            Collection<File> fileList= FileUtils.listFiles(scriptDir,extentions,false);
            scriptList.clear();
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
