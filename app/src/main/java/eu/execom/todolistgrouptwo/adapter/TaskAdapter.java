package eu.execom.todolistgrouptwo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.view.TaskItemView;
import eu.execom.todolistgrouptwo.view.TaskItemView_;


@EBean
public class TaskAdapter extends BaseAdapter {

    @RootContext
    Context context;


    private final List<Task> tasks = new ArrayList<>();

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Task task = getItem(position);
        if (convertView == null) {
            return TaskItemView_.build(context).bind(task);
        } else {
            return ((TaskItemView) convertView).bind(task);
        }
    }

    public void setTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    public void addTask(Task task) {
        tasks.add(task);
        notifyDataSetChanged();
    }

    public void removeTask(Task task){
        tasks.remove(task);
        notifyDataSetChanged();
    }

    public void removeTask(int pos){
        tasks.remove(pos);
        notifyDataSetChanged();
    }

   /* public void updateTask(Task oldTask, Task newTask){
        //removeTask(task);
        tasks.get(tasks.indexOf(oldTask)).setTitle(newTask.getTitle());
        tasks.get(tasks.indexOf(oldTask)).setDescription(newTask.getDescription());
        notifyDataSetChanged();
    }*/
    public void updateTask(int position, Task newTask){
        tasks.add(position, newTask);
        notifyDataSetChanged();
    }


}
