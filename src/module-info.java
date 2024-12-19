module OOP {
    requires javafx.controls;
    requires javafx.fxml;
 
    opens game to javafx.fxml;
    exports game;
 
    opens atde.userinterface to javafx.fxml;
    exports atde.userinterface;
 
    opens bigtwo.userinterface to javafx.fxml;
    exports bigtwo.userinterface;
}