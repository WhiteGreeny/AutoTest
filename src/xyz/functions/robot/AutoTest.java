package xyz.functions.robot;

import xyz.entity.Operation;
import xyz.entity.Position;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AutoTest {
    private Map<String, Position> posMap;
    private SimulatorUtils magic;
    private SimpleDateFormat dateFormat;
    private Date date;
    private File outPath;

    public AutoTest() throws AWTException {
        posMap = null;
        magic = new SimulatorUtils();
        dateFormat=new SimpleDateFormat("MM_dd_HH_mm");
        date=new Date();
    }

    public boolean setPositionMap(Map<String, Position> posMap) {
        if (posMap == null || posMap.size() <= 0) {
            return false;
        }
        this.posMap = posMap;
        return true;
    }
    public boolean setOutPath(File outPath){
        if(outPath==null){
            return false;
        }
        this.outPath=outPath;
        return true;
    }

    /**
     * @param list
     * @param isOffset 是否进行偏移
     * @return
     */
    public String executeOperations(List<Operation> list, boolean isOffset) throws InterruptedException, IOException {
        String extention=".png";
        StringBuilder sb=new StringBuilder();
        if(outPath==null){
            return sb.append("输出路径未设置\n").toString();
        }
        if (list == null || list.size() <= 0) {
            return sb.append("operation list empty!\n").toString();
        }
        for (Operation operation : list) {
            String str=executeOperation(operation,operation.getRange());
            if(str.length()>0){
                sb.append(operation.getName()).append(" ").append(str).append("\n");
            }
            if(operation.isScreenShot()){
                Thread.sleep(2000);
                String fileName=outPath.getCanonicalPath()+"\\"+dateFormat.format(date)+extention;
                boolean flag=magic.saveScreenShot(1280,720,new File(fileName));
                if(!flag){
                    sb.append(operation.getName()).append(" screenshot fail\n");
                }
            }


        }
        return sb.length()>0?sb.toString():"";
    }

    /**
     * @param operation
     * @param range  (0~range]
     * @return
     */
    public String executeOperation(Operation operation, int range) throws InterruptedException {
        if (operation == null) {
            return "operation is null\n";
        }
        Position pos = null;
        //Position nextPos = null;
        switch (operation.getType()) {
            case CHOICE:
            case BUTTON:
                pos = operation.getPos();
                if (operation.isChoices()) {
                    pos = magic.getNextValidChoicePos(pos, range, operation.getHeight());
                }
                if(pos==null){
                    return "can not get valid position\n";
                }
                for(int i=0;i<operation.getTimes();i++){
                    long delay=operation.getTimes()>2?100:1000;
                    magic.mouseClick(pos,operation.isDoubleC(),delay);
                }
                return "";
            case MONEY:
                pos = operation.getPos();
                if(pos==null){
                    return "can not get valid position\n";
                }
                magic.mouseDoubleClick(pos);
                String money= String.format("%.2f",Math.random()*1000000%100000);
                magic.pasteString(money);
                return "";
            default:
                return "wrong type\n";
        }
    }
}
