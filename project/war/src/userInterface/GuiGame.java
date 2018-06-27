package userInterface;

import bussinesLogic.JasonManager;
import bussinesLogic.War;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiGame extends Application{
	private static boolean atWar = true;
	private War war;
	
	public GuiGame() {
		this.war = War.getInstance();
	}
	public void startGame(){
		boolean gsonGame;
		gsonGame = war.ifGsonGame(); // ask if load from gson
		if(gsonGame){
			JasonManager jsonManager = new JasonManager(war);
			war = jsonManager.readFromGson();
			jsonManager.startAll();
			System.out.println(	war.toString());
			//			jsonManager.setAllMissilesFromGson(); //instead of - war.setAllMissilesFromGson();
			//			war.initMissileDestructors();
			//			war.initMissileLauncherDestructors();
			//			War.setCurrentTime(System.currentTimeMillis());

			//			jsonManager.startMissiles();
			//			jsonManager.startLaunchers();
			//			jsonManager.startMissileDestructors();
			//			jsonManager.startMissileLauncherDestructors();
			//			jsonManager.setActiveLaunchers();

			

		}
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("WarFXML.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("War World III");
			primaryStage.setResizable(false);
			primaryStage.show();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
