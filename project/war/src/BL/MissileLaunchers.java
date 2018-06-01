package BL;

import java.util.ArrayList;
import java.util.Random;

import baseClasses.Missile;
import baseClasses.MissileLauncher;

public class MissileLaunchers {
	private ArrayList<MissileLauncher> launcher = new ArrayList<>();
	private ArrayList<MissileLauncher> activeLauncher = new ArrayList<>();
	private int totalMissilesLaunched;
	private int totalMissilesHit;
	
	

	public MissileLaunchers(){
		totalMissilesLaunched = 0;
		totalMissilesHit = 0;
	}

	public void addMissileLauncher(){
		MissileLauncher theMissileLauncher = new MissileLauncher();
		launcher.add(theMissileLauncher);
		activeLauncher.add(theMissileLauncher);

		Thread missileLauncher = new Thread(theMissileLauncher);
		System.out.println("add missile launcher id:"+theMissileLauncher.getId());
		System.out.println("isHidden="+ theMissileLauncher.isHidden());
		missileLauncher.start();
	}

	public void launchMissile(String destination, int flyTime, int damage, ArrayList<Missile> allMissiles){
		if(activeLauncher.size() == 0){
			//end of war
			return;
		}
		else{
			int randomIndex = randomNumber(0, activeLauncher.size());
			MissileLauncher launcher = activeLauncher.get(randomIndex);
			Missile theMissile = new Missile(destination, flyTime, damage, launcher);
			allMissiles.add(theMissile);
			launcher.addMissile(theMissile); //not sure it's needed, in "flying" there's already "addMissile"
			//maybe theMissile.start(); here
			totalMissilesLaunched++;
		} 
	}

	public MissileLauncher findMissileLauncher(){
		if(activeLauncher.size() == 0)
			return null;
		int randomIndex = randomNumber(0, activeLauncher.size());
		MissileLauncher randLauncher = activeLauncher.get(randomIndex);
		if(!randLauncher.isHidden())
			activeLauncher.remove(randomIndex);
		return randLauncher;
	}

	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}

}
