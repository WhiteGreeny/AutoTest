package xyz.entity;

import javafx.beans.property.SimpleStringProperty;
import xyz.functions.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Operation {
    private SimpleStringProperty no;
    private SimpleStringProperty name;
    private OperationType type;
    private Position pos;
    private int times;
    private boolean doubleC;
    private boolean choices;
    private int height;
    private int range;
    private boolean screenShot;

    public Operation(){
        this.no=new SimpleStringProperty("0");
        this.name=new SimpleStringProperty("00");
    }
    public Operation(String no, String name, OperationType type, Position pos, int times, boolean doubleC, boolean choices, int height, int range, boolean screenShot) {
        this.no = new SimpleStringProperty(no);
        this.name = new SimpleStringProperty(name);
        this.type = type;
        this.times = times;
        this.doubleC = doubleC;
        this.choices = choices;
        this.height = height;
        this.range = range;
        this.screenShot = screenShot;
        this.pos = pos;
    }

    public static Operation getInstanceByJSONObj(JSONObject jsonObj) {
        if (jsonObj.getString("name") != null && jsonObj.getString("name").trim().length() > 0) {
            return new Operation(jsonObj.getString("no"),
                    jsonObj.getString("name"),
                    OperationType.valueOf(jsonObj.getString("type").toUpperCase()),
                    Position.string2Pos(jsonObj.getString("pos")),
                    jsonObj.getInt("times"),
                    jsonObj.getBoolean("doubleC"),
                    jsonObj.getBoolean("choices"),
                    jsonObj.getInt("height"),
                    jsonObj.getInt("range"),
                    jsonObj.getBoolean("screenshot"));
        }
        return null;

    }

    public static List<Operation> getInstListByJSONObj(JSONObject jsnObj) {
        List<Operation> list = new ArrayList<>();
        for (int num = 0x01; num <= 0xff; num++) {
            String key = String.format("%02x", num);
            if (!jsnObj.has(key)) {
                break;
            }
            JSONObject subObj = jsnObj.getJSONObject(key);
            if (subObj != null) {
                Operation temp = Operation.getInstanceByJSONObj(subObj);
                if (temp != null) {
                    list.add(temp);
                }
            }
        }
        return list.size() > 0 ? list : null;
    }

    public static boolean operationsToJSON(List<Operation> list) {
        return true;
    }

    public String getNo() {
        return no.get();
    }

    public SimpleStringProperty noProperty() {
        return no;
    }

    public void setNo(String no) {
        this.no.set(no);
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

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
