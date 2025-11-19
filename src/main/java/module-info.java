module ro.umfst.oop.cardealership {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens ro.umfst.oop.cardealership to javafx.fxml;
    exports ro.umfst.oop.cardealership;
}