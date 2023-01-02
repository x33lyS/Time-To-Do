package com.example.todoapp;

import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;

public interface OnDialogCloseListner {

    void onDialogClose(DialogInterface dialogInterface);

    boolean onCreateOptionMenu(Menu menu);

}
