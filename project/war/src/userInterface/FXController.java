package userInterface;

import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import bussinesLogic.War;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FXController {
	War war;
	private final int MAX_LAUNCHERS = 10;
	private final int MAX_LAUNCHER_DESTRUCTOR = 5;
	private final int MAX_MISSILE_DESTRUCTOR = 8;
	private final int LAUNCHER_SIZE = 40;

	@FXML AnchorPane anchorPane;
	@FXML FlowPane flowLaunchers, flowLauncherDestructors, flowMissileDestructors, flowDestinations;
	@FXML TextField destinationTextField;
	@FXML MenuItem shipDestructor, planeDestructor;
	@FXML Button endWar;
	
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	void initialize() {

	}
	public FXController() {
		this.war = War.getInstance();
	}

	public void addMissileLauncher(){
		if(flowLaunchers.getChildren().size() < MAX_LAUNCHERS){
			ImageView launcher = new ImageView(new Image("File:src/userInterface/tank.png"));
			launcher.setFitWidth(LAUNCHER_SIZE);
			launcher.setFitHeight(LAUNCHER_SIZE);
			flowLaunchers.getChildren().add(launcher);
			war.addMissileLauncher();
		}
	}

	public void addMissileLauncherDestructor(ActionEvent e){
		if(flowLauncherDestructors.getChildren().size() < MAX_LAUNCHER_DESTRUCTOR){
			if(e.getSource() == planeDestructor){
				war.addMissileLauncherDestructor("Plane");
				ImageView destructor = new ImageView(new Image("File:src/userInterface/plane.png"));
				destructor.setFitWidth(LAUNCHER_SIZE);
				destructor.setFitHeight(LAUNCHER_SIZE);
				flowLauncherDestructors.getChildren().add(destructor);
			}
			if(e.getSource() == shipDestructor){
				war.addMissileLauncherDestructor("Ship");
				ImageView destructor = new ImageView(new Image("File:src/userInterface/ship.png"));
				destructor.setFitWidth(LAUNCHER_SIZE);
				destructor.setFitHeight(LAUNCHER_SIZE);
				flowLauncherDestructors.getChildren().add(destructor);
			}
		}
	}
	
	public void addMissileDestructors(){
		if(flowMissileDestructors.getChildren().size() < MAX_MISSILE_DESTRUCTOR){
			ImageView destructor = new ImageView(new Image("File:src/userInterface/destructor.png"));
			destructor.setFitWidth(LAUNCHER_SIZE);
			destructor.setFitHeight(LAUNCHER_SIZE);
			flowMissileDestructors.getChildren().add(destructor);
			war.addMissileDestructor();
		}
		
	}
	
	public void launchMissile(){
		war.launchMissile(createDialogPane());
		
	}
	
	public String createDialogPane(){
		TextInputDialog dialog = new TextInputDialog("destination");
		dialog.setTitle("Launch Missile");
		dialog.setHeaderText("Please enter a destination:");
		dialog.show();
		Optional<String> result = dialog.showAndWait();
		return result.get();
		

//		if (result.isPresent()) {
//
//		    entered = result.get();
//		}
	}
	






}