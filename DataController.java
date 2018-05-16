import java.io.*;
import java.util.*;
import javafx.event.ActionEvent;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class DataController implements Initializable
{
	@FXML
    Button oneQButton;
    @FXML
    Button twoQButton;
    @FXML
    Button threeQButton;
    @FXML
    Button fourQButton;
    @FXML
    Button fiveQButton;
    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    public static List<ChartData> questionList = new ArrayList<ChartData>();
    public static ChartData oneQuestion = new ChartData("Introverted (1) to Extroverted (5)", 0, 0, 0, 0 ,0, "First Question");
    public static ChartData twoQuestion = new ChartData("Very Picky (1) to Will Eat Anything (5)", 0, 0, 0, 0 ,0, "Second Question");
    public static ChartData threeQuestion = new ChartData("What are Rules? (1) to I LOVE NCSSM RULES (5)", 0, 0, 0, 0 ,0, "Third Question");
    public static ChartData fourQuestion = new ChartData("Left-Wing (1) to Right-Wing (5)", 0, 0, 0, 0 ,0, "Fourth Question");
    public static ChartData fiveQuestion = new ChartData("Get Away From Me (1) to I LOVE HUGGING (5)", 0, 0, 0, 0 ,0, "Fifth Question");

    public void initialize(URL location, ResourceBundle resources)
    {
    	String line = "";
		String splitBy = ",";
		BufferedReader br = null;

		questionList.add(oneQuestion);
		questionList.add(twoQuestion);
		questionList.add(threeQuestion);
		questionList.add(fourQuestion);
		questionList.add(fiveQuestion);

    	try
    	{
    		br = new BufferedReader(new FileReader(MatchmakerApp.fileToRead));
			String headerLine = br.readLine();

			while((line = br.readLine()) != null )
			{
				String[] tempPerson = line.split(splitBy);

				oneQuestion.addPoint(Integer.parseInt(tempPerson[3]));
				twoQuestion.addPoint(Integer.parseInt(tempPerson[5]));
				threeQuestion.addPoint(Integer.parseInt(tempPerson[7]));
				fourQuestion.addPoint(Integer.parseInt(tempPerson[9]));
				fiveQuestion.addPoint(Integer.parseInt(tempPerson[11]));
			}
    	}

    	catch(FileNotFoundException e)
		{
			MatchmakerApp.noFileFound();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		oneQButton.setOnAction(this::handleOne);
		twoQButton.setOnAction(this::handleTwo);
		threeQButton.setOnAction(this::handleThree);
		fourQButton.setOnAction(this::handleFour);
		fiveQButton.setOnAction(this::handleFive);
    }

    public DataController()
    {
    }

    public void handleOne(ActionEvent event)
    {
    	MatchmakerApp.barGraphViewer(oneQuestion.getQName(),
    		oneQuestion.getNumOne(), oneQuestion.getNumTwo(), oneQuestion.getNumThree(), oneQuestion.getNumFour(),
    		oneQuestion.getNumFive());
    }

    public void handleTwo(ActionEvent event)
    {
    	MatchmakerApp.barGraphViewer(twoQuestion.getQName(),
    		twoQuestion.getNumOne(), twoQuestion.getNumTwo(), twoQuestion.getNumThree(), twoQuestion.getNumFour(),
    		twoQuestion.getNumFive());
    }

    public void handleThree(ActionEvent event)
    {
    	MatchmakerApp.barGraphViewer(threeQuestion.getQName(),
    		threeQuestion.getNumOne(), threeQuestion.getNumTwo(), threeQuestion.getNumThree(), threeQuestion.getNumFour(),
    		threeQuestion.getNumFive());
    }

    public void handleFour(ActionEvent event)
    {
    	MatchmakerApp.barGraphViewer(fourQuestion.getQName(),
    		fourQuestion.getNumOne(), fourQuestion.getNumTwo(), fourQuestion.getNumThree(), fourQuestion.getNumFour(),
    		fourQuestion.getNumFive());
    }

    public void handleFive(ActionEvent event)
    {
    	MatchmakerApp.barGraphViewer(fiveQuestion.getQName(),
    		fiveQuestion.getNumOne(), fiveQuestion.getNumTwo(), fiveQuestion.getNumThree(), fiveQuestion.getNumFour(),
    		fiveQuestion.getNumFive());
    }
}