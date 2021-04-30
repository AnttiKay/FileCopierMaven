package app;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import app.fileCopier.FileCopier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXMLController implements Initializable {

    @FXML
    private TextField fileToCopyTextField;

    @FXML
    private Button browseFilesButton;

    @FXML
    private Button openFolderToCopiedFileButton;

    @FXML
    private TextField copiedFilePathTextField;

    @FXML
    private Button copyFile;

    @FXML
    private Label ShowCopySuccessLabel;;

    private FileCopier fileCopier;
    private FileChooser fileChooser;

    @FXML
    void browseFIles(ActionEvent event) {

        fileChooser.setTitle("Select File");
        File fileToCopy = fileChooser.showOpenDialog(fileToCopyTextField.getScene().getWindow());

        if (fileToCopy != null) {
            fileToCopyTextField.setText(fileToCopy.getAbsolutePath());
        }
    }

    @FXML
    void copyFile(ActionEvent event) {
        fileCopier.setInputFilePath(fileToCopyTextField.getText());
        if (!copiedFilePathTextField.getText().equals("")) {
            fileCopier.setOutputFilePath(copiedFilePathTextField.getText());
        } else {
            copiedFilePathTextField.setText(FileCopier.createOutputFileName(fileToCopyTextField.getText()));
            fileCopier.setOutputFilePath(copiedFilePathTextField.getText());
        }
        fileCopier.copyFile();
        ShowCopySuccessLabel.setText("File copied successfully.");
    }

    @FXML
    void openFileFolder(ActionEvent event) {
        fileChooser.setTitle("Save file as...");

        File copiedFile = fileChooser.showSaveDialog(fileToCopyTextField.getScene().getWindow());
        if (copiedFile != null) {
            copiedFilePathTextField.setText(copiedFile.getAbsolutePath());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileCopier = new FileCopier();
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
        .addAll(new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));

    }
}
