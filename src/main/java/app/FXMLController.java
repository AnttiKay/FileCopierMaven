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
import java.awt.Desktop;

public class FXMLController implements Initializable {

    @FXML
    private TextField FileToCopyTextField;

    @FXML
    private Button BrowseFilesButton;

    @FXML
    private Button OpenFolderToCopiedFileButton;

    @FXML
    private TextField CopiedFilePathTextField;

    @FXML
    private Button CopyFile;

    @FXML
    private Label ShowCopySuccessLabel;

    private FileCopier fileCopier;

    @FXML
    void browseFIles(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File fileToCopy = fileChooser.showOpenDialog(FileToCopyTextField.getScene().getWindow());

        FileToCopyTextField.setText(fileToCopy.getAbsolutePath());
    }

    @FXML
    void copyFile(ActionEvent event) {
        fileCopier.setInputFilePath(FileToCopyTextField.getText());
        if (!CopiedFilePathTextField.getText().equals("")) {
            fileCopier.setOutputFilePath(CopiedFilePathTextField.getText());
        } else {
            CopiedFilePathTextField.setText(FileCopier.createOutputFileName(FileToCopyTextField.getText()));
            fileCopier.setOutputFilePath(CopiedFilePathTextField.getText());
        }
        fileCopier.copyFile();
        ShowCopySuccessLabel.setText("File copied successfully.");
    }

    @FXML
    void openFileFolder(ActionEvent event) {
        if (!CopiedFilePathTextField.getText().equals("")) {
            File copiedFile = new File(CopiedFilePathTextField.getText());
            if (copiedFile.isFile()) {
                Desktop.getDesktop().browseFileDirectory(copiedFile);
            } else {
                ShowCopySuccessLabel.setText("Error: Path to copied file is not a file.");
            }
        } else {
            ShowCopySuccessLabel.setText("Error: Path to copied file is empty");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileCopier = new FileCopier();

    }
}
