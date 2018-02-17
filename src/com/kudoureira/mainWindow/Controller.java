package com.kudoureira.mainWindow;

import com.kudoureira.crawlKindle.AmazonKindle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private CheckBox ourCheckBox;
    @FXML
    private Label ourLabel;

    @FXML
    public void initialize() {
        loginButton.setDisable(true);
    }

    @FXML
    public void onButtonClicked(ActionEvent e) {
        if(e.getSource().equals(loginButton)) {
            System.out.println("Hello, " + emailField.getText());
            AmazonKindle kindle = new AmazonKindle(emailField.getText(), passwordField.getText());
            Boolean login = kindle.processBooks();
            if(!login) {
                ourLabel.setText("Login failed, please try again");
            }
        }

//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
//                    System.out.println("I'm going to sleep on the: " + s);
//                    Thread.sleep(10000);
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
//                            System.out.println("I'm updating the label on the: " + s);
//                            ourLabel.setText("Logging into Amazon Kindle");
//                        }
//                    });
//                } catch(InterruptedException event) {
//                    // we don't care about this
//                }
//            }
//        };
//
//        new Thread(task).start();

        if(ourCheckBox.isSelected()) {
            passwordField.clear();
            loginButton.setDisable(true);
        }
    }

    @FXML
    public void handleKeyReleased() {
        String text1 = emailField.getText();
        String text2 = passwordField.getText();
        boolean disableButtons = text1.isEmpty() || text1.trim().isEmpty() || text2.isEmpty() || text2.trim().isEmpty();
        loginButton.setDisable(disableButtons);
    }

    public void handleChange() {
        System.out.println("The checkbox is " + (ourCheckBox.isSelected() ? "checked" : "not checked"));
    }
}
