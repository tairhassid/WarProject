package userInterface;

import java.util.Scanner;



import bussinesLogic.JasonManager;
import bussinesLogic.War;


public class ConsoleGame {
	private static boolean atWar = true;
	private War war;
	private int[] flyTimeByArea;

	public ConsoleGame(War war){
		this.war = war;
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

			menu();

		}
		else{
			menu();
		}
	}



	//	public void readFromGson(){
	//		if(Files.exists(Paths.get("war.json"))){
	//			Gson gson = new Gson();
	//			FileReader reader = null;
	//			try {
	//				reader = new FileReader("war.json");
	//			}
	//			catch(FileNotFoundException e){
	//				e.printStackTrace();
	//			}
	//			war = gson.fromJson(reader, War.class);
	//			System.out.println(war.toString());
	//		}
	//
	//	}


	public void menu(){
		Scanner s = new Scanner(System.in);
		int action;
		flyTimeSettings();

		while(atWar) {
			System.out.println("\nPlease choose an action:");
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
			War.nowGsonGame = false;

			switch(action) {
			case 1:
				war.addMissileLauncher();
				break;

			case 2:
				boolean illegal = true;
				while(illegal) {
					System.out.println("Choose 'Plane' or 'Ship'");
					String typeOfDestructor = s.nextLine();

					try {
						typeOfDestructor = typeOfDestructor.substring(0, 1).toUpperCase().concat(typeOfDestructor.substring(1).toLowerCase());
						war.addMissileLauncherDestructor(typeOfDestructor);
						illegal = false;
					}
					catch(IllegalArgumentException ex) {
						System.out.println(typeOfDestructor + " is illegal choice\n");
					}
				}
				break;

			case 3:
				war.addMissileDestructor();
				break;	

			case 4:
				illegal = true;

				if(war.getMissileLaunchers().getActiveLaunchers().size() > 0) {
					
					while(illegal) {
						System.out.print("Please enter a destination to hit- area 1, 2 or 3: --> ");
						int dest = s.nextInt();
						if(dest <= 3 && dest >= 1) {

							illegal = false;
							int randDamage = War.randomNumber(1000, 5001);
							war.launchMissile("Area " + dest, flyTimeByArea[dest-1], randDamage); //just as an example
						}
						else
							System.out.println("Area " + dest + " does not exist\n");
					}
				}
				else 
					System.out.println("\nCan't launch a missile: There are no active launchers. You need to add one!");

			break;

		case 5:
			if(war.getMissileLauncherDestructors().getDestructor().size() == 0)
				System.out.println("\nCan't destruct launchers: There are no missile launcher destructors. You need to add one!");
			else if(war.getMissileLaunchers().getActiveLaunchers().size() == 0)
				System.out.println("\nCan't destruct launchers: There are no missile launchers to destruct!");
			else
				war.destructMissileLauncher();

			break;

		case 6:
			if(war.findMissile() == null)
				System.out.println("\nCan't destruct missile: There are no currently flying missiles");
			else if(war.getMissileDestructors().getDestructors().size() == 0)
				System.out.println("\nCan't destruct missile: There are no missile destructors. You need to add one!");
			else
				war.destructMissile();
			break;

		case 7:
			war.getWarSummary();
			break;

		case 8:
			endWar();
			break;

		default:
			break;

		}
	}
	s.close();

}

private void flyTimeSettings() {
	flyTimeByArea = new int[3];
	flyTimeByArea[0] = 5;
	flyTimeByArea[1] = 6;
	flyTimeByArea[2] = 7;
}

private void endWar() {
	System.out.println("enter end war");
	war.getWarSummary();
	war.endWar();
	atWar = false;
}

}
