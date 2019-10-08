package proplan.windows.Stages;

import javafx.stage.Stage;

public class TaskStage extends Stage {
    public final static int SIZE = 400;

    private boolean isUpdate;
    public TaskStage(boolean isUpdate) {
        this.isUpdate = isUpdate;
        this.config();
    }

    private void config() {
        if(isUpdate) {
            this.setTitle("Update task window");
        } else {
            this.setTitle("Add new task window");
        }
    }
}
