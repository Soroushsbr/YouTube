module espresso.youtube {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;


    opens espresso.youtube to javafx.fxml;
    exports espresso.youtube;
}