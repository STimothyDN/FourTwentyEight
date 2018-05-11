import java.io.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MatchController implements Initializable
{
	@FXML
    Button aboutButton;
    @FXML
	Button downloadButton;
    @FXML
    Button downloadProgramButton;
    @FXML
    Button openButton;
	@FXML
	Button runButton;
    @FXML
    Button singleGo;
    @FXML
    ComboBox singleChooser;
	@FXML
	ToggleButton viewButton;
    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;


	@Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ArrayList<String> fromFile = new ArrayList<String>();
        fromFile.add("https://docs.google.com/document/d/1PwOzBPkFMk3p-YBe4re_hgx0AAmvhXaM-NFWWZlYolQ/export?format=txt");
        fromFile.add("https://drive.google.com/uc?export=download&id=1mpenYPJxAUQet4pyz-xEc28I-XQzfQk3");
        ArrayList<String> toFile = new ArrayList<String>();
        toFile.add("Matchmaking/about.txt");
        toFile.add("Matchmaking/alert.wav");

        for(int x = 0; x < fromFile.size(); x++)
        {
            MatchmakerApp.downloadFile(fromFile.get(x), toFile.get(x));
        }

        if(MatchmakerApp.fileToReadCheck.exists() && !MatchmakerApp.fileToReadCheck.isDirectory())
        {
            MatchmakerApp.dataDownloaded = true;
            ObservableList<String> ol = FXCollections.observableArrayList();
            MatchmakerApp.CSVToJava();
            for(int i = 0; i < MatchmakerApp.peopleList.size(); i++)
            {
                ol.add(MatchmakerApp.peopleList.get(i).toString());
            }
            FXCollections.sort(ol);
            singleChooser.setItems(ol);
        }
        
        else
        {
            downloadButton.setText("Download Data Set");
        }

        if(!MatchmakerApp.checkProgram())
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("New Version of Program Available");
            alert.setHeaderText(null);
            alert.setContentText("A new version of the program is available! Would you like to download it?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                MatchmakerApp.downloadProgram();
            }
        }

        downloadButton.setOnAction(this::handleDownload);
        downloadProgramButton.setOnAction(this::handleProgramDownload);
        aboutButton.setOnAction(this::handleAbout);
        runButton.setOnAction(this::handleRun);
        openButton.setOnAction(this::handleOpen);
        viewButton.setOnAction(this::handleView);
        singleGo.setOnAction(this::handleSingle);
    }

    public MatchController()
    {
    }

    public void handleView(ActionEvent event)
    {
        if(MatchmakerApp.isWordView == true)
        {
            MatchmakerApp.isWordView = false;
            MatchmakerApp.fileToReadNow = "Matchmaking/matchmaking.csv";
            viewButton.setText("Spreadsheet View");
        }
        else
        {
            MatchmakerApp.isWordView = true;
            MatchmakerApp.fileToReadNow = "Matchmaking/matchmaking.txt";
            viewButton.setText("Word View");
        }
    }

    public void handleProgramDownload(ActionEvent event)
    {
        MatchmakerApp.downloadProgram();
    }

    public void handleDownload(ActionEvent event)
    {
    	String fromFile = "https://spreadsheets.google.com/feeds/download/spreadsheets/Export?key=1Xg8K3HSECHoQnyHKfljdTdxVTJNvGnyf0VzziMUWb9E&exportFormat=csv";
        String toFile = "Matchmaking/matchmaking - Form Responses 1.csv";
        String newFile = "Matchmaking/test.csv";
        File deleteFile = new File(newFile);
        ObservableList<String> ol = FXCollections.observableArrayList();

        deleteFile.delete();

        MatchmakerApp.downloadFile(fromFile, newFile);

        try
        {
            byte[] f1 = Files.readAllBytes(Paths.get(toFile));
            byte[] f2 = Files.readAllBytes(Paths.get(newFile));

            if(!Arrays.equals(f1, f2))
            {
                    deleteFile.delete();
                    MatchmakerApp.downloadFile(fromFile, toFile);

                    MatchmakerApp.peopleList = new ArrayList<People>();
                    MatchmakerApp.CSVToJava();

                    for(int i = 0; i < MatchmakerApp.peopleList.size(); i++)
                    {
                        ol.add(MatchmakerApp.peopleList.get(i).toString());
                    }

                    FXCollections.sort(ol);
                    downloadButton.setText("Download Newest Version of Data");

                    if(MatchmakerApp.dataDownloaded = false)
                    {
                        MatchmakerApp.dataDownloaded = true;
                    }

                    singleChooser.setItems(ol);
                    MatchmakerApp.showInfoAlert("Confirmation", null, "The latest data has been loaded into the matchmaking program!");
            }
            else
            {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("You have the most up to date version! No file downloaded.");
                alert.showAndWait();
                deleteFile.delete();
            }
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void handleSingle(ActionEvent event)
    {
        MatchmakerApp.matchChain = 25000;
        MatchmakerApp.isSingleRun = true;
        MatchmakerApp.peopleList = new ArrayList<People>();
        MatchmakerApp.CSVToJava();
        String person = singleChooser.getValue().toString();
        People firstPerson = null;

        for(int i = 0; i < MatchmakerApp.peopleList.size(); i++)
        {
            if(MatchmakerApp.peopleList.get(i).toString().equals(person))
            {
                firstPerson = MatchmakerApp.peopleList.get(i);
            }
        }

        int firstPersonIndex = MatchmakerApp.peopleList.indexOf(firstPerson);
        People secondPerson = MatchmakerApp.peopleList.get(0);
        int secondPersonIndex = 0;

        MatchmakerApp.peopleList.set(secondPersonIndex, firstPerson);
        MatchmakerApp.peopleList.set(firstPersonIndex, secondPerson);

        String compatible = MatchmakerApp.pairingPeople();

        MatchmakerApp.playAlert();
        MatchmakerApp.showInfoAlert("Congratulations!", "New Pairing Generated", "After 25,000 simulations, the best match generated is:" + "\n" + compatible);
        MatchmakerApp.peopleList = new ArrayList<People>();
        MatchmakerApp.isSingleRun = false;
    }

    public void handleRun(ActionEvent event)
    {
        MatchmakerApp.matchChain = 5000;
        MatchmakerApp.peopleList = new ArrayList<People>();
        int originalSize = MatchmakerApp.peopleList.size();

        if(MatchmakerApp.fileToWriteCheck.exists() && !MatchmakerApp.fileToWriteCheck.isDirectory())
        {
            MatchmakerApp.fileToWriteCheck.delete();
        }

        if(MatchmakerApp.fileToWriteCheckCSV.exists() && !MatchmakerApp.fileToWriteCheckCSV.isDirectory())
        {
            MatchmakerApp.fileToWriteCheckCSV.delete();
        }

        MatchmakerApp.CSVToJava();
        MatchmakerApp.JavaToFile("A low compatibility indicates one of two things: you were compared among a small pool of people," + "\n" + "which occurs when the algorithm reaches close to the end of running, or you are simply not compatible with anyone." + "\n" + "Feel free to keep re-running the algorithm to determine whether you fall in category 1 or category 2." + "\n");
        
        while(MatchmakerApp.peopleList.size() > 3)
        {
            Collections.shuffle(MatchmakerApp.peopleList);
            MatchmakerApp.JavaToFile(MatchmakerApp.pairingPeople());
        }

        if(MatchmakerApp.peopleList.size() == 0)
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No platonic pairs have been generated.");
            alert.showAndWait();
        }
        
        else
        {
            MatchmakerApp.playAlert();
            MatchmakerApp.showInfoAlert("Confirmation", null, "The latest platonic pairs have been generated!");
        }
    }

    public void handleAbout(ActionEvent event)
    {
        String fromFile = "https://docs.google.com/document/d/1PwOzBPkFMk3p-YBe4re_hgx0AAmvhXaM-NFWWZlYolQ/export?format=txt";
        String toFile = "Matchmaking/about.txt";
        
        MatchmakerApp.downloadFile(fromFile, toFile);
        MatchmakerApp.fileViewer("Matchmaking/about.txt", "About FourTwentyEight", true);
    }

    public void handleOpen(ActionEvent event)
    {
        MatchmakerApp.fileViewer(MatchmakerApp.fileToReadNow, "FourTwentyEight Viewer", MatchmakerApp.isWordView);
    }
}