module com.finance.mcms {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    requires poi;
    requires poi.ooxml;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.finance.mcms to javafx.fxml;
    exports com.finance.mcms;
    exports com.finance.mcms.Controller;
    opens com.finance.mcms.Controller to javafx.fxml;
    opens com.finance.mcms.Model to javafx.base;
    opens com.finance.mcms.Controller.Customer;
    opens com.finance.mcms.Controller.Loan;
}