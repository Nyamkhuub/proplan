package proplan;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import proplan.restapi.User;


public class Node extends VBox {
    private EventHandler<DragEvent> dragOver, dragDropped;

    private TextField title;
    private Text body;
    private JsonObject data;
    private MainController controller;
    public String parent;

    public JsonObject getData() {
        return this.data;
    }

    Node(JsonObject data, MainController controller, String parent) {
        this.title = new TextField(data.get("title").getAsString());
        this.body = new Text(data.get("body").getAsString());
        this.data = data;
        this.parent = parent;
        this.title.setDisable(true);
        this.getChildren().addAll(this.title, this.body);
        this.controller = controller;
        nodeDragHandler();

    }

    public void nodeDragHandler() {
        dragOver = new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        };


        dragDropped = new EventHandler <DragEvent> () {
            @Override
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);
                event.setDropCompleted(true);
                event.consume();
            }
        };

        this.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver (dragOver);
                getParent().setOnDragDropped (dragDropped);

                MainController.draggingButton = Node.this;
                MainController.parent = Node.this.parent;
                ClipboardContent content = new ClipboardContent();
                DragElement element = new DragElement();
                content.put(DragElement.AddNode, element);
                startDragAndDrop(TransferMode.ANY).setContent(content);
                event.consume();
            }
        });

        //Mouse click event handler
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               VBox root = new VBox(15);
               root.setPadding(new Insets(15));
               HBox horizontalBox = new HBox(15);
               Button submit = new Button("save");
               submit.setDisable(true);
               Button close = new Button("close");
               close.setPrefWidth(70);
               close.setPrefHeight(35);
               submit.setPrefWidth(70);
               submit.setPrefHeight(35);
               horizontalBox.getChildren().addAll(submit, close);
               TextField title = new TextField(Node.this.title.getText());
               title.textProperty().addListener(((observable, oldValue, newValue) -> {
                   if(!oldValue.equals(newValue)) {
                       submit.setDisable(false);
                   }
               }));
               TextArea body = new TextArea(Node.this.body.getText());
               body.textProperty().addListener(((observable, oldValue, newValue) -> {
                   if(!oldValue.equals(newValue)) {
                       submit.setDisable(false);
                   }
               }));
               root.getChildren().addAll(title, body, horizontalBox);
               Scene editTextScene = new Scene(root, 400, 400);
               Stage editedStage = new Stage();
               submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent event) {
                       Node.this.data.addProperty("title", title.getText());
                       Node.this.data.addProperty("body", body.getText());
                       Node.this.title.setText(title.getText());
                       Node.this.body.setText(body.getText());
                        editedStage.close();
                   }
               });
               close.setOnMouseClicked(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent event) {
                       editedStage.close();
                   }
               });
               editedStage.setTitle("Edit your plan");
               editedStage.setScene(editTextScene);
               editedStage.setX(event.getX());
               editedStage.setY(event.getY());
               editedStage.show();
            }
        });
    }

    public void changeSection(User data, String section,  boolean isDelete, int id) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("token", data.mainData.get("idToken").getAsString());
        body.addProperty("title", this.title.getText());
        body.addProperty("body", this.body.getText());
        body.addProperty("section", section);
        if(isDelete) {
            body.addProperty("id", this.data.get("id").getAsInt());
            body.addProperty("type", "remove");
        } else {
            body.addProperty("type", "add");
            body.addProperty("id", id);
        }
        data.request(body, data.LOGIN_URL, "POST", false);
    }
}
