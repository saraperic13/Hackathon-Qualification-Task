package eu.execom.todolistgrouptwo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.List;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.adapter.TaskAdapter;
import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.constant.Constants;
import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.preference.UserPreferences_;


@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    protected static final int ADD_TASK_REQUEST_CODE = 42;
    protected static final int UPDATE_TASK_REQUEST_CODE = 43;
    protected static final int LOGIN_REQUEST_CODE = 420; // BLAZE IT

    private List<Task> tasks;

    @ViewById
    FloatingActionButton addTask;

    @ViewById
    ListView listView;

    @Bean
    TaskAdapter adapter;

    @Pref
    UserPreferences_ userPreferences;

    @RestService
    RestApi restApi;

    @Extra("oldTaskID")
    String oldTaskID;

    private int taskPosition;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        logout();
        return true;
    }


    @AfterViews
    @Background
    void checkUser() {
        if (!userPreferences.accessToken().exists()) {
            LoginActivity_.intent(this).startForResult(LOGIN_REQUEST_CODE);
            return;
        }
        try {
            tasks = restApi.getAllTasks();
        } catch (RestClientException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        initData();
    }


    @UiThread
    void initData() {
        try{
            listView.setAdapter(adapter);
            adapter.setTasks(tasks);
        } catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }


    @Click
    void addTask() {
        AddTaskActivity_.intent(this).startForResult(ADD_TASK_REQUEST_CODE);
    }


    @OnActivityResult(ADD_TASK_REQUEST_CODE)
    @Background
    void onResult(int resultCode, @OnActivityResult.Extra String task) {
        if (resultCode == RESULT_OK) {
            final Gson gson = new Gson();
            final Task newTask = gson.fromJson(task, Task.class);

            try {
                final Task newNewTask = restApi.createTask(newTask);
                tasks = restApi.getAllTasks();
                onTaskCreated(newNewTask);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }


    @UiThread
    void onTaskCreated(Task task) {
        adapter.setTasks(tasks);
        Toast.makeText(this, "Task Created", Toast.LENGTH_SHORT).show();
    }


    @OnActivityResult(LOGIN_REQUEST_CODE)
    void onLogin(int resultCode, @OnActivityResult.Extra("token") String token) {
        if (resultCode == RESULT_OK) {
            userPreferences.accessToken().put(token);
            checkUser();
        }
        else{
            finish();
        }
    }


    void logout(){
        userPreferences.userId().remove();
        userPreferences.accessToken().remove();
        checkUser();
    }


    @ItemClick
    public void listViewItemClicked(final int position){
        final CharSequence[] items = {Constants.UPDATE, Constants.DELETE};
        final Task task = adapter.getItem(position);

        taskPosition = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) onUpdateRecord(task);
                else createDeleteDialog(task);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void onUpdateRecord(Task task) {
        Intent intent = new Intent(this, AddTaskActivity_.class);
        final Gson gson = new Gson();
        intent.putExtra("taskUpdate", gson.toJson(task));
        intent.putExtra(Constants.DML_TYPE, Constants.UPDATE);

        startActivityForResult(intent, UPDATE_TASK_REQUEST_CODE);
    }


    @Background
    void update(Task updatedTask){
        try {
            restApi.updateTask(updatedTask, updatedTask.getId());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }


    @OnActivityResult(UPDATE_TASK_REQUEST_CODE)
    void onUpdate(int resultCode, @OnActivityResult.Extra("task") String task, @OnActivityResult.Extra("oldTaskID") String ID) {
        if (resultCode == RESULT_OK) {
            final Gson gson = new Gson();
            final Task updatedTask = gson.fromJson(task, Task.class);

            tasks.remove(taskPosition);
            adapter.removeTask(taskPosition);

            updatedTask.setId(Long.parseLong(ID));
            tasks.add(taskPosition, updatedTask);
            adapter.updateTask(taskPosition, updatedTask);
            update(updatedTask);
            Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
        }
    }


    @Background
    void delete(Task task){
        try{
            restApi.delete(task.getId());
        }catch (Exception e){
            Log.e("DELETE", e.getMessage(), e);
            return;
        }
    }


    private void createDeleteDialog(final Task task){
        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(this);
        deleteDialogOk.setTitle("Delete Task?");
        deleteDialogOk.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tasks.remove(task.getId());
                        adapter.removeTask(task);
                        delete(task);
                        Toast.makeText(HomeActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        deleteDialogOk.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        deleteDialogOk.show();
    }

}
