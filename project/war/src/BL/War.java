package BL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import baseClasses.Missile;
import baseClasses.MissileLauncher;
import baseClasses.MissileLauncherDestructor;
import baseClasses.MissileLauncherDestructor.type;

public class War {
	
	public static long timer;
	private static War instance;
	private ArrayList<Missile> allMissiles = new ArrayList<>();
	private int totalDamage;
	private MissileLaunchers missileLaunchers = new MissileLaunchers();
	private MissileDestructors missileDestructors = new MissileDestructors();
	private MissileLauncherDestructors missileLauncherDestructors = new MissileLauncherDestructors();

	private  War() {
		totalDamage = 0;
		timer = Calendar.getInstance().getTimeInMillis();
	}

	public static War getInstance(){
		if(instance == null){
			instance = new War();
		}
		return instance;
	}


	public void sumUp() {
		// TODO 
	}
	
	
	public static long getCurrentTime() {
		return Calendar.getInstance().getTimeInMillis() - timer;
	}

	public int getTotalDamage() {
		return totalDamage;
	}
	
	public void addMissileLauncher(){
		missileLaunchers.addMissileLauncher();
	}
	
	public void addMissileLauncherDestructor(String theType){
		MissileLauncherDestructor.type t = type.valueOf(theType);
		missileLauncherDestructors.addMissileLauncherDestructor(t);
	}
	
	public void addMissileDestructor(){
		missileDestructors.addMissileDestructor();
	}
	
	public void launchMissile(String destination, int flyTime, int damage){
		missileLaunchers.launchMissile(destination, flyTime, damage, allMissiles);
	}
	
	public void destructMissileLauncher(){
		MissileLauncher theMissileLauncher = missileLaunchers.findMissileLauncher();
		if(theMissileLauncher == null){
			//end of war
			System.out.println("No missiles launchers left");
			return;
		}
		missileLauncherDestructors.destructMissileLauncher(theMissileLauncher);
	}
	
	public void destructMissile(){
		missileDestructors.destructMissile(allMissiles);
	}
	
	public static int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}

}


