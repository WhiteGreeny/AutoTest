package xyz.entity;

import javafx.beans.property.SimpleStringProperty;

public class ScriptObject {
    private SimpleStringProperty name;
    private String filePath;
    public ScriptObject(){
        name=new SimpleStringProperty();
    }
    public ScriptObject(String name,String filePath){
        this.name=new SimpleStringProperty(name);
        this.filePath=filePath;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
