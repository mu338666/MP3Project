package com.example.mu338.a11_22mp3recycledb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {
                                    // 리사이클러뷰 어댑터, 메인 어댑터에서 제공되는 내부클래스를 쓰는것, CustomViewHolder는 내가 직접 만드는것.
                                    // 3개의 메소드인데, 리스트뷰 메소드 4개가 포함되어있음.

    // 1. private Context context : onCreateViewHolder에서 ViewGroup으로 제공이 된다.
    private int layout;
    private ArrayList<MyData> list;
    private ArrayList<String> list2;
    private ArrayList<Integer> list3;

    private Context context;

    static MainAdapter.CustomViewHolder customViewHolder;

    // 데이터베이스


    public MainAdapter(int layout, ArrayList<MyData> list) {
        this.layout = layout;
        this.list = list;
    }

    public MainAdapter(int layout, ArrayList<MyData> list, ArrayList<Integer> list3) {
        this.layout = layout;
        this.list = list;
        this.list3 = list3;
    }

    public MainAdapter(int layout, ArrayList<MyData> list, ArrayList<String> list2, ArrayList<Integer> list3) {
        this.layout = layout;
        this.list = list;
        this.list2 = list2;
        this.list3 = list3;
    }

    // 뷰 홀더에 있는 화면을 객체화해서 해당된 viewHolder 리턴한다.
    @NonNull
    @Override // getView와 같음.
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int position) {

        // 이 view는 밑 내부클래스 생성자인 itemView에 넘겨줌. 레이아웃 인플레이터
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        // 해당된 뷰 홀더의 아이디를 찾는다.
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    // customViewHolder : 뷰 홀더의 정보가 들어옴. 값을 넣는다.
    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder customViewHolder, final int position) {


        customViewHolder.celeName.setText(list.get(position).getCeleName());
        customViewHolder.celeMusic.setText(list.get(position).getCeleMusicName());

        // Log.d("dd", String.valueOf(list.get(position).getAlbumImage()));
        customViewHolder.albumImage.setImageResource(MainActivity.list3.get(position));


        customViewHolder.itemView.setTag(position);

        customViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View viewDialog = v.inflate(v.getContext(), R.layout.dialog, null);

                ImageView diologImage = viewDialog.findViewById(R.id.dialogImage);

                TextView dialogMN = viewDialog.findViewById(R.id.dialogMN);
                TextView dialogCN = viewDialog.findViewById(R.id.dialogCN);
                TextView dialogAL = viewDialog.findViewById(R.id.dialogAL);
                TextView dialogDT = viewDialog.findViewById(R.id.dialogDT);
                TextView dialogGR = viewDialog.findViewById(R.id.dialogGR);

                MyData myData1 = list.get(position);
                diologImage.setImageResource(list3.get(position));

                dialogMN.setText(myData1.getCeleMusicName());
                dialogCN.setText(myData1.getCeleName());
                dialogAL.setText(myData1.getAlbumName());
                dialogDT.setText(myData1.getDate());
                dialogGR.setText(myData1.getGenre());

                // 다이얼로그 세팅
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());

                dialog.setTitle(" 정보");
                dialog.setIcon(R.mipmap.music);
                dialog.setView(viewDialog); // 이미지가 들어감

                dialog.setPositiveButton("닫기", null);

                dialog.show();
            }
        });

    }

    @Override // 리스트의 사이즈를 준다.
    public int getItemCount() {
        return (list != null) ? (list.size()) : (0); // 리스트에 값이 들어있으면 ~
    }


    // =========== 내부 클래스

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener{

        final TextView celeMusic;
        final TextView celeName;

        final ImageView albumImage;
        final ImageButton imageButton;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            celeMusic = itemView.findViewById(R.id.celeMusic);
            celeName = itemView.findViewById(R.id.celeName);

            albumImage = itemView.findViewById(R.id.albumImage);
            imageButton = itemView.findViewById(R.id.imageButton);

            itemView.setOnCreateContextMenuListener(this);
        }

        // 컨텍스트 메뉴 정의
        @Override
        public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {

            final int position = (int)v.getTag();
            MenuItem edit = menu.add(0, 1001, 0, "수정");
            MenuItem delete = menu.add(0, 1002, 0, "삭제");
            MenuItem save = menu.add(0, 1003, 0, "저장");

            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            save.setOnMenuItemClickListener(this);

        }


        // 컨텍스트 메뉴 선택시 이벤트
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()){

                case 1001 :

                    AlertDialog.Builder dialog = new AlertDialog.Builder(itemView.getContext());

                    View viewDialog = LayoutInflater.from(itemView.getContext()).inflate(R.layout.edit_dialog, null, false);

                    dialog.setTitle(" 수정");
                    dialog.setIcon(R.mipmap.music);
                    dialog.setView(viewDialog); // 이미지가 들어감

                    final EditText editSongName = viewDialog.findViewById(R.id.editSongName);
                    final EditText editCeleName = viewDialog.findViewById(R.id.editCeleName);

                    MyData myData1 = list.get(getAdapterPosition());
                    // diologImage.setImageResource(myData1.getAlbumImage());

                    editSongName.setText(myData1.getCeleMusicName());
                    editCeleName.setText(myData1.getCeleName());


                    // 다이얼로그 세팅

                    dialog.setNegativeButton("수정", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try{

                                MainActivity.sqLiteDatabase = MainActivity.myDBHelper.getWritableDatabase();

                                String editStr = "UPDATE groupTBL SET celeMusicName =" + editSongName.getText().toString() + " WHERE celeName = '" + editCeleName.getText().toString() + "';";

                                String str1 = editSongName.getText().toString();
                                String str2 = editCeleName.getText().toString();

                                MyData myData = new MyData(str1, str2);

                                list.set(getAdapterPosition(), myData);

                                celeMusic.setText(myData.getCeleMusicName());

                                MainActivity.sqLiteDatabase.execSQL(editStr);

                                notifyDataSetChanged();

                                MainActivity.sqLiteDatabase.close();

                                // MainActivity.listAddInsert();



                            }catch (SQLiteException e){
                                e.printStackTrace();
                            }

                        }

                    });

                    dialog.setPositiveButton("닫기", null);

                    dialog.show();


                    break;

                case 1002 :

                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(itemView.getContext());

                    View viewDialog2 = LayoutInflater.from(itemView.getContext()).inflate(R.layout.delete_dialog, null, false);

                    dialog2.setTitle(" 삭제");
                    dialog2.setIcon(R.mipmap.music);
                    dialog2.setView(viewDialog2); // 이미지가 들어감

                    final EditText delSongName = viewDialog2.findViewById(R.id.delSongName);

                    MyData myData2 = list.get(getAdapterPosition());
                    // diologImage.setImageResource(myData1.getAlbumImage());

                    delSongName.setText(myData2.getCeleMusicName());

                    dialog2.setNegativeButton("삭제", new DialogInterface.OnClickListener(){


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try{

                                MainActivity.sqLiteDatabase = MainActivity.myDBHelper.getWritableDatabase();

                                MainActivity.sqLiteDatabase.execSQL("DELETE FROM groupTBL WHERE celeMusicName = '" + list.get(getAdapterPosition()).getCeleMusicName()+ "';");

                                list.remove(getAdapterPosition());
                                list2.remove(getAdapterPosition());
                                list3.remove(getAdapterPosition());

                                notifyDataSetChanged();

                                MainActivity.sqLiteDatabase.close();

                                // MainActivity.listAddInsert();

                            }catch (SQLiteException e){
                                e.printStackTrace();
                            }

                        }

                    });

                    dialog2.setPositiveButton("닫기", null);

                    dialog2.show();

                    break;

                case 1003 :

                    SubActivity.list.add(new MyData(celeMusic.getText().toString(), celeName.getText().toString()));

                    break;



            }


            return false;
        }
    }




}
