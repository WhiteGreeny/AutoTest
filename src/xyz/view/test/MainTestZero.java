package xyz.view.test;

import org.apache.commons.io.FileUtils;
import xyz.entity.Operation;
import xyz.entity.Position;
import xyz.functions.json.JSONObject;
import xyz.functions.robot.AutoTest;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTestZero {
    public static void main(String[] args){
        File file=new File("C:\\AZone\\杂物箱\\开发产生\\测试JSON脚本\\paybill.json");
        try{
            String content=FileUtils.readFileToString(file,"UTF-8");
            JSONObject jsonObject=new JSONObject(content);
            Map<String,Position> posMap=new HashMap<>();
            Map<String,Object> jsonMap=jsonObject.toMap();
            for(String key:jsonMap.keySet()){
                posMap.put(key,Position.string2Pos(jsonMap.get(key).toString()));
            }
            content=FileUtils.readFileToString(new File("C:\\AZone\\杂物箱\\开发产生\\测试JSON脚本\\paybillOperations.json"),"UTF-8");
            jsonObject=new JSONObject(content);
            List<Operation> list=Operation.getInstListByJSONObj(jsonObject);
            AutoTest robot=new AutoTest();
            robot.setPositionMap(posMap);
            robot.setOutPath(new File("C:\\AZone\\杂物箱\\开发产生\\TestImage"));
            String message=robot.executeOperations(list,true);
            System.out.println(message);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
