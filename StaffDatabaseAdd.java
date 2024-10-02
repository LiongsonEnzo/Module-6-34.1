import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class StaffDatabaseApp extends Application {
    private TextField tfID = new TextField();
    private TextField tfLastName = new TextField();
    private TextField tfFirstName = new TextField();
    private TextField tfMI = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfCity = new TextField();
    private TextField tfState = new TextField();
    private TextField tfTelephone = new TextField();
    private TextField tfEmail = new TextField();

    private Connection connection;
    private PreparedStatement psView, psInsert, psUpdate;

    @Override
    public void start(Stage primaryStage) {
        // Setup database connection
        setupDatabaseConnection();

        // Create the UI layout
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        pane.setHgap(5);
        pane.setVgap(5);

        pane.add(new Label("ID:"), 0, 0);
        pane.add(tfID, 1, 0);
        pane.add(new Label("Last Name:"), 0, 1);
        pane.add(tfLastName, 1, 1);
        pane.add(new Label("First Name:"), 0, 2);
        pane.add(tfFirstName, 1, 2);
        pane.add(new Label("MI:"), 0, 3);
        pane.add(tfMI, 1, 3);
        pane.add(new Label("Address:"), 0, 4);
        pane.add(tfAddress, 1, 4);
        pane.add(new Label("City:"), 0, 5);
        pane.add(tfCity, 1, 5);
        pane.add(new Label("State:"), 0, 6);
        pane.add(tfState, 1, 6);
        pane.add(new Label("Telephone:"), 0, 7);
        pane.add(tfTelephone, 1, 7);
        pane.add(new Label("Email:"), 0, 8);
        pane.add(tfEmail, 1, 8);

        Button btnView = new Button("View");
        Button btnInsert = new Button("Insert");
        Button btnUpdate = new Button("Update");
        pane.add(btnView, 1, 9);
        pane.add(btnInsert, 1, 10);
        pane.add(btnUpdate, 1, 11);

        // Handle button actions
        btnView.setOnAction(e -> viewRecord());
        btnInsert.setOnAction(e -> insertRecord());
        btnUpdate.setOnAction(e -> updateRecord());

        // Setup scene and stage
        Scene scene = new Scene(pane, 400, 400);
        primaryStage.setTitle("Staff Database");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupDatabaseConnection() {
        try {
            // Change URL, user, and password according to your database
            String url = "jdbc:mysql://localhost:3306/yourDatabase";
            String user = "yourUsername";
            String password = "yourPassword";
            connection = DriverManager.getConnection(url, user, password);

            // Prepare SQL statements
            psView = connection.prepareStatement("SELECT * FROM Staff WHERE id = ?");
            psInsert = connection.prepareStatement("INSERT INTO Staff VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            psUpdate = connection.prepareStatement("UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, city=?, state=?, telephone=?, email=? WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void viewRecord() {
        try {
            String id = tfID.getText();
            psView.setString(1, id);
            ResultSet rs = psView.executeQuery();

            if (rs.next()) {
                tfLastName.setText(rs.getString("lastName"));
                tfFirstName.setText(rs.getString("firstName"));
                tfMI.setText(rs.getString("mi"));
                tfAddress.setText(rs.getString("address"));
                tfCity.setText(rs.getString("city"));
                tfState.setText(rs.getString("state"));
                tfTelephone.setText(rs.getString("telephone"));
                tfEmail.setText(rs.getString("email"));
            } else {
                showAlert("No record found with ID " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void insertRecord() {
        try {
            psInsert.setString(1, tfID.getText());
            psInsert.setString(2, tfLastName.getText());
            psInsert.setString(3, tfFirstName.getText());
            psInsert.setString(4, tfMI.getText());
            psInsert.setString(5, tfAddress.getText());
            psInsert.setString(6, tfCity.getText());
            psInsert.setString(7, tfState.getText());
            psInsert.setString(8, tfTelephone.getText());
            psInsert.setString(9, tfEmail.getText());

            int rowsInserted = psInsert.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Record inserted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateRecord() {
        try {
            psUpdate.setString(1, tfLastName.getText());
            psUpdate.setString(2, tfFirstName.getText());
            psUpdate.setString(3, tfMI.getText());
            psUpdate.setString(4, tfAddress.getText());
            psUpdate.setString(5, tfCity.getText());
            psUpdate.setString(6, tfState.getText());
            psUpdate.setString(7, tfTelephone.getText());
            psUpdate.setString(8, tfEmail.getText());
            psUpdate.setString(9, tfID.getText());

            int rowsUpdated = psUpdate.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Record updated successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
