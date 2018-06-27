package userInterface;



import java.util.Scanner;

import bussinesLogic.LoggerManager;
import bussinesLogic.War;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	private static final String FXML_DOC = "WarFXML.fxml";

	public static void main(String[] args) {
		//gui or console
		//json or new game
		
		War war = War.getInstance();
		LoggerManager loggerManager = new LoggerManager();
		
		Scanner s = new Scanner(System.in);
		String answer;
		System.out.println("Do you wish to play with gui? y/n");
		answer = s.nextLine();
		if (answer.equalsIgnoreCase("y")){
			GuiGame gui = new GuiGame();
			launch(args);
		}
		else{
			ConsoleGame cg = new ConsoleGame();
			cg.startGame();
			
		}
		
		

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(FXML_DOC));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("World War III");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
