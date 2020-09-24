package tw.org.iii.bill.guessgame;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class AddRoomActivity extends AppCompatActivity {

    private Button btnLeft;
    private Button btnRight;

    private ImageSwitcher imageSwitcher;
    private int roomData = 0;
    private List<Drawable> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        selectPlayers();
        selectTime();
        selectRoomData();
        putData();
    }

    public void backToMain(View view) {
        Button btn = findViewById(R.id.BackBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddRoomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void selectPlayers() {
        MaterialSpinner spinner = findViewById(R.id.add_Players);
        spinner.setItems("8 Players", "6 Players", "4 Players", "2 Players");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Choose " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void selectTime() {
        MaterialSpinner spinner = findViewById(R.id.add_Time);
        spinner.setItems("10 Minutes", "20 Minutes");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Choose " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void selectRoomData() {
        final List<Drawable> drawableList = new ArrayList<>();

        drawableList.add(getResources().getDrawable(R.drawable.animals));
        drawableList.add(getResources().getDrawable(R.drawable.clothes));
        drawableList.add(getResources().getDrawable(R.drawable.furnitures));

        final ImageSwitcher imageSwitcher = findViewById(R.id.roomDataImg);
        btnLeft = findViewById(R.id.add_leftBtn);
        btnRight = findViewById(R.id.add_rightBtn);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(AddRoomActivity.this);
            }
        });

        imageSwitcher.setImageDrawable(drawableList.get(roomData++));

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageDrawable(drawableList.get(roomData--));
                if (roomData<0) {
                    roomData = drawableList.size()-1;
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageDrawable(drawableList.get(roomData++));
                if(roomData>=drawableList.size()) {
                    roomData = 0;
                }
            }
        });



//        imageSwitcher = findViewById(R.id.roomDataImg);
//        btnLeft = findViewById(R.id.add_leftBtn);
//        btnRight = findViewById(R.id.add_rightBtn);
//
//        // TODO
//
//        imageSwitcher.setFactory((ViewSwitcher.ViewFactory) this);
//        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                return new ImageView(AddRoomActivity.this);
//            }
//        });
//
//        imageSwitcher.setImageDrawable(list.get(0));

    }
    public void startIt(View view) {
        Intent intent=new Intent(this,GameActivity.class);
        startActivity(intent);
    }
    private void putData() {
        list = new ArrayList<Drawable>();
        list.add(getResources().getDrawable(R.drawable.animals));
        list.add(getResources().getDrawable(R.drawable.clothes));
        list.add(getResources().getDrawable(R.drawable.furnitures));
    }
}
