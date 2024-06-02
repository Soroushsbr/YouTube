module espresso.youtube {
    requires javafx.controls;
    requires javafx.fxml;


    opens espresso.youtube to javafx.fxml;
    exports espresso.youtube;
}