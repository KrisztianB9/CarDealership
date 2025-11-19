package ro.umfst.oop.cardealership;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class DealershipSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dealership.fxml"));

            try {
                Image icon = new Image(getClass().getResourceAsStream("car_icon.png"));
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                System.out.println("Warning: car_icon.png not found. Starting without icon.");
            }

            Scene scene = new Scene(root, 600, 500);

            primaryStage.setTitle("Dealership Management System");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
