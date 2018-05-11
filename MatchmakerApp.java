import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MatchmakerApp extends Application
{
	public static File fileToWriteCheck = new File("Matchmaking/matchmaking.txt");
	public static File fileToWriteCheckCSV = new File("Matchmaking/matchmaking.csv");
	public static List<People> removePeople = new ArrayList<People>();
	public static List<People> peopleList = new ArrayList<People>();
	public static List<Double> peopleProbList = new ArrayList<Double>();
	public static List<String> peopleListCSV = new ArrayList<String>();
	public static int partner = 0;
	public static int maxValueFinal = 0;
	public static double compatibility = 0;
	public static int countHowMany = 0;
	public static String fileToRead = "Matchmaking/matchmaking - Form Responses 1.csv";
	public static File fileToReadCheck = new File(fileToRead);
	public static String fileToReadNow = "Matchmaking/matchmaking.csv";
	public static String fxmlFile = "MatchmakerApp.fxml";
	public static boolean isWordView = false;
    public static boolean dataDownloaded = false;
    public static boolean isSingleRun = false;
    public static int matchChain = 5000;
    public static String newestProgramString = "https://github.com/STimothyDN/FourTwentyEight/raw/master/MatchmakerApp.jar";
    public static String testProgramString = "Matchmaking/MatchmakerApp.jar";
    public static String ogProgramString = "MatchmakerApp.jar";
    public static File deleteFile = new File(testProgramString);

	public MatchmakerApp()
	{
	}

	public static void main(String[] args) 
	{
		File dir = new File("Matchmaking");

		dir.mkdir();
		launch(args);
	}

	public void start(Stage primary) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

        primary.setTitle("FourTwentyEight");
        primary.setScene(new Scene(root));
        primary.show();
	}

	public static String pairingPeople()
	{
		String recommendation = "Unknown";

		removePeople();
		listToProb(peopleList);
		matchAlgo(peopleList, peopleProbList);
		partner = maxValueFinal;
		removePeople.add(peopleList.get(0));
		removePeople.add(peopleList.get(partner));
		if(compatibility >= 97.00)
		{
			recommendation = "Perfect Match";
		}
		else if(compatibility < 97.00 && compatibility >= 80.00)
		{
			recommendation = "Very Likely Match";
		}
		else if(compatibility < 80.00 && compatibility >= 50.00)
		{
			recommendation = "Likely Match";
		}
		else if(compatibility < 50.00 && compatibility >= 30.00)
		{
			recommendation = "Re-match Recommended";
		}
		else if(compatibility < 30.00 && compatibility >= 0.00)
		{
			recommendation = "Re-match Needed";
		}
		else
		{
			recommendation = "Unknown";
		}

		if(!isSingleRun)
		{
			peopleListCSV.add(peopleList.get(0).toString());
			peopleListCSV.add(peopleList.get(partner).toString());
			peopleListCSV.add(Double.toString(compatibility));
			peopleListCSV.add(recommendation);
			JavaToCSV(peopleListCSV);
		}
		if(!isSingleRun)
		{
			return peopleList.get(0) + " & " + peopleList.get(partner) + " Compatibility: " + Double.toString(compatibility) + "%, Recommendation: " + recommendation;
		}
		else
		{
			return peopleList.get(0) + " & " + peopleList.get(partner) + "\n" + "Compatibility: " + Double.toString(compatibility) + "%" + "\n" + "Recommendation: " + recommendation;
		}
	}

	public static void CSVToJava()
	{
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";

		try
		{
			br = new BufferedReader(new FileReader(fileToRead));
			String headerLine = br.readLine();

			while((line = br.readLine()) != null )
			{
				String[] tempPerson = line.split(splitBy);
				People newPerson = new People(tempPerson[1], tempPerson[2], Double.parseDouble(tempPerson[3]), 
					Double.parseDouble(tempPerson[4]), Double.parseDouble(tempPerson[5]), 
					Double.parseDouble(tempPerson[6]), Double.parseDouble(tempPerson[7]), 
					Double.parseDouble(tempPerson[8]), Double.parseDouble(tempPerson[9]), 
					Double.parseDouble(tempPerson[10]), Double.parseDouble(tempPerson[11]), 
					Double.parseDouble(tempPerson[12]));
				
				peopleList.add(newPerson);
			}
		}

		catch(FileNotFoundException e)
		{
			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("Exception Dialog");
			alert.setHeaderText("Oh no!");
			alert.setContentText("Could not find data set! If this is your first time running the program, have you clicked the Download Data Set button?");

			Exception ex = new FileNotFoundException("Could not find file matchmaker.txt");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);

			ex.printStackTrace(pw);

			String exceptionText = sw.toString();
			Label label = new Label("The exception stacktrace was:");
			TextArea textArea = new TextArea(exceptionText);

			textArea.setEditable(false);
			textArea.setWrapText(true);
			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();

			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);
			alert.getDialogPane().setExpandableContent(expContent);
			alert.showAndWait();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		finally
		{
			if(br != null)
			{
				try
				{
					br.close();
				}

				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		Collections.shuffle(peopleList);
	}

	public static void listToProb(List<People> peopleList)
	{
		for(int k = 0; k < peopleList.size(); k++)
		{
			if(k != 0)
			{
				PeopleProb newPeopleProb = new PeopleProb(peopleList.get(0), peopleList.get(k));
				PeopleProbPrior newPeopleProbPrior = new PeopleProbPrior(peopleList.get(0), peopleList.get(k));
				Double finalTotallyFinalProb = 0.00;
				Double oldProb = newPeopleProbPrior.totalProb();
				List<Double> counter = new ArrayList<Double>();
				Double gamma = .01;

				for(int x = 0; x <= 1000; x++)
				{
					Random r = new Random();
					Double newProb = newPeopleProb.totalProb() + r.nextGaussian()*gamma;
					Double randomTest = Math.random();
					Double testRatio = newProb/oldProb;

					if(testRatio > randomTest && newProb <= 1.00 && newProb >= 0.00)
					{
						oldProb = newProb;
					}

					counter.add(oldProb);
				}

				finalTotallyFinalProb = mode(counter, 0.00);
				peopleProbList.add(finalTotallyFinalProb);
			}
		}
	}

	public static void matchAlgo(List<People> peopleList, List<Double> peopleProbList)
	{
		int startIndex = 0;
		List<Integer> counter = new ArrayList<Integer>();

		if(maxValueFinal != 0)
		{
			maxValueFinal = 0;
		}

		for(int x = 0; x <= matchChain; x++)
		{
			int newIndex = (int)(Math.random() * ((peopleProbList.size() - 1) + 1));
			Double randomTest = Math.random();

			if(startIndex != newIndex)
			{
				Double testRatio = peopleProbList.get(newIndex)/peopleProbList.get(startIndex);
				
				if(testRatio > randomTest)
				{
					startIndex = newIndex;
				}
				
				counter.add(startIndex);
			}
		}

    	maxValueFinal = mode(counter, 0) + 1;
    	compatibility = (Math.round((peopleProbList.get(mode(counter, 0)) * 100) * 100))/100;
	}

	public static void removePeople()
	{
		if(removePeople.size() != 0)
		{
			for(int i = 0; i < removePeople.size(); i++)
			{
				peopleList.remove(removePeople.get(i));
			}
		}

		partner = 0;
		removePeople = new ArrayList<People>();
		peopleProbList = new ArrayList<Double>();
		peopleListCSV = new ArrayList<String>();
	}

	public static void JavaToCSV(List<String> peopleListCSV)
	{
		String commaDelimit = ",";
    	String newLine = "\n";
		String fileToWriteCSV = "Matchmaking/matchmaking.csv";
		BufferedWriter bw = null;

		try
		{
			bw = new BufferedWriter(new FileWriter(fileToWriteCSV, true));
			
			for(int x = 0; x < peopleListCSV.size(); x++)
			{
				bw.write(peopleListCSV.get(x));
				bw.write(commaDelimit);
			}

			bw.write(newLine);
		}

		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		finally
		{
			if(bw != null)
			{
				try
				{
					bw.close();
				}

				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static Double mode(List<Double> counter, Double x)
	{
		int maxCount = 0;
		Double maxValue = 0.00;

		for (int i = 0; i < counter.size(); ++i)
		{
        	int count = 0;
        	
        	for (int j = 0; j < counter.size(); ++j)
        	{
            	if (counter.get(j) == counter.get(i)) ++count;
        	}
        	
        	if (count > maxCount) 
        	{
            	maxCount = count;
            	maxValue = counter.get(i);
        	}
    	}

    	return maxValue;
	}

	public static Integer mode(List<Integer> counter, Integer y)
	{
		int maxCount = 0;
		int maxValue = 0;

		for (int i = 0; i < counter.size(); ++i)
		{
        	int count = 0;
        	
        	for (int j = 0; j < counter.size(); ++j)
        	{
            	if (counter.get(j) == counter.get(i)) ++count;
        	}
        	
        	if (count > maxCount) 
        	{
            	maxCount = count;
            	maxValue = counter.get(i);
        	}
    	}

    	return maxValue;
	}

	public static String JavaToFile(String content)
	{
		String fileToWrite = "Matchmaking/matchmaking.txt";
		BufferedWriter bw = null;

		try
		{
			bw = new BufferedWriter(new FileWriter(fileToWrite, true));
			bw.write(content);
			bw.newLine();
		}

		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		finally
		{
			if(bw != null)
			{
				try
				{
					bw.close();
				}

				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return(content);
	}

	public static void showAlert(AlertType alertType, String title, String headerText, String contentText)
	{
		Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
	}

	public static void playAlert()
    {
        String musicFile = "Matchmaking/alert.wav";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mp = new MediaPlayer(sound);
        mp.play();
    }

    public static void downloadFile(String fromFile, String toFile)
    {
        try
        {
            URL website = new URL(fromFile);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(toFile);

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean checkProgram()
    {
        boolean checkProgram = false;
        deleteFile.delete();
        MatchmakerApp.downloadFile(newestProgramString, testProgramString);
        
        try
        {
            byte[] f1 = Files.readAllBytes(Paths.get(testProgramString));
            byte[] f2 = Files.readAllBytes(Paths.get(ogProgramString));
            checkProgram = Arrays.equals(f1, f2);
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }

        return checkProgram;
    }

    public static void downloadProgram()
    {
    	if(!checkProgram())
        {
            try
            {
                deleteFile.delete();
                downloadFile(newestProgramString, ogProgramString);
                Runtime.getRuntime().exec("java -jar MatchmakerApp.jar");
                System.exit(0);
            }

            catch(IOException e)
            {
                e.printStackTrace();
            }
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

    public static void fileViewer(String fileRead, String textHeader, boolean isWordView)
    {
        Stage secondary = new Stage();
        TextArea displayPairs = new TextArea();
        BorderPane rootTwo = new BorderPane();
        TableView<FinalPeople> displayCSV = new TableView<>();
        String delimit = ",";

        displayPairs.setPrefSize(875, 565);
        displayPairs.setEditable(false);
        displayCSV.setPrefSize(875, 565);

        try
        {
            String line;
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new FileReader(fileRead));
            ObservableList<FinalPeople> ol = FXCollections.observableArrayList();
            String[] al = {};
            
            if(isWordView)
            {
                while((line = br.readLine()) != null)
                {
                    sb.append(line+"\n");
                }

                br.close();
                displayPairs.setText(sb.toString());
                rootTwo.setCenter(displayPairs);
            }
            else
            {
                TableColumn columnF1 = new TableColumn("Person");
                TableColumn columnF2 = new TableColumn("Person");
                TableColumn columnF3 = new TableColumn("Compatibility");
                TableColumn columnF4 = new TableColumn("Algorithm Recommendation");

                columnF1.setCellValueFactory(new PropertyValueFactory<>("f1"));
                columnF2.setCellValueFactory(new PropertyValueFactory<>("f2"));
                columnF3.setCellValueFactory(new PropertyValueFactory<>("f3"));
                columnF4.setCellValueFactory(new PropertyValueFactory<>("f4"));

                while((line = br.readLine()) != null)
                {
                    al = line.split(delimit);

                    FinalPeople finalPeople = new FinalPeople(al[0], al[1], Double.parseDouble(al[2]), al[3]);
                    
                    ol.add(finalPeople);
                }

                br.close();
                displayCSV.setItems(ol);
                displayCSV.getColumns().addAll(columnF1, columnF2, columnF3, columnF4);
                rootTwo.setCenter(displayCSV);
            }
        }

        catch(FileNotFoundException ex)
        {
           ex.printStackTrace();
        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        secondary.setTitle(textHeader);
        secondary.setScene(new Scene(rootTwo));
        secondary.show();
    }
}

class People
{
	private String firstName;
	private String lastName;
	private double q1;
	private double impQ1;
	private double q2;
	private double impQ2;
	private double q3;
	private double impQ3;
	private double q4;
	private double impQ4;
	private double q5;
	private double impQ5;

	public People(String firstName, String lastName, double q1,
		double impQ1, double q2, double impQ2, double q3, double impQ3, double q4, double impQ4, double q5, double impQ5)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.q1 = q1;
		this.impQ1 = impQ1;
		this.q2 = q2;
		this.impQ2 = impQ2;
		this.q3 = q3;
		this.impQ3 = impQ3;
		this.q4 = q4;
		this.impQ4 = impQ4;
		this.q5 = q5;
		this.impQ5 = impQ5;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public double getQ1()
	{
		return q1;
	}

	public double getImpQ1()
	{
		return impQ1;
	}

	public double getQ2()
	{
		return q2;
	}

	public double getImpQ2()
	{
		return impQ2;
	}

	public double getQ3()
	{
		return q3;
	}

	public double getImpQ3()
	{
		return impQ3;
	}

	public double getQ4()
	{
		return q4;
	}

	public double getImpQ4()
	{
		return impQ4;
	}

	public double getQ5()
	{
		return q5;
	}

	public double getImpQ5()
	{
		return impQ5;
	}

	@Override
	public String toString()
	{
		return firstName + " " + lastName;
	}
}

class PeopleProb
{
	private People personOne;
	private People personTwo;

	public PeopleProb(People personOne, People personTwo)
	{
		this.personOne = personOne;
		this.personTwo = personTwo;
	}

	public double calcQ1Prob()
	{
		return Math.pow((1 - (Math.abs((personOne.getQ1() - personTwo.getQ1())/5))), personOne.getImpQ1());
	}

	public double calcQ2Prob()
	{
		return Math.pow((1 - (Math.abs((personOne.getQ2() - personTwo.getQ2())/5))), personOne.getImpQ2());
	}

	public double calcQ3Prob()
	{
		return Math.pow((1 - (Math.abs((personOne.getQ3() - personTwo.getQ3())/5))), personOne.getImpQ3());
	}

	public double calcQ4Prob()
	{
		return Math.pow((1 - (Math.abs((personOne.getQ4() - personTwo.getQ4())/5))), personOne.getImpQ4());
	}

	public double calcQ5Prob()
	{
		return Math.pow((1 - (Math.abs((personOne.getQ5() - personTwo.getQ5())/5))), personOne.getImpQ5());
	}

	public double totalProb()
	{
		return calcQ1Prob() * calcQ2Prob() * calcQ3Prob() * calcQ4Prob() * calcQ5Prob();
	}
}

class PeopleProbPrior
{
	private People personOne;
	private People personTwo;

	public PeopleProbPrior(People personOne, People personTwo)
	{
		this.personOne = personOne;
		this.personTwo = personTwo;
	}

	public double calcQ1Prob()
	{
		return (1 - (Math.abs((personOne.getQ1() - personTwo.getQ1())/5)));
	}

	public double calcQ2Prob()
	{
		return (1 - (Math.abs((personOne.getQ2() - personTwo.getQ2())/5)));
	}

	public double calcQ3Prob()
	{
		return (1 - (Math.abs((personOne.getQ3() - personTwo.getQ3())/5)));
	}

	public double calcQ4Prob()
	{
		return (1 - (Math.abs((personOne.getQ4() - personTwo.getQ4())/5)));
	}

	public double calcQ5Prob()
	{
		return (1 - (Math.abs((personOne.getQ5() - personTwo.getQ5())/5)));
	}

	public double totalProb()
	{
		return calcQ1Prob() * calcQ2Prob() * calcQ3Prob() * calcQ4Prob() * calcQ5Prob();
	}
}