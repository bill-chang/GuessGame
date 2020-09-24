package tw.org.iii.bill.guessgame;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat {
    private String name;
//    private ArrayList<String> nameList=new ArrayList<>();
    private String chatMessage;
    private long time;
    public Chat(String name,String chatMessage){
        this.name=name;
        this.chatMessage=chatMessage;
        time=new Date().getTime();
//        nameList=new ArrayList<>();
    }
    public Chat(){

    }
//    public void addName(String name){
//        nameList.add(name);
//    }
//    public String getListName(){
//        nameList.equals(name);
//        return name;
//    }
    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
