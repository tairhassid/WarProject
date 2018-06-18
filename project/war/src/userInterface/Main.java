package userInterface;



import bussinesLogic.LoggerManager;
import bussinesLogic.War;

public class Main {

	public static void main(String[] args) {
		//gui or console
		//json or new game
		
		War war = new War();
		LoggerManager loggerManager = new LoggerManager();
		ConsoleGame cg = new ConsoleGame(war);
		
		cg.startGame();
		

	}

}
