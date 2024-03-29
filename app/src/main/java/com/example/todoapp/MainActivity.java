package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnDialogCloseListner, PopupMenu.OnMenuItemClickListener {

    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;

    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;
    public static String password = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
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
        setUpAlarm();
    }

    private void setUpAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 16);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
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
                        Intent intent = new Intent(MainActivity.this, About.class);
                        startActivity(intent);
                        return true;
                    case R.id.item2:
                        showDeleteAllTasksDialog();
                        return true;
                    case R.id.menuItem1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final EditText input = new EditText(MainActivity.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        builder.setView(input);
                        builder.setMessage("Password")
                                .setTitle("Write your password")
                                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        password = input.getText().toString();
                                        if(password.equals("1234") || password.equals("4321")){
                                            Intent Connectintent = new Intent(MainActivity.this, Connect.class);
                                            startActivity(Connectintent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
    private void showDeleteAllTasksDialog() {
        if (adapter.getItemCount() == 0) {
            Toast.makeText(this, "No tasks to delete", Toast.LENGTH_SHORT).show();
        } else {
            // Créez un nouvel AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Définissez le titre et le message du Dialog
            builder.setTitle("Delete all tasks")
                    .setMessage("Are you sure you want to delete all tasks?")
                    // Ajoutez un bouton "Oui" qui appelle la méthode deleteAllTasks
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteAllTasks();
                        }
                    })
                    // Ajoutez un bouton "Non" qui ferme simplement le Dialog
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Ne faites rien, simplement fermez le Dialog
                        }
                    });
            // Créez et affichez le Dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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
        builder.setMessage("You will exit the app")
                .setTitle("Warning !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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