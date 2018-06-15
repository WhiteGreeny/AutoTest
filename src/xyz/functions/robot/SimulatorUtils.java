package xyz.functions.robot;

import xyz.entity.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SimulatorUtils {
    private Robot robot;
    public SimulatorUtils() throws AWTException {
        robot=new Robot();
    }
    public void combineKey(int...keyArr) throws InterruptedException {
        for(int key:keyArr){
            robot.keyPress(key);
        }
        for(int key:keyArr){
            robot.keyRelease(key);
        }
        Thread.sleep(1000);
    }
    public void mouseLeftClick(int x,int y) throws InterruptedException {
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(1000);
    }
    public void mouseLeftClick(Position pos) throws InterruptedException {
        robot.mouseMove(pos.getX(),pos.getY());
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(1000);
    }public void mouseLeftClickWithoutSleep(int x,int y) throws InterruptedException {
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    public void mouseLeftClickWithoutSleep(Position pos) throws InterruptedException {
        robot.mouseMove(pos.getX(),pos.getY());
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    public void mouseDoubleClick(int x,int y) throws InterruptedException {
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(1000);
    }
    public void mouseDoubleClick(Position pos) throws InterruptedException {
        robot.mouseMove(pos.getX(),pos.getY());
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(1000);
    }
    public void mouseClick(Position pos,boolean isDouble,long delay) throws InterruptedException {
        if(isDouble){
            mouseDoubleClick(pos);
        }else{
            mouseLeftClickWithoutSleep(pos);
            Thread.sleep(delay);
        }
    }
    public void pasteString(String str) throws InterruptedException {
        Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection strSelection=new StringSelection(str);
        clipboard.setContents(strSelection,null);
        this.combineKey(KeyEvent.VK_CONTROL,KeyEvent.VK_V);
    }
    public boolean saveScreenShot(int x,int y,File file){
        try {
            if(file==null||x<=0||y<=0){
                return false;
            }
            String fileName=file.getName();
            String imageFormat=fileName.substring(fileName.indexOf(".")+1,fileName.length());
            BufferedImage screenShot=robot.createScreenCapture(new Rectangle(0,0,x,y));
            ImageIO.write(screenShot,imageFormat,file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 文本框高度约20px <br>
     * 需要检测文本框第一个字位置的像素颜色 <br>
     * 检测多个点，如果有满足条件的(如 不为白色)，则认为不为空 <br>
     * 不可以只判断是否黑色，因为字体不一定是黑色，但一定不是白色 <br>
     * 从x+5,y+5开始，往右下检测10px <br>
     * 相隔2px，检测一次 <br>
     * @param x,y 左上角坐标
     * @return 为空则返回true
     */
    public boolean isTextEmpty(int x,int y){
        boolean empty=true;
        for(int i=5;i<10&&empty;i++){
            for(int j=5;j<10&&empty;j++){
                if(!isWhite(x+j,y+i)){
                    empty=false;
                }
            }
        }
        return empty;
    }
    public boolean isTextEmpty(Position pos){
        boolean empty=true;
        for(int i=5;i<10&&empty;i++){
            for(int j=5;j<10&&empty;j++){
                if(!isWhite(pos.getX()+j,pos.getY()+i)){
                    empty=false;
                }
            }
        }
        return empty;
    }
    public final boolean isBlack(int x,int y){
        Color pointColor=robot.getPixelColor(x,y);
        return pointColor.equals(Color.BLACK);
    }
    public final boolean isWhite(int x,int y){
        Color pointColor=robot.getPixelColor(x,y);
        return pointColor.equals(Color.white);
    }

    /**
     * 偏移量大于等于0，0则坐标不变. <br>
     * 即从第一个选项开始数，不算目标选项的数目
     * 如想选择第六个选项
     * 则偏移量为5
     * @param pos
     * @param offset 偏移量
     * @param height 单个选项的高度
     * @return 目标选项的左上角坐标
     */
    public Position getNextChoicePos(Position pos,int offset,int height){
        if(pos!=null&&offset>=0){
            int y=pos.getY()+height*offset;
            if(y>720||y<=0){
                return null;
            }
            int x=pos.getX();
            return new Position(x,y);
        }
        return null;
    }

    /**
     *
     * @param pos
     * @param range [0~range]
     * @return
     */
    public Position getNextValidChoicePos(Position pos,int range,int height){
        if(pos==null){
            return null;
        }
        if(range==0){
            pos.adjustPos(10,10);
            return pos;
        }
        //检查首个选项是否可用，如果不可用也就不需要再往下寻找其他选项了
        if(isTextEmpty(pos)){
            return null;
        }
        int offset= (int)(Math.random()*100)%range;
        Position nextPos=getNextChoicePos(pos,offset,height);
        while((nextPos==null||isTextEmpty(nextPos))&&--offset>=0){
            nextPos=getNextChoicePos(pos,offset,height);
        }
        //如果nextpos有效，那么offset不会有机会小于0
        //只有在nextpos一直无效的情况下，offset才会自减至小于0
        if(offset<0){
            return null;
        }
        nextPos.adjustPos(10,10);
        return nextPos;
    }
}
