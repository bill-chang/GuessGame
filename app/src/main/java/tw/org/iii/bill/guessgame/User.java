package tw.org.iii.bill.guessgame;


import android.app.ListActivity;

import java.util.ArrayList;

public class User  {
    private  String name;
    private  int point;
    private String  img;
    private String info;
//    private ArrayList<String> userList;
    public  User(){
        name="";
//        userList=new ArrayList<>();
        point=0;
        img="";
        info="";
    }
//    public void addName(String name){
//        userList.add(name);
//    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setPoint(int point){
        this.point=point;
    }
    public int getPoint(){
        return  point;
    }
    public void setImg(String img){
        this.img=img;
    }
    public String getImg(){
        return img;
    }
    public void setInfo(String info){
        this.info=info;
    }
    public String  getInfo(){
        return info;
    }


}
