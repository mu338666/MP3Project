package com.example.mu338.a11_22mp3recycledb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public interface ClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}


