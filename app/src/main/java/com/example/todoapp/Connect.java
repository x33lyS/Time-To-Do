package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Connect extends AppCompatActivity {

    public static final String TAG = "Connect";
    private RecyclerView recyclerView;



    public static Connect newInstance() {
        return new Connect();
    }

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button back = findViewById(R.id.Connectback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Connect.this, MainActivity.class);
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


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://timetodo-9a390-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Tasks");
        // Lecture de la valeur unique à partir de la base de données
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Object description = childSnapshot.getValue();
                    Log.v(TAG, "Value is: " + description);
                }
                recyclerView = findViewById(R.id.recycler_view);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Traitement de l'erreur ici...
            }
        });
    }

    public void show(FragmentManager supportFragmentManager, String tag) {
    }
}


