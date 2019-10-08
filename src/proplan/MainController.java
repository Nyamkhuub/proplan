package proplan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {
    public static Node draggingButton = null;
    public static String parent = null;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu addTask;

    @FXML
    private MenuItem changePassword;

    @FXML
    private MenuItem logout;

    @FXML
    private TitledPane first;

    @FXML
    private TitledPane second;

    @FXML
    private TitledPane third;

    private JsonObject mainData;

    @FXML
    void initialize() {
        this.first.setPrefWidth(Main.MAINWIDTH/3);
        this.second.setPrefWidth(Main.MAINWIDTH/3);
        this.third.setPrefWidth(Main.MAINWIDTH/3);

        this.first.setMaxHeight(Main.MAINHEIGHT);
        this.second.setMaxHeight(Main.MAINHEIGHT);
        this.third.setMaxHeight(Main.MAINHEIGHT);
        this.newTask();
        JsonParser parser = new JsonParser();

        JsonElement data = null;
        try {
            //data = parser.parse(new FileReader("./src/data.json"));
            this.mainData = LoginPage.user.getData();
            setContent(this.first, "todo");
            setContent(this.second, "inprocess");
            setContent(this.third, "done");
            LoginPage.state = true;
        } catch (Exception e) {
            e.printStackTrace();
            LoginPage.state = false;
        }
        handleDrag(this.first);
        handleDrag(this.second);
        handleDrag(this.third);
    }

    /**
     * @author Nyamkhuu.B
     *
     * @param pane
     * @param state
     */
    private void setContent(TitledPane pane, String state) {
        JsonArray array = this.mainData.get(state).getAsJsonObject().getAsJsonArray("contents");
        VBox elements = new VBox(15);

        for(JsonElement object : array) if(!object.isJsonNull()) {
            JsonObject temp = object.getAsJsonObject();
            Node element = new Node(temp, this, state);
            elements.getChildren().add(element);

        }
        pane.setContent(elements);
        pane.setText(this.mainData.get(state).getAsJsonObject().get("name").getAsString());
    }

    /**
     * @author Nyamkhuu.B
     *
     * @param pane
     */
    private void handleDrag(TitledPane pane) {
        pane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if(dragboard.hasContent(DragElement.AddNode) && draggingButton.getParent() != pane) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
        });

        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if(dragboard.hasContent(DragElement.AddNode)) {
                    ((VBox)draggingButton.getParent()).getChildren().remove(draggingButton);
                    try {
                        int id = ((VBox)pane.getContent()).getChildren().size() + 1;

                        draggingButton.changeSection(LoginPage.user, draggingButton.parent, true, 0);
                        draggingButton.changeSection(LoginPage.user, pane.getText().replaceAll(" ", ""), false, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ((VBox)pane.getContent()).getChildren().add(draggingButton);
                }
            }
        });
    }

    /**
     * @author Nyamkhuu.B
     */
    private void newTask() {
        Label label = new Label("Add task");
        label.setOnMouseClicked(event -> {
            System.out.println("testing...");
        });
        this.addTask.setGraphic(label);
    }
}
