package tw.org.iii.bill.guessgame;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Sever {
    private  static int serverPort=8888;
    private static ServerSocket serverSocket;//伺服器端的socket
    private  static int count=0;
    private static ArrayList clients=new ArrayList();

    public static void main(String[] args){
        try{
            serverSocket=new ServerSocket(serverPort);
//            Log.v("bill","sever start");
            System.out.println("Waiting for Client connect ");

            while (serverSocket.isClosed()){
                waitNewClient();
            }
        }catch (Exception e){
            Log.v("bill","sever socket error");
        }
    }

    public  static void waitNewClient(){
        try{
            Socket socket=serverSocket.accept();
            ++count;
            Log.v("bill","現在使用者數量:"+count);
            addNewClient(socket);
        }catch (Exception e){

        }
    }

    public static void addNewClient(final Socket socket)throws IOException{
        Thread t=new Thread(new Runnable() {//RUNnable跟直接 override run方法有啥不一樣
            @Override
            public void run() {
                try{
                    clients.add(socket);
                    BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (socket.isConnected()){
                        String mesg=br.readLine();
                        if(mesg==null){
                            System.out.println("Client Disconnected");
                            break;
                        }
                        System.out.println(mesg);
                        castMsg(mesg);
                    }
                }catch (IOException e){
                    e.getStackTrace();
                }
                finally {
                    clients.remove(socket);
                    --count;

                }
            }
        });t.start();
    }


    //廣播訊息給其他的客戶端 應該不需要
    public static void castMsg(String Msg){
        Socket[] clientArrays =new Socket[clients.size()];
        clients.toArray(clientArrays);
        for(Socket socket:clientArrays){
            try{
                BufferedWriter bw;
                bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(Msg+"\n");
                bw.flush();
            }catch(IOException e){

            }
        }
    }
}
