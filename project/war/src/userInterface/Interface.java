package userInterface;

import java.io.IOException;
import java.util.Scanner;

import BL.War;
import baseClasses.MissileLauncherDestructor;
import baseClasses.MissileLauncherDestructor.type;

public class Interface {
	private static boolean atWar = true;
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int action;
		
		War logic = War.getInstance();

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
				logic.addMissileLauncher();
				break;

			case 2:
				boolean illegal = true;
				while(illegal ) {
					System.out.println("Choose 'Plane' or 'Ship'");
					String typeOfDestructor = s.nextLine();
					try {
						logic.addMissileLauncherDestructor(typeOfDestructor);
						illegal = false;
					}
					catch(IllegalArgumentException ex) {
						System.out.println(typeOfDestructor + " is illegal choice");
					}
				}
				break;

			case 3:
				logic.addMissileDestructor();
				break;	

			case 4:
				logic.launchMissile("Sderot", 8000, 1000); //just as an example
				break;

			case 5:
				logic.destructMissileLauncher();
				break;

			case 6:
				logic.destructMissile();
				break;

			case 7:
				logic.sumUp();
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
