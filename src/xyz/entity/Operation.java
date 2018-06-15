package xyz.entity;

import xyz.functions.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Operation {
    private String name;
    private OperationType type;
    private int times;
    private boolean doubleC;
    private boolean choices;
    private int height;
    private int range;
    private boolean screenShot;

    public Operation(String name, OperationType type, int times, boolean doubleC, boolean choices, int height, int range,boolean screenShot) {
        this.name = name;
        this.type = type;
        this.times = times;
        this.doubleC = doubleC;
        this.choices = choices;
        this.height=height;
        this.range=range;
        this.screenShot=screenShot;
    }

    public static Operation getInstanceByJSONObj(JSONObject jsonObj) {
        if(jsonObj.getString("name")!=null&&jsonObj.getString("name").trim().length()>0){
            return new Operation(jsonObj.getString("name"),
                    OperationType.valueOf(jsonObj.getString("type").toUpperCase()),
                    jsonObj.getInt("times"),
                    jsonObj.getBoolean("doubleC"),
                    jsonObj.getBoolean("choices"),
                    jsonObj.getInt("height"),
                    jsonObj.getInt("range"),
                    jsonObj.getBoolean("screenshot"));
        }
        return null;

    }
    public static List<Operation> getInstListByJSONObj(JSONObject jsnObj){
        List<Operation> list=new ArrayList<>();
        for(int num=0x01;num<=0xff;num++){
            String key=String.format("%02x",num);
            if(!jsnObj.has(key)){
                break;
            }
            JSONObject subObj=jsnObj.getJSONObject(key);
            if(subObj!=null){
                Operation temp=Operation.getInstanceByJSONObj(subObj);
                if(temp!=null){
                    list.add(temp);
                }
            }
        }
        return list.size()>0?list:null;
    }
    public static boolean operationsToJSON(List<Operation> list){
        return true;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public boolean isDoubleC() {
        return doubleC;
    }

    public void setDoubleC(boolean doubleC) {
        this.doubleC = doubleC;
    }

    public boolean isChoices() {
        return choices;
    }

    public void setChoices(boolean choices) {
        this.choices = choices;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isScreenShot() {
        return screenShot;
    }

    public void setScreenShot(boolean screenShot) {
        this.screenShot = screenShot;
    }
}
