package xyz.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import xyz.entity.Operation;
import xyz.entity.OperationType;
import xyz.entity.Position;

import java.util.ArrayList;
import java.util.List;

public class AddWindowController {
    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<Operation,String> noCol;
    @FXML
    private TableColumn<Operation,String> nameCol;
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

    List<Operation> list;
    ObservableList<Operation> obsList;
    @FXML
    private void initialize(){
        noCol.setCellValueFactory(cellData->cellData.getValue().noProperty());
        nameCol.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        list=new ArrayList<>();
        type.setItems(FXCollections.observableArrayList("BUTTON","CHOICE","MONEY"));
    }
    @FXML
    private void handleRefresh(){
        if(list!=null){
            obsList=FXCollections.observableList(list);
            operationTable.setItems(obsList);
        }else{
            showAlert(Alert.AlertType.ERROR,"操作表未初始化","");
        }

    }
    @FXML
    private void handleAddOperation(){
        //todo 操作序号唯一性验证
        if(!emptyCheck()){
            Operation operation =new Operation();
            operation.setNo(noField.getText());
            operation.setName(nameField.getText());
            operation.setPos(new Position(Integer.parseInt(xField.getText()), Integer.parseInt(xField.getText())));
            operation.setType(OperationType.valueOf(type.getValue().toUpperCase()));
            operation.setTimes(Integer.parseInt(timesField.getText()));
            operation.setDoubleC(isDoubleClick.isSelected());
            operation.setChoices(isRandomChoices.isSelected());
            operation.setScreenShot(isScreenShot.isSelected());
            operation.setHeight(Integer.parseInt(heightField.getText()));
            operation.setRange(Integer.parseInt(rangeField.getText()));
            list.add(operation);
            this.handleRefresh();
        }
    }

    private boolean emptyCheck(){
        StringBuilder sb=new StringBuilder();
        if(noField.getText()==null||noField.getText().trim().length()<=0){
            sb.append("序号不可为空\n");
        }
        if(nameField.getText()==null||nameField.getText().trim().length()<=0){
            sb.append("操作名称不可为空\n");
        }else if(!nameField.getText().matches("[a-zA-Z]+[\\w]*")){
            sb.append("操作名称无效\n");
        }
        if(type.getValue()==null||type.getValue().trim().length()<=0){
            sb.append("必须选择一种类型\n");
        }
        if(xField.getText()==null||xField.getText().trim().length()<=0){
            sb.append("X坐标不可为空\n");
        }
        if(yField.getText()==null||yField.getText().trim().length()<=0){
            sb.append("Y坐标不可为空\n");
        }
        if(timesField.getText()==null||timesField.getText().trim().length()<=0){
            sb.append("次数不可为空\n");
        }
        if(heightField.getText()==null||heightField.getText().trim().length()<=0){
            sb.append("文本高度不可为空\n");
        }else if(Integer.parseInt(heightField.getText())<=0){
            sb.append("文本高度无效\n");
        }
        if(rangeField.getText()==null||rangeField.getText().trim().length()<=0){
            sb.append("选择范围不可为空\n");
        }else if(Integer.parseInt(rangeField.getText())<0){
            sb.append("选择范围不可小于0");
        }
        if(sb.length()>0){
            showAlert(Alert.AlertType.ERROR,null,sb.toString());
            return true;
        }else{
            return false;
        }
    }
    private void showAlert(Alert.AlertType type,String header,String content){
        Alert alert =new Alert(type);
        alert.setHeaderText(header==null?"请检查输入":header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
