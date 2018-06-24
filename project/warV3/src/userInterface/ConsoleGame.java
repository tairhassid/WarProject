package userInterface;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import bussinesLogic.JasonManager;
import bussinesLogic.War;


public class ConsoleGame {
	private static boolean atWar = true;
	private War war;
	
	public ConsoleGame(){
		war = new War();
	}
	
	public void startGame(){
		boolean gsonGame;
		gsonGame = war.ifGsonGame(); // ask if load from gson
		if(gsonGame){
			JasonManager jsonManager = new JasonManager(war);
			war = jsonManager.readFromGson();
			jsonManager.setAllMissilesFromGson(); //instead of - war.setAllMissilesFromGson();
			war.initMissileDestructors();
			War.setCurrentTime(System.currentTimeMillis());
			jsonManager.startMissiles();
			jsonManager.startLaunchers();
			jsonManager.startMissileDestructors();
			jsonManager.startMissileLauncherDestructors();
			
			//join
			//System.out.println("War: "+war.toString());
			//menu();
		
		}
		else{
			menu();
		}
	}
	

	
	public void readFromGson(){
		if(Files.exists(Paths.get("war.json"))){
			Gson gson = new Gson();
			FileReader reader = null;
			try {
				reader = new FileReader("war.json");
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
			}
			war = gson.fromJson(reader, War.class);
			System.out.println(war.toString());
		}

    }
	
	
	public void menu(){
		Scanner s = new Scanner(System.in);
		int action;
		

		while(atWar) {
			System.out.println("Please choose an action:");
			System.out.println("1. Add a missile launcher\n"
					+ "2. Add a missile launcher destructor\n"
					+ "3. Add a missile destructor\n"
					+ "4. Launch missile\n"
					+ "5. Destruct a missile launcher\n"
					+ "6. Destruct a missile\n"
					+ "7. Show war conclusion\n"
					+ "8. End war\n");

			System.out.print("Enter your choice now: ");
			action = s.nextInt();
			s.nextLine();

			switch(action) {
			case 1:
				war.addMissileLauncher();
				break;

			case 2:
				boolean illegal = true;
				while(illegal ) {
					System.out.println("Choose 'Plane' or 'Ship'");
					String typeOfDestructor = s.nextLine();
					try {
						war.addMissileLauncherDestructor(typeOfDestructor);
						illegal = false;
					}
					catch(IllegalArgumentException ex) {
						System.out.println(typeOfDestructor + " is illegal choice");
					}
				}
				break;

			case 3:
				war.addMissileDestructor();
				break;	

			case 4:
				war.launchMissile("Sderot", 8, 1000); //just as an example
				break;

			case 5:
				war.destructMissileLauncher();
				break;

			case 6:
				war.destructMissile();
				break;

			case 7:
				war.sumUp();
				break;

			case 8:
				endWar();
				break;

			default:
				break;

			}
		}

	}

	private static void endWar() {
		// TODO Auto-generated method stub
		atWar = false;
	}

}
