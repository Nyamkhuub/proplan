package proplan.windows;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class AddTask extends VBox {
    private TextField title;
    private TextArea body;
    private Button submit;

    AddTask(String title, String body, int id) {
        this.title = new TextField(title);
        this.body = new TextArea(body);
        submit = new Button("Add");
        this.config();
    }

    private void config() {
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

            }
        });
    }
    public JsonObject getData() {
        JsonObject data =new JsonObject();
        data.addProperty("title", this.title.getText());
        data.addProperty("body", this.body.getText());
        return data;
    }
}
