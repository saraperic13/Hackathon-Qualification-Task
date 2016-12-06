package eu.execom.todolistgrouptwo.model;


public class Task {

    private long id;

    private String title;

    private String description;

    private  boolean  finished;


    public Task() {
    }


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.finished = false;
    }


    public Task(String task_name, String description, boolean checked) {
        this.title = task_name;
        this.description = description;
        finished = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public boolean getFinished() {
        return finished;
    }
}
