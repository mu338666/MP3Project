package com.example.mu338.a11_22mp3recycledb;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private String[] celeName = { "아이유", "태연", "태연", "러블리즈", "볼빨간사춘기", "볼빨간사춘기", "아이콘(IKON)", "김동률",
                                        "한동근", "DAY6", "OutSider", "오마이걸", "JULY", "DAY6", "윤하"};

    private String[] musicName = { "팔레트", "기억을 걷는 시간", "I", "지금, 우리", "싸운날",
                        "나만 안되는 연애", "AirPlane", "출발", "미치고 싶다", "Dance Dance", "외톨이", "비밀정원", "My Soul", "반드시 웃는다", "소나기"};

    private Integer[] albumImage = { R.drawable.a1 , R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7,
            R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a16 };


    // ======== 다이얼로그 데이터

    private String[] albumName = { "Palette", "My Voice", "I", "Re package", "RED ICKLE",
            "RED PLANET", "Welcome Back", "Monologue", "Your Diary", "Every Day6 May", "Maestro", "비밀정원", "Time", "SUNRIZE", "SuperSonic"};

    private String[] date = { "2017.04.21", "2017.02.28", "2015.10.07", "2017.05.02", "2016.04.22", "2016.08.29",
            "2015.11.16", "2008.01.24", "2017.05.05", "2017.05.08", "2009.06.01", "2018.01.09", "2007.04.25", "2017.06.07", "2012.07.03"};

    private String[] genre = { "R&B / Soul" , "록 / 메탈", "발라드", "댄스", "인디음악", "인디음악", "랩 / 힙합",
            "인디음악", "발라드", "록 / 메탈", "랩 / 힙합", "댄스", "클래식", "록 / 메탈", "발라드"};


    // MP3
    static ImageButton btnPlay, btnPause;
    static TextView txtMP3, txtTime;
    static SeekBar pbMP3;

    static MediaPlayer mediaPlayer;
    static String selectedMP3;

    static final String MP3_PATH = Environment.getExternalStorageDirectory().getPath()+"/";

    static final String PATH = Environment.getExternalStorageDirectory().getPath()+"/MusicImage/";

    // 리사이클
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private static MainAdapter mainAdapter;

    // DataBase
    public static MyDBHelper myDBHelper;
    public static SQLiteDatabase sqLiteDatabase;

    // 공용
    static ArrayList<MyData> list = new ArrayList<>();
    static ArrayList<String> list2 = new ArrayList<>();
    static ArrayList<Integer> list3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Mukwon's MP3");

        recyclerView = findViewById(R.id.recyclerView);

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        txtMP3 = findViewById(R.id.txtMP3);
        txtTime = findViewById(R.id.txtTime);
        pbMP3 = findViewById(R.id.pbMP3);

        // MP3 onCreate
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);

        File[] files = new File(MP3_PATH).listFiles();

        if(list2 != null){
            list2.removeAll(list2);
        }

        for( File file : files ){
            String filename = file.getName();
            String extendName = filename.substring(filename.length() - 3);

            if(extendName.equals("mp3")){
                list2.add(filename);
            }
        }


        for(int i = 0 ; i < albumImage.length ; i++){
            list3.add(albumImage[i]);
        }


        // 리사이클 onCreate

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);


        myDBHelper = new MyDBHelper(this);

        listAddInsert();

        showInsert();

        mainAdapter = new MainAdapter(R.layout.activity_sub, list, list2, list3);

        recyclerView.setAdapter(mainAdapter);

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);

        pbMP3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                if(mediaPlayer != null){
                    mediaPlayer.reset();
                }

                selectedMP3 = list2.get(position);
                // selectedMP3 = list2.get(position);

                try {

                    mediaPlayer = new MediaPlayer();

                    mediaPlayer.setDataSource(MP3_PATH + selectedMP3);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    txtMP3.setText("실행 중인 음악 : " + selectedMP3);

                    Thread thread = new Thread() {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

                        @Override
                        public void run() {
                            if (mediaPlayer == null) {
                                mediaPlayer.stop();
                                return;
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 1. 총 노래 재생시간
                                    txtTime.setText(selectedMP3 + "재생시간 " + mediaPlayer.getDuration());
                                    pbMP3.setMax(mediaPlayer.getDuration());
                                }
                            });

                            while (mediaPlayer.isPlaying()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbMP3.setProgress(mediaPlayer.getCurrentPosition());
                                        txtTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                                    }
                                });

                                SystemClock.sleep(200);

                            }

                        }
                    };

                    thread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }

    // 옵션 메뉴 인플레이터 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater(); // 메뉴를 객체로 만들겠다. xml을 객체화 시키려고.
        menuInflater.inflate(R.menu.menu1, menu);

        return true;
    }

    // 옵션 메뉴를 클릭을 하면 생기는 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.itemInsert :

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                View viewDialog = View.inflate(MainActivity.this, R.layout.new_song_dialog, null);

                dialog.setTitle(" 검색");
                dialog.setIcon(R.mipmap.music);
                dialog.setView(viewDialog);

                final EditText mainSongName = findViewById(R.id.mainSongName);

                dialog.setNegativeButton(" 검색", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }

                });

                dialog.setPositiveButton("닫기", null);

                dialog.show();

                break;

            case R.id.itemSave :

                Intent intent = new Intent(MainActivity.this, SubActivity.class);

                startActivityForResult(intent, 1004);

                break;

            case R.id.itemSelect:

                list.removeAll(list);

                sqLiteDatabase = myDBHelper.getReadableDatabase();

                Cursor cursor;

                cursor = sqLiteDatabase.rawQuery("SELECT * FROM groupTBL;", null);

                while(cursor.moveToNext()){
                    list.add(new MyData(cursor.getString(0), cursor.getString(1), cursor.getString(2)
                            , cursor.getString(3), cursor.getString(4)));
                }
                mainAdapter.notifyDataSetChanged();
                cursor.close();
                sqLiteDatabase.close();

                break;


            default: break;

        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnPlay :

                try {

                    mediaPlayer = new MediaPlayer();

                    mediaPlayer.setDataSource(MP3_PATH + selectedMP3);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    btnPlay.setClickable(false);
                    btnPause.setClickable(true);
                    txtMP3.setText("실행 중인 음악 : "+selectedMP3);

                    Thread thread = new Thread(){

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

                        @Override
                        public void run() {
                            if(mediaPlayer == null){
                                return;
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 1. 총 노래 재생시간
                                    txtTime.setText(selectedMP3 + "재생시간 "+mediaPlayer.getDuration());
                                    pbMP3.setMax(mediaPlayer.getDuration());
                                }
                            });

                            while (mediaPlayer.isPlaying()){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbMP3.setProgress(mediaPlayer.getCurrentPosition());
                                        txtTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                                    }
                                });

                                SystemClock.sleep(200);

                            }

                        }
                    };

                    thread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btnPause :

                // mediaPlayer.pause();
                mediaPlayer.stop();
                // btnPlay.setClickable(true);
                //btnPause.setClickable(false);

                txtMP3.setText("실행 정지된 음악 : "+selectedMP3);

                pbMP3.setProgress(0);
                txtTime.setText("진행 시간 : "+txtTime);

                break;
        }

    }


    public void listAddInsert(){

        try{

            sqLiteDatabase = myDBHelper.getWritableDatabase();

            for(int i = 0 ; i < musicName.length ; i++){
                sqLiteDatabase.execSQL("INSERT INTO groupTBL Values ('" + musicName[i] +"', '" + celeName[i] + "', " +
                        "'"+ albumName[i] +"', '" + date[i] + "', '"+ genre[i] +"', "+albumImage[i]+");");
                list.add(new MyData(musicName[i], celeName[i], albumName[i], date[i], genre[i]));
            }

            //String insertStr = "INSERT INTO groupTBL values('" + edtName.getText().toString().trim() + "'," +
            // edtNum.getText().toString() + ");";

            // list.add(new MainData(edtName.getText().toString(), edtNum.getText().toString()));

            mainAdapter.notifyDataSetChanged();

            // sqLiteDatabase.execSQL(insertStr);
            sqLiteDatabase.close();


        }catch (SQLiteException e){
            e.printStackTrace();
        }

    }


    static public void showInsert(){

        if(list != null){
            list.removeAll(list);
        }

        sqLiteDatabase = myDBHelper.getReadableDatabase();

        Cursor cursor;

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM groupTBL;", null);

        while(cursor.moveToNext()){
            list.add(new MyData(cursor.getString(0), cursor.getString(1), cursor.getString(2)
            , cursor.getString(3), cursor.getString(4)));
        }

        cursor.close();
        sqLiteDatabase.close();

    }


    public void toastDisplay(String message){

        Toast.makeText(getApplicationContext(),message,
                Toast.LENGTH_SHORT).show();

    }

}
