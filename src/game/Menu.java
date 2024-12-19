package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();

		root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		root.getStyleClass().add("root");
		
        // Tạo scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Danh Bai Vippro");
        primaryStage.show();
	}
	
	public static void main(String args[]) {
        launch(args);
	}
}
