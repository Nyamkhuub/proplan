package proplan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public final static int MAINWIDTH = 1200;
    public final static int MAINHEIGHT = 600;
    public static Stage loginStage;

    @Override
    public void start(Stage primaryStage) {
        loginStage = new Stage();
        LoginPage loginPage = new LoginPage();
        Scene loginScene = new Scene(loginPage, MAINWIDTH, MAINHEIGHT);
        loginStage.setMinHeight(MAINHEIGHT);
        loginStage.setMinWidth(MAINWIDTH);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Login page");
        loginStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}