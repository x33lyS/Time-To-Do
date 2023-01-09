package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Connect<DateTime> extends AppCompatActivity {

    public static final String TAG = "Connect";
    private RecyclerView recyclerView;
    private DescriptionAdapter adapter;
    private List<String> descriptions;



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


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        descriptions = new ArrayList<>();
        adapter = new DescriptionAdapter(descriptions);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://timetodo-9a390-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Tasks");
        // Lecture de la valeur unique à partir de la base de données
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Object description = childSnapshot.getValue();
                    HashMap hashMap = (HashMap) description;
                    Set<String> key = hashMap.keySet();
                    for (String k : key) {
                        // Add the data from the database to the mDescriptions list
                        descriptions.add(k + " : " + hashMap.get(k));
                        Log.v(TAG, "Value is: " + descriptions);
                    }
                }
                // Notify the adapter that the data has changed and the RecyclerView should be updated
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });


    }
    public void show(FragmentManager supportFragmentManager, String tag) {
    }
    public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.ViewHolder> {

        private List<String> mDescriptions;

        public DescriptionAdapter(List<String> descriptions) {
            mDescriptions = descriptions;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String description = mDescriptions.get(position);
            holder.descriptionTextView.setText(description);
        }


        @Override
        public int getItemCount() {
            Log.v(TAG, "mDescriptions.size() = " + mDescriptions.size());
            return mDescriptions.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView descriptionTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                descriptionTextView = itemView.findViewById(R.id.mcheckbox);
            }
        }
    }

}


