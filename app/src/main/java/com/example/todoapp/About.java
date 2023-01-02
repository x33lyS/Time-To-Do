package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

public class About extends AppCompatActivity {

    public static final String TAG = "About";

    public static About newInstance(){
        return new About();
    }

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button back = findViewById(R.id.back);
        TextView text = findViewById(R.id.textView);
        TextView creator = findViewById(R.id.textView2);
        String paragraph = "\"This is a simple to-do list app. It allows you to add, switch left to edit, and switch right to delete tasks. You can also mark tasks as complete and delete all task in one button click. ";
        String creatorsText = "Creators\n" + "This app was created by: Florian Lejosne and Adam Haouzi. ";
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, MainActivity.class);
                startActivity(intent);
                v.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // L'utilisateur appuie sur la vue, modifiez son apparence pour simuler un hover
                                view.setTranslationX(10);
                                return true;
                            case MotionEvent.ACTION_UP:
                                // L'utilisateur relâche la vue, restaurez son apparence originale
                                view.setTranslationX(-10);
                                return true;
                            default:
                                return false;
                        }
                    }
                });


            }
        });
        text.setText(paragraph);
        creator.setText(creatorsText);
    }

    public void show(FragmentManager supportFragmentManager, String tag) {
    }
}