package tw.org.iii.bill.guessgame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.connection.ConnectionUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;

public class Clint extends Activity {
    private  Thread thread;
    private Socket clintSocket;//客戶端socket
    private BufferedReader br;//取得網路輸入串流
    private BufferedWriter bw;//取得網路輸出串流
    private Object write,read;//從JAVA伺服器傳遞與接收資料的物件
    private String tmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        thread=new Thread();
        thread.start();
    }

    private Runnable Connection =new Runnable() {
        @Override
        public void run() {
            try{
                InetAddress severIp=InetAddress.getByName("192.168.50.154");
                int severPort=8888;
                clintSocket=new Socket(severIp,severPort);
                bw=new BufferedWriter(new OutputStreamWriter(clintSocket.getOutputStream()));
                br=new BufferedReader((new InputStreamReader(clintSocket.getInputStream())));
                while (clintSocket.isConnected()){
                    tmp=br.readLine();

                    if (tmp!=null){
                        tmp=tmp.substring(tmp.indexOf("{"),tmp.lastIndexOf("}")+1);
                        read=new JSONObject(tmp);//ObJECT 不行 JSONobJECT可以 read 不是object 所以沒用到上面的
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.v("bill","socket連線="+e.toString());
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            //傳送離線Action 給Server端
            JSONObject write=new JSONObject();
            write.put("action","離線");
            bw.write(write+"\n");
            bw.flush();
            bw.close();
            br.close();
            clintSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
