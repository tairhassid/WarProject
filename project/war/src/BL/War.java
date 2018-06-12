package BL;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import baseClasses.Missile;
import baseClasses.MissileLauncher;
import baseClasses.MissileLauncherDestructor;
import baseClasses.MissileLauncherDestructor.DestructorType;

public class War {
	
	public static long timer;
	//private static War instance;
	private ArrayList<Missile> allMissiles = new ArrayList<>();
	private int totalDamage;
	private MissileLaunchers missileLaunchers = new MissileLaunchers();
	private MissileDestructors missileDestructors = new MissileDestructors();
	private MissileLauncherDestructors missileLauncherDestructors = new MissileLauncherDestructors();
	
    

	public  War() {
		totalDamage = 0;
		timer = Calendar.getInstance().getTimeInMillis();
	}
	
//	public void readFromGson(){
//		  try (Reader reader = new FileReader("staff.json")) {
//
//		        // Convert JSON to Java Object
//		        Staff staff = gson.fromJson(reader, Staff.class);
//		        System.out.println(staff);
//
//
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//	}

//	public static War getInstance(){
//		if(instance == null){
//			instance = new War();
//		}
//		return instance;
//	}


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
		MissileLauncherDestructor.DestructorType t = DestructorType.valueOf(theType);
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
		Missile theMissile = findMissile();
		if(theMissile == null){
			//No missiles to destruct
			System.out.println("No missiles to destruct");
			return;
		}
		missileDestructors.destructMissile(theMissile);
	}
	
	public Missile findMissile(){//this method will return the same missile if it's not distracted yet
		for (Missile m : allMissiles){
			if(!m.getIsDestructed()){
				return m;
			}
		}
		return null;
	}
	
	public static int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}

	public boolean ifGsonGame() {
		Scanner s = new Scanner(System.in);
		String answer;
		System.out.println("Do you wish to load game from file? y/n");
		answer = s.nextLine();
		if (answer.equalsIgnoreCase("y")){
			return true;
		}
		return false;
		
	}
	@Override
	public String toString(){
		return missileLaunchers.toString();
	}
	
	public MissileLaunchers getMissileLaunchers() {
		return missileLaunchers;
	}

	public void setMissileLaunchers(MissileLaunchers missileLaunchers) {
		this.missileLaunchers = missileLaunchers;
	}

	public MissileDestructors getMissileDestructors() {
		return missileDestructors;
	}

	public void setMissileDestructors(MissileDestructors missileDestructors) {
		this.missileDestructors = missileDestructors;
	}

	public MissileLauncherDestructors getMissileLauncherDestructors() {
		return missileLauncherDestructors;
	}

	public void setMissileLauncherDestructors(MissileLauncherDestructors missileLauncherDestructors) {
		this.missileLauncherDestructors = missileLauncherDestructors;
	}

	public ArrayList<Missile> getAllMissiles() {
		return allMissiles;
	}

	public void setAllMissiles(ArrayList<Missile> allMissiles) {
		this.allMissiles = allMissiles;
	}
	
	public void setAllMissilesFromGson(){
		setAllMissiles(missileLaunchers.getAllMissiles());
	}
	
	public void setActiveLaunchers(){
		missileLaunchers.setActiveLaunchers();
	}
	

}


