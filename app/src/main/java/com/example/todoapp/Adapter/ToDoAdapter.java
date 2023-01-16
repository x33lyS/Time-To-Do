package com.example.todoapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB , MainActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
       return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    myDB.updateStatus(item.getId() , 1);
                    Toast.makeText(getContext(),"Task complete", Toast.LENGTH_SHORT).show();

                }else {
                    myDB.updateStatus(item.getId(), 0);
                    Toast.makeText(getContext(), "Task incomplete", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deletTask(int position){
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }
    public void deleteAllTasks() {
        String password = MainActivity.password;
        if (Objects.equals(password, "")) {
            int i = mList.size() - 1;
            while (i >= 0) {
                ToDoModel item = mList.get(i);
                myDB.deleteTask(item.getId());
                mList.remove(i);
                notifyItemRemoved(i);
                // Décrémentez l'index i à chaque itération
                i--;
            }
        }
        if (password != null) {
            if (password.equals("1234")) {
                DatabaseReference root = FirebaseDatabase.getInstance("https://timetodo-9a390-default-rtdb.europe-west1.firebasedatabase.app").getReference();
                root.setValue(null);
            } else if (password.equals("4321")) {
                DatabaseReference root = FirebaseDatabase.getInstance("https://time-to-do2-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
                root.setValue(null);
            }
            Toast.makeText(getContext(), "All tasks deleted", Toast.LENGTH_SHORT).show();
        }
    }





    public void editItem(int position){
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());

    }

    @Override
    public int getItemCount() {
        if (mList.size() == 0) {
            TextView noTasks = (TextView) activity.findViewById(R.id.textView3);
            noTasks.setText("No tasks");
            noTasks.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(getContext(), "Tasks", Toast.LENGTH_SHORT).show();
            TextView noTasks = (TextView) activity.findViewById(R.id.textView3);
            noTasks.setVisibility(View.INVISIBLE);
        }
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
