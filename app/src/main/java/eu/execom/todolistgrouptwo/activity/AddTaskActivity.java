package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.constant.Constants;
import eu.execom.todolistgrouptwo.model.Task;


@EActivity(R.layout.activity_add_task)
public class AddTaskActivity extends AppCompatActivity {


    @ViewById
    TextInputEditText title;

    @ViewById
    TextInputEditText description;

    @ViewById
    CheckBox checkBox;

    @Extra("taskUpdate")
    String taskJson;

    private long previousTaskId = -1;


    @AfterViews
    void checkForRequest() {
        try {
            String request = getIntent().getExtras().get(Constants.DML_TYPE).toString();
            if (request.equals(Constants.UPDATE)) {
                final Gson gson = new Gson();
                final Task task = gson.fromJson(taskJson, Task.class);

                previousTaskId = task.getId();

                title.setText(task.getTitle());
                description.setText(task.getDescription());

                checkBox.setChecked(false);
                if(task.getFinished()) checkBox.setChecked(true);
            }

        } catch (NullPointerException e) {
            Log.e("exception", e.getMessage());
        }
    }


    @Click
    void saveTask() {
        if(title.getText().toString().trim().equals("")){
            Toast.makeText(this, "Task title must not be empty!", Toast.LENGTH_SHORT).show();
        }
        else{
            final Task task = new Task(title.getText().toString(),
                description.getText().toString());
            if(checkBox.isChecked()) task.setFinished(true);

            final Intent intent = new Intent();
            final Gson gson = new Gson();
            intent.putExtra("task", gson.toJson(task));
            intent.putExtra("oldTaskID", Long.toString(previousTaskId));
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Click
    void cancel(){
        finish();
    }
    
}
