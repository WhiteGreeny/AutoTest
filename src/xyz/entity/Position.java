package xyz.entity;

public class Position {
    private int x=0;
    private int y=0;
    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }
    public String toString(){
        return "("+x+","+y+")";
    }

    /**
     *
     * @param posStr (x,y)
     * @return position obj
     */
    public static Position string2Pos(String posStr){
        String str=posStr.substring(1,posStr.length()-1);
        String[] strArr=str.split(",");
        return new Position(Integer.parseInt(strArr[0]),Integer.parseInt(strArr[1]));
    }

    /**
     *
     * @param x 现x坐标差值
     * @param y 现y坐标差值
     */
    public void adjustPos(int x,int y){
        this.x=this.x+x;
        this.y=this.y+y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
