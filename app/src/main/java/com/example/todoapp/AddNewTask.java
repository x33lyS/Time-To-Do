package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";


    //widgets
    private EditText mEditText;
    private Button mSaveButton;

    private DataBaseHelper myDb;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask , container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);

        myDb = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if (task.length() > 0 ){
                mSaveButton.setEnabled(false);
            }
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = prefs.getString("task", "");
        if (name != null) {
            mEditText = view.findViewById(R.id.edittext);
            mEditText.setText(name);
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.toString().equals("")){
                   mSaveButton.setEnabled(false);
                   mSaveButton.setBackgroundColor(Color.GRAY);
               }else{
                   mSaveButton.setEnabled(true);
                   mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("task", "" + mEditText.getText());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String text = mEditText.getText().toString();

                if (finalIsUpdate){
                   myDb.updateTask(bundle.getInt("id") , text);
               }else{
                   ToDoModel item = new ToDoModel();
                   item.setTask(text);
                   item.setStatus(0);
                   myDb.insertTask(item);
               }
               dismiss();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("task").apply();
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://timetodo-9a390-default-rtdb.europe-west1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Task");
                myRef.setValue(mEditText.getText().toString());

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "Value is: " + value);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

            }
        });
    }

    public boolean onCreateOptionMenu(Menu menu){
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
