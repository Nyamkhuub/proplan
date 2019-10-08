package proplan.windows;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import proplan.restapi.User;

public class AddTask extends VBox {
    private TextField title;
    private TextArea body;
    private Button submit, close;
    private Stage ownStage;
    private User activeUser;
    private int id;
    private boolean isUpdate = false;
    private String ownSection;

    public boolean isEdited = false;

    public AddTask(String title, String body, int id, Stage ownStage, User activeUser, String section) {
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        if(!title.isEmpty() && !body.isEmpty()) {
            isUpdate = true;
        }
        this.ownStage = ownStage;
        this.activeUser = activeUser;
        this.id = id;
        this.ownSection = section;
        this.title = new TextField(title);
        this.body = new TextArea(body);
        HBox buttons = new HBox(15);
        submit = new Button("Add");
        close = new Button("Close");
        this.config();
        buttons.getChildren().addAll(submit, close);
        this.getChildren().addAll(this.title, this.body, buttons);
    }

    private void config() {
        close.setPrefWidth(70);
        close.setPrefHeight(35);
        submit.setPrefWidth(70);
        submit.setPrefHeight(35);
        this.submit.setDisable(true);
        this.title.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)) {
                this.submit.setDisable(false);
            }
        }));
        this.body.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)) {
                this.submit.setDisable(false);
            }
        }));
        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AddTask.this.modifyTask();
            }
        });
        close.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AddTask.this.ownStage.close();
            }
        });
    }

    /**
     * If isUpdate is true, this function will update task.
     * If isUpdate is false, this function will create new task.
     */
    private void modifyTask() {
        JsonObject body = this.getFullData();
        try {
            this.activeUser.request(body, User.LOGIN_URL, "POST", false);
            this.isEdited = true;
            Notifications.create().text(this.isUpdate? "Update this task, successfully!" : "Add new task to todo, successfully!").position(Pos.TOP_RIGHT).showInformation();
        } catch (Exception e) {
            e.printStackTrace();
            this.isEdited = false;
            Notifications.create().text("Sorry system couldn't create new Task, please try again").position(Pos.TOP_RIGHT).showError();
        } finally {
            this.ownStage.close();
        }
    }

    /**
     *
     * @return this json object for request body to rest api
     */
    private JsonObject getFullData() {
        JsonObject body = this.getData();
        body.addProperty("token", this.activeUser.mainData.get("idToken").getAsString());
        body.addProperty("section", this.ownSection);
        body.addProperty("id", this.id);
        if(this.isUpdate) {
            body.addProperty("type", "update");
        } else {
            body.addProperty("type", "add");
        }
        return body;
    }

    /**
     *
     * @return value of title and body
     */
    public JsonObject getData() {
        JsonObject data =new JsonObject();
        data.addProperty("title", this.title.getText());
        data.addProperty("body", this.body.getText());
        return data;
    }
}
