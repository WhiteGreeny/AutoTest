package xyz.view.test;

import org.apache.commons.io.FileUtils;
import xyz.entity.Position;
import xyz.functions.json.JSONObject;
import xyz.functions.robot.SimulatorUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainTest {
    public static void main(String[] args){
        try {
            SimulatorUtils magic=new SimulatorUtils();
            File file=new File("C:\\AZone\\杂物箱\\开发产生\\测试JSON脚本\\paybill.json");
            String content=FileUtils.readFileToString(file,"UTF-8");
            JSONObject jsonObject=new JSONObject(content);
            Map<String,Position> posMap=new HashMap<>();
            Map<String,Object> jsonMap=jsonObject.toMap();
            for(String key:jsonMap.keySet()){
                posMap.put(key,Position.string2Pos(jsonMap.get(key).toString()));
            }
            //新增
            magic.mouseLeftClick(posMap.get("add"));
            Thread.sleep(500);
            //自制
            magic.mouseLeftClick(posMap.get("selfMake"));
            Thread.sleep(500);
            //组织
            magic.mouseLeftClick(posMap.get("org"));
            Thread.sleep(500);
            magic.mouseLeftClick(posMap.get("orgfl"));
            Thread.sleep(500);
            magic.mouseDoubleClick(posMap.get("orgsl"));
            Thread.sleep(500);
            //供应商
            magic.mouseLeftClick(posMap.get("supplier"));
            Thread.sleep(500);
            Position sptflPos=posMap.get("supplierfl");
            sptflPos.adjustPos(-10,-10);
            int count=0;
            Position actualPos=magic.getNextChoicePos(sptflPos,count,18);
            if(actualPos!=null){
                actualPos.adjustPos(10,10);
                magic.mouseLeftClick(actualPos);
                Thread.sleep(500);
                Position sptSlPos=posMap.get("suppliersl");
                Position actualSlPos=magic.getNextValidChoicePos(sptSlPos,6,22);
                if(actualSlPos!=null){
                    magic.mouseDoubleClick(actualSlPos);
                }
            }
            System.out.println("finish");


            /*while(count>0){
                if (actualPos!=null){
                    magic.mouseLeftClick(actualPos);
                    Thread.sleep(1000);
                    Position sptSlPos=posMap.get("suppliersl");
                    Position actualSlPos=magic.getNextChoicePos(sptSlPos,1);
                    if (actualSlPos!=null){
                        magic.mouseDoubleClick(actualSlPos);
                        break;
                    }
                }
                actualPos=magic.getNextChoicePos(sptflPos,count);
                count--;
            }*/



        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
