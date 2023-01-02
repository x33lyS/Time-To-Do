package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListner, PopupMenu.OnMenuItemClickListener {

    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;

    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        Button menuButton = findViewById(R.id.menuButton);
        myDB = new DataBaseHelper(MainActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB , MainActivity.this);

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

    }

    public void openMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        menu.inflate(R.menu.example_menu);
        menu.show();
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
//                        AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
                        Intent intent = new Intent(MainActivity.this, About.class);
                        startActivity(intent);
                        return true;
                    case R.id.item2:
                        Log.d("TAG", "onMenuItemClick: item2");
                        // Faites quelque chose lorsque l'élément de menu 2 est sélectionné
                        return true;
                    default:
                        return false;
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed(){
        display();
    }
    public void display(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vous allez quitter l'application")
                .setTitle("Attention !")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }
                }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }
}