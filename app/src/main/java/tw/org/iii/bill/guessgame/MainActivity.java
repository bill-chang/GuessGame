package tw.org.iii.bill.guessgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText et_name;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ImageView img;
    private Button login_left_btn,login_right_btn;
    private int playerNo=0;
    private ArrayList<String> abc123 = new ArrayList<>();
    private ArrayList<String> abc456 = new ArrayList<>();
    private ArrayList<String> topicAnimal1 = new ArrayList<>();
    private ArrayList<String> topicCloth2 = new ArrayList<>();
    private ArrayList<String> topicFurniture = new ArrayList<>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private DataSnapshot dataSnapshot;
    private  Intent music,service;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    123);
        } else {
            init();


        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }

    private void init() {
        et_name = findViewById(R.id.login_et_name);
        database = FirebaseDatabase.getInstance();
        selectLanguage();
        selectPlayerImg();
        music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
        login_left_btn=findViewById(R.id.login_left_btn);
        login_right_btn=findViewById(R.id.login_right_btn);
        getMyIp();
        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(singleListener);

    }

    ValueEventListener singleListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            MainActivity.this.dataSnapshot=dataSnapshot;
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                abc123.add(childSnapshot.getKey());
                for (DataSnapshot member : childSnapshot.getChildren()) {
                    abc456.add(member.getKey());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    private void getMyIp(){
        new Thread(){
            @Override
            public void run() {
                try{
                    String myIp= InetAddress.getLocalHost().getHostAddress();

                }catch (UnknownHostException e){

                }
            }
        }.start();
    }




    public void doStart(View view) {
        final String id = et_name.getText().toString();

        topicAnimal1.add("獅子");
        topicAnimal1.add("貓");
        topicAnimal1.add("老鼠");
        topicAnimal1.add("狗");topicAnimal1.add("鱷魚");
        topicCloth2.add("T-shirt");
        topicCloth2.add("POLO衫");
        topicCloth2.add("襯衫");
        topicCloth2.add("洋裝");
        topicCloth2.add("裙子");
        topicCloth2.add("牛仔褲");
        topicFurniture.add("椅子");
        topicFurniture.add("桌子");
        topicFurniture.add("沙發");
        topicFurniture.add("茶几");
        topicFurniture.add("櫃子");
        List<Object> list = new ArrayList();
        list.add(topicFurniture);
        list.add(topicAnimal1);
        list.add(topicCloth2);


        if (id.matches("")) {
            Log.v("bill", "etName");
            AlertDialog dialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("開始失敗!");
            builder.setMessage("名子未填寫,請重填!");
            builder.setCancelable(true);
            builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog = builder.create();
            dialog.show();


        }else{
            Log.v("bill","else:");
            if(abc123.size()==0){
                User user = new User();
                user.setName(id);
                user.setImg("");
                user.setPoint(0);
                int random = (int) (Math.random() * 3);
                Log.v("bill", "random" + random);
                Object list1 = list.get(random);
                Log.v("bill", "data");

//                DatabaseReference cRoom = myRef.child("room:"+ id).child("chatRoom");
//                cRoom.setValue("");
                DatabaseReference users = myRef.child("room:" + id).child("users").child(id);
                users.setValue(user);
                DatabaseReference topic = myRef.child("room:" + id).child("topic");
                topic.setValue(list1);

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                service=new Intent(this,MyService.class);//server
                service.putExtra("userName",id);
                intent.putExtra("name",id);
                Log.v("bill","intent:" +intent.getBundleExtra("data"));
                startService(service);// server
                startActivity(intent);

                    } else {
                        String fp=abc123.get(abc123.size()-1);
                        if (dataSnapshot.child(fp).child("users").getChildrenCount() < 8) {
                            //最後一個節點 users不滿8人 加入房間
                            User user1 = new User();
                            user1.setName(id);
                            user1.setImg("");
                            user1.setPoint(0);

                            DatabaseReference userName = myRef.child(fp).child("users").child(id);
                            userName.setValue(user1);

                            Intent intent = new Intent(MainActivity.this, GameActivity.class);

                            intent.putExtra("name",id);
                            Log.v("bill","intent:" +intent.getBundleExtra("data"));

                            startActivity(intent);

                        } else if (dataSnapshot.child(fp).child("users").getChildrenCount() == 8) {
                            //最後一個房間 滿八人 創房間
                            Log.v("bill","else if");
                            User user = new User();
                            user.setName(id);
                            user.setImg("");
                            user.setPoint(0);
//
                            list.add(topicFurniture);
                            list.add(topicAnimal1);
                            list.add(topicCloth2);

                            int random = (int) (Math.random() * 3);
                            Log.v("bill", "random" + random);
                            Object list1 = list.get(random);

                            //                        Chat chat=new Chat(id,);
                            Log.v("bill", "data");
//                            DatabaseReference cRoom = myRef.child(fp).child("chatRoom");
//                            cRoom.setValue("");
                            DatabaseReference users = myRef.child("room:" + id).child("users").child(id);
                            users.setValue(user);
                            DatabaseReference topic = myRef.child("room:" + id).child("topic");
                            topic.setValue(list1);

                            Intent intent = new Intent(MainActivity.this, GameActivity.class);
                            service=new Intent(this,MyService.class);//server
                            service.putExtra("userName",id);
                            intent.putExtra("name",id);
                            startService(service);// server
                            startActivity(intent);

                        }
                    }
                }

    }


    public void doCreate(View view) {
        Intent intent=new Intent(this,AddRoomActivity.class);
        startActivity(intent);
    }



    public void doSetting(View view) {
        Button btn = findViewById(R.id.login_setting_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MusicSettingActivity.class);
                startActivity(intent);
            }
        });
    }


    private void selectLanguage() {
        MaterialSpinner spinner = findViewById(R.id.login_et_language);
        spinner.setItems("chinese","English");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Choose " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void selectPlayerImg() {
        final List<Drawable> drawableList = new ArrayList<Drawable>();

        drawableList.add(getResources().getDrawable(R.drawable.avatar1));
        drawableList.add(getResources().getDrawable(R.drawable.avatar2));
        drawableList.add(getResources().getDrawable(R.drawable.avatar3));
        drawableList.add(getResources().getDrawable(R.drawable.avatar4));

        final ImageSwitcher imageSwitcher = findViewById(R.id.login_mid_ly);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(MainActivity.this);
            }
        });

        imageSwitcher.setImageDrawable(drawableList.get(playerNo++));

        login_left_btn = findViewById(R.id.login_left_btn);
        login_right_btn = findViewById(R.id.login_right_btn);

        login_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageDrawable(drawableList.get(playerNo--));
                if (playerNo<0) {
                    MainActivity.this.playerNo = drawableList.size()-1;
                }
            }
        });

        login_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageDrawable(drawableList.get(playerNo++));
                if(playerNo>=drawableList.size()) {
                    playerNo = 0;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        stopService(music);
        stopService(service);
        super.onDestroy();
    }
}