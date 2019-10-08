package proplan;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.control.Notifications;
import proplan.restapi.User;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends VBox {
    private TextField email;
    private PasswordField password;
    private Button submit;
    public static User user;
    static boolean state = false;
    public static final Pattern VALIDMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALIDMAIL.matcher(emailStr);
        return matcher.find();
    }
    LoginPage() {
        user = new User();
        Label title = new Label("PROPLAN");
        title.setFont(new Font(30));
        email = new TextField();
        password = new PasswordField();
        email.setPromptText("Email...");
        email.setPrefHeight(30);
        password.setPromptText("Password...");
        password.setPrefHeight(30);
        submit = new Button("Submit");
        submit.setPrefHeight(30);
        submit.setDisable(true);
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && validate(email.getText())) {
                submit.setDisable(false);
            }
        });
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!password.getText().isEmpty() && validate(newValue)) {
                submit.setDisable(false);
            }
        });
        VBox buttons = new VBox(10);
        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Popup popup = new Popup();
                //ProgressIndicator pi = new ProgressIndicator();
                //VBox box = new VBox();
                //box.setAlignment(Pos.CENTER);
                //box.setPrefSize(Main.MAINWIDTH, Main.MAINHEIGHT);
                //box.getChildren().add(pi);
                //box.setStyle("-fx-background-color: white; -fx-opacity: 0.8");
                //popup.getContent().add(box);
                //popup.show(Main.loginStage);
                JsonObject userData = null;
                try {
                    userData = user.signIn(email.getText(), password.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(userData == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Email/password is invalid!, please try again");
                    alert.show();
                    //popup.hide();
                }
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("main.fxml"));
                    Main.loginStage.setTitle("Planning simple program");
                    Main.loginStage.setScene(new Scene(root, Main.MAINWIDTH, Main.MAINHEIGHT));

                    if(state) {
                        Notifications.create().text("Data source load, successfully!").position(Pos.TOP_RIGHT).showInformation();
                    } else {
                        Notifications.create().text("Data source couldn't load!").position(Pos.TOP_RIGHT).showError();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                submit.requestFocus();
            }
        });
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.getChildren().add(submit);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);
        this.setPadding(new Insets(0, 400, 0, 400));
        this.getChildren().addAll(title, email, password, buttons);
    }
}
