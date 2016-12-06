package eu.execom.todolistgrouptwo.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.model.Task;


@EViewGroup(R.layout.view_item_task)
public class TaskItemView extends LinearLayout {

    @ViewById
    TextView title;

    @ViewById
    TextView description;


    public TaskItemView(Context context) {
        super(context);
    }


    public TaskItemView bind(Task task) {
        title.setText(task.getTitle());
        description.setText(task.getDescription());
        if(task.isFinished()){
            title.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_check_circle_checked_24dp, 0, 0, 0);
        }
        else{
            title.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_check_circle_faded_24dp, 0, 0, 0);
        }

        return this;
    }
}
