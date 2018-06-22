package xyz.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xyz.entity.Operation;
import xyz.entity.OperationType;
import xyz.entity.Position;
import xyz.entity.ScriptObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class AddWindowController {
    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<Operation, String> noCol;
    @FXML
    private TableColumn<Operation, String> nameCol;
    @FXML
    private TextField noField;
    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private TextField xField;
    @FXML
    private TextField yField;
    @FXML
    private TextField timesField;
    @FXML
    private CheckBox isDoubleClick;
    @FXML
    private CheckBox isRandomChoices;
    @FXML
    private CheckBox isScreenShot;
    @FXML
    private TextField heightField;
    @FXML
    private TextField rangeField;
    @FXML
    private TextField fileNameField;
    @FXML
    private Label fileNameLabel;
    @FXML
    private ScrollPane imagePane;
    @FXML
    private SplitPane editPane;

    private List<Operation> list;
    private Map<String, Operation> map;
    private ObservableList<Operation> obsList;
    private boolean finish;
    private Stage dialogStage;
    private String outpath;
    private String fileName;
    private boolean edit;
    private ScriptObject script;
    private double imageW;
    private double imageH;
    private Image image;
    private File imageFile;

    public AddWindowController(){}
    public boolean isFinish(){
        return finish;
    }
    public void setMap(Map<String,Operation> map){
        if(map!=null){
            this.map=map;
        }
        handleRefresh();
    }
    public void setDialogStage(Stage dialogStage){
        this.dialogStage=dialogStage;
        /*dialogStage.setOnCloseRequest(event -> {
            if(map.size()>0){
                String tempName;
                if(fileNameField.getText()!=null&&fileNameField.getText().trim().length()>0){
                    tempName=fileNameField.getText();
                }else{
                    tempName="Unnamed";
                }
                Date now=new Date();
                tempName=tempName+"autosave"+String.format("%tF_%tR",now,now).replaceAll("-","");
                fileNameField.setText(tempName);
                handleSave();
                showAlert(Alert.AlertType.INFORMATION,"已自动保存","文件名称:"+tempName+".json");
            }
        });*/
    }
    public void setOutpath(String outpath){
        this.outpath=outpath;
    }
    public String getFileName(){
        return fileName;
    }
    public void setEdit(boolean edit){
        this.edit=edit;
        if(edit){
            fileNameField.setEditable(false);
            fileNameField.setText(script.getName());
            /*fileNameField.setVisible(false);
            fileNameLabel.setVisible(false);*/
        }
    }
    public void setScript(ScriptObject script){
        this.script=script;
    }
    @FXML
    private void initialize() {
        noCol.setCellValueFactory(cellData -> cellData.getValue().noProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        operationTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->showOperationDetails(newValue));
        showOperationDetails(null);
        list = new ArrayList<>();
        map = new HashMap<>();
        finish=false;
        edit=false;
        type.setItems(FXCollections.observableArrayList("BUTTON", "CHOICE", "MONEY"));
        /*InputStream in= null;
        try {
            in = new FileInputStream("resource/大圣2.jpg");
            Image image=new Image(in);
            imageW=image.getWidth();
            imageH=image.getHeight();
            ImageView imageView=new ImageView(image);
            imagePane.setContent(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/


    }

    @FXML
    private void handleRefresh() {
        if (list != null &&map!=null&&map.size() > 0) {
            list.clear();
            for (int num = 0x01; num <= 0xff; num++) {
                String key = String.format("%02x", num).toUpperCase();
                if (map.containsKey(key)) {
                    list.add(map.get(key));
                }
            }
            obsList = FXCollections.observableList(list);
            operationTable.setItems(obsList);
        }/* else {
            showAlert(Alert.AlertType.ERROR, "操作表未初始化或映射为空", "");
        }*/

    }

    @FXML
    private void handleAddOperation() {
        if (!emptyCheck()) {
            if (map.containsKey(noField.getText().toUpperCase())) {
                showAlert(Alert.AlertType.ERROR, "操作序号重复", "");
            } else {
                Operation operation = new Operation();
                operation.setNo(noField.getText().toUpperCase());
                operation.setName(nameField.getText());
                operation.setPos(new Position(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText())));
                operation.setType(OperationType.valueOf(type.getValue().toUpperCase()));
                operation.setTimes(Integer.parseInt(timesField.getText()));
                operation.setDoubleC(isDoubleClick.isSelected());
                operation.setChoices(isRandomChoices.isSelected());
                operation.setScreenShot(isScreenShot.isSelected());
                operation.setHeight(Integer.parseInt(heightField.getText()));
                operation.setRange(Integer.parseInt(rangeField.getText()));
                map.put(operation.getNo().toUpperCase(), operation);
                this.handleRefresh();
                showOperationDetails(null);
            }

        }
    }
    @FXML
    private void handleDelete(){
        int index=operationTable.getSelectionModel().getSelectedIndex();
        if(index>=0){
            Operation temp=operationTable.getItems().get(index);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("删除确认");
            alert.setHeaderText("确认删除以下记录");
            alert.setContentText("序号 : " + temp.getNo() + "\n操作 : " + temp.getName());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                map.remove(temp.getNo());
                operationTable.getItems().remove(index);
            }
            showOperationDetails(null);
        }else{
            showAlert(Alert.AlertType.ERROR,"需要选中一条记录",null);
        }
    }

    @FXML
    private void handleSave(){
        if(edit||(fileNameField.getText()!=null&&fileNameField.getText().trim().length()>0)&&map.size()>0){
            fileName=fileNameField.getText();
            if(fileName.contains(".")){
                fileName=fileName.substring(0,fileName.lastIndexOf("."));
            }
            script.setName(fileName+".json");
            finish=true;
            dialogStage.close();
        }else{
            showAlert(Alert.AlertType.ERROR,null,"文件名不能为空\n或至少应有一步操作");
        }
    }
    @FXML
    private void handleCancle(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("取消确认");
        alert.setHeaderText("未保存的数据将丢失");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            finish=false;
            dialogStage.close();
        }
    }
    @FXML
    private void handleEdit(){
        Operation temp=operationTable.getSelectionModel().getSelectedItem();
        if(temp!=null){
            temp.setNo(noField.getText().toUpperCase());
            temp.setName(nameField.getText());
            temp.setPos(new Position(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText())));
            temp.setType(OperationType.valueOf(type.getValue().toUpperCase()));
            temp.setTimes(Integer.parseInt(timesField.getText()));
            temp.setDoubleC(isDoubleClick.isSelected());
            temp.setChoices(isRandomChoices.isSelected());
            temp.setScreenShot(isScreenShot.isSelected());
            temp.setHeight(Integer.parseInt(heightField.getText()));
            temp.setRange(Integer.parseInt(rangeField.getText()));
            showOperationDetails(temp);
        }
    }
    @FXML
    private void handleClick(MouseEvent e){
        if (imagePane.getContent()!=null) {
            double w=imageW-imagePane.getViewportBounds().getWidth();
            double h=imageH-imagePane.getViewportBounds().getHeight();
            xField.setText(Integer.toString((int)(e.getX()+w*imagePane.getHvalue())));
            yField.setText(Integer.toString((int)(e.getY()+h*imagePane.getVvalue())));
        }
    }
    @FXML
    private void handleImageChoose(){
        FileChooser chooser=new FileChooser();
        chooser.setTitle("选择一张图片");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG","*.jpg"),
                new FileChooser.ExtensionFilter("BMP","*.bmp"),
                new FileChooser.ExtensionFilter("PNG","*.png")
        );
        if(imageFile!=null){
            chooser.setInitialDirectory(imageFile.getParentFile());
        }
        imageFile=chooser.showOpenDialog(new Stage());
        if(imageFile!=null){
            try {
                InputStream in=new FileInputStream(imageFile);
                image=new Image(in);
                ImageView imageView=new ImageView(image);
                imagePane.setContent(imageView);
                imageW=image.getWidth();
                imageH=image.getHeight();
            } catch (FileNotFoundException e) {
                showAlert(Alert.AlertType.ERROR,"文件打开失败，可能不存在",null);
                e.printStackTrace();
            }
        }else{
            showAlert(Alert.AlertType.INFORMATION,"未选择图片",null);
        }
    }
    @FXML
    private void handlePaste(KeyEvent e){
        if(e.isControlDown()&&e.getCode().equals(KeyCode.V)){
            Clipboard clipboard=Clipboard.getSystemClipboard();
            image=(Image)clipboard.getContent(DataFormat.IMAGE);
            if(image!=null){
                setImage();
            }else{
                showAlert(Alert.AlertType.WARNING,"剪贴板中没有图片",null);
            }
        }
    }
    private void setImage(){
        ImageView imageView=new ImageView(image);
        imagePane.setContent(imageView);
        imageW=image.getWidth();
        imageH=image.getHeight();
    }

    private boolean emptyCheck() {
        StringBuilder sb = new StringBuilder();

        if (!noField.getText().toUpperCase().matches("[0-9A-F]{2}")&&noField.getText().equals("00")) {
            sb.append("序号必须为2位的大于0的16进制数字\n");
        }
        if (!nameField.getText().matches("[a-zA-Z]+[\\w]*")) {
            sb.append("操作名称无效\n");
        }
        if (type.getValue() == null || type.getValue().trim().length() <= 0) {
            sb.append("必须选择一种类型\n");
        }
        if (!xField.getText().matches("[1-9][0-9]*")) {
            sb.append("无效的X坐标\n");
        }
        if (!yField.getText().matches("[1-9][0-9]*")) {
            sb.append("无效的Y坐标\n");
        }
        if (!timesField.getText().matches("[0-9]+")) {
            sb.append("无效的次数\n");
        }
        if (!heightField.getText().matches("[0-9]+")) {
            sb.append("文本高度无效\n");
        }
        if (!rangeField.getText().matches("[0-9]+")) {
            sb.append("选择范围无效");
        }
        if (sb.length() > 0) {
            showAlert(Alert.AlertType.ERROR, null, sb.toString());
            return true;
        } else {
            return false;
        }
    }
    private void showOperationDetails(Operation operation){
        if(operation==null){
            noField.setText("");
            nameField.setText("");
            type.setValue(null);
            xField.setText("");
            yField.setText("");
            timesField.setText("");
            isDoubleClick.setSelected(false);
            isRandomChoices.setSelected(false);
            isScreenShot.setSelected(false);
            heightField.setText("");
            rangeField.setText("");
        }else{
            noField.setText(operation.getNo());
            nameField.setText(operation.getName());
            type.setValue(operation.getType().toString());
            xField.setText(Integer.toString(operation.getPos().getX()));
            yField.setText(Integer.toString(operation.getPos().getY()));
            timesField.setText(Integer.toString(operation.getTimes()));
            isDoubleClick.setSelected(operation.isDoubleC());
            isRandomChoices.setSelected(operation.isChoices());
            isScreenShot.setSelected(operation.isScreenShot());
            heightField.setText(Integer.toString(operation.getHeight()));
            rangeField.setText(Integer.toString(operation.getRange()));
        }
    }


    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header == null ? "请检查输入" : header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
