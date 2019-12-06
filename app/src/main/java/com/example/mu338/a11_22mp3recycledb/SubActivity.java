package com.example.mu338.a11_22mp3recycledb;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {

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


    static ImageButton btnPlay, btnPause;
    static TextView txtMP3, txtTime;
    static SeekBar pbMP3;

    static MediaPlayer mediaPlayer;
    static String selectedMP3;

    static final String MP3_PATH = Environment.getExternalStorageDirectory().getPath()+"/";

    // 리사이클
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MainAdapter mainAdapter;

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
        setContentView(R.layout.activity_choice);
        setTitle("Mukwon's MP3 Choice");

        /*
        // getIntent 속에 값이 들어있음.
        Intent intent = getIntent();
        number1 = intent.getIntExtra("number1", 0);
        number2 = intent.getIntExtra("number2", 0);
        */

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

        // 리사이클 onCreate

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);


        myDBHelper = new MyDBHelper(this);


        mainAdapter = new MainAdapter(R.layout.activity_sub, list);

        recyclerView.setAdapter(mainAdapter);

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {


                if(mediaPlayer != null){
                    mediaPlayer.reset();
                }

                selectedMP3 = list2.get(position);

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
                                        txtTime.setText(selectedMP3 + " 진행 시간 : " + simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
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

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);


        finish();
    }
}
    /*
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        sum = number1 + number2;

        intent.putExtra("sum", sum);

        // 자기를 부른 액티비티한테 다시 돌아가는것
        setResult(1001, intent);

        finish(); // 자기가 죽으면서 원위치로 돌아감.

    }
    */

