module espresso.youtube {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens espresso.youtube to javafx.fxml;
    exports espresso.youtube;
    exports espresso.youtube.Server;
}