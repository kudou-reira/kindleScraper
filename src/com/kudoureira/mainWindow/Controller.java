package com.kudoureira.mainWindow;

import com.kudoureira.crawlKindle.AmazonKindleScrape;
import com.kudoureira.dictionary.Novels;
import com.kudoureira.htmlKindle.HTMLScrape;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    File[] files;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox ourCheckBox;
    @FXML
    private Label ourLabel;
    @FXML
    private Label filesLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button changeHTMLScan;
    @FXML
    private Button changeKindleScraper;
    @FXML
    private Button parseFiles;


    @FXML
    public void initialize() {
        loginButton.setDisable(true);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onLoginButtonClicked(ActionEvent e) {
        if(e.getSource().equals(loginButton)) {
            System.out.println("Hello, " + emailField.getText());
            AmazonKindleScrape kindle = new AmazonKindleScrape(emailField.getText(), passwordField.getText());
            Boolean login = kindle.processBooks();
            if(!login) {
                ourLabel.setText("Login failed, please try again");
            } else {
                ourLabel.setText("Your books have been processed");
                kindle.dump();
            }
        }

        if(ourCheckBox.isSelected()) {
            passwordField.clear();
            loginButton.setDisable(true);
        }
    }

    public void onHTMLScanClicked(ActionEvent e) throws IOException {
        Stage stage;
        Parent root;

        if(e.getSource() == changeHTMLScan) {
            System.out.println("html scan clicked");
            stage = (Stage) changeHTMLScan.getScene().getWindow();
            stage.setTitle("Kindle Notebook HTML Scanner");
            root = FXMLLoader.load(getClass().getResource("htmlWindow.fxml"));
            Scene scene = new Scene(root, 900, 350);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onKindleScraperButtonClicked(ActionEvent e) throws IOException {
        System.out.println("kindle scraper clicked");
        Stage stage = new Stage();
        stage.setTitle("Amazon Kindle Scraper");
        Pane scraperPane = null;
        scraperPane = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        Scene scraperScene = new Scene(scraperPane, 900, 350);
        stage.setScene(scraperScene);

        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();

        stage.show();
    }

    public void onParseButtonClicked(ActionEvent e) {
        if(e.getSource().equals(parseFiles)) {
            System.out.println("on parse button clicked");
            HTMLScrape notebook = new HTMLScrape(files);
            notebook.scan();
            System.out.println("this is books " + notebook.getBooks().toString());
            Novels novels = new Novels(notebook.getBooks());
            novels.compile();
        }
    }


    @FXML
    public void onDirectoryButtonClicked(ActionEvent e) throws IOException {
        System.out.println("directory button clicked");
        String path = System.getProperty("user.dir");
        String testPath = "D:\\Dropbox\\kindleScans";

        JFrame frame = new JFrame("Simple GUI");
        FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        fd.setDirectory(testPath);
        fd.setFile("*.html");
        fd.setVisible(true);
//        String filename = fd.getFile();
        files = fd.getFiles();

        if(files.length != 0) {
            StringBuilder sb = new StringBuilder();
            for(File file : files) {
                System.out.println("this is files " + file);
                sb.append(file);
                sb.append("\n");
            }
            filesLabel.setText("These are your selected files" + "\n" + sb);
        }

//        if (filename == null)
//            System.out.println("You cancelled the choice");
//        else
//            System.out.println("You chose " + filename);
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
