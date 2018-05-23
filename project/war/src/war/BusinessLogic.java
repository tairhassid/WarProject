package war;

import java.util.ArrayList;
import java.util.Calendar;

import war.MissileLauncherDestructors.type;

public class BusinessLogic {
	
	public static long timer;
	private static BusinessLogic instance;
	
	private ArrayList<MissileLaunchers> missileLaunchers = new ArrayList<>();
	private ArrayList<MissileDestructors> missileDestructors = new ArrayList<>();
	private ArrayList<MissileLauncherDestructors> missileLauncherDestructors = new ArrayList<>();
	private ArrayList<Missile> allMissiles = new ArrayList<>();

	private int totalMissilesLaunched;
	private int totalMissilesDestructed;
	private int totalSuccessfulLaunched;
	private int totalDamage;
	private int totalMissileLauncherDestroyed;

	private  BusinessLogic() {
		totalMissilesLaunched = 0;
		totalMissilesDestructed = 0;
		totalSuccessfulLaunched = 0;
		totalDamage = 0;
		totalMissileLauncherDestroyed = 0;
		timer = Calendar.getInstance().getTimeInMillis(); 
	}

	public static BusinessLogic getInstance(){
		if(instance == null){
			instance = new BusinessLogic();
		}
		return instance;
	}

	public void addMissileLauncher(){
		MissileLaunchers theMissileLauncher = new MissileLaunchers();
		missileLaunchers.add(theMissileLauncher);

		Thread missileLauncher = new Thread(theMissileLauncher);
		System.out.println("add missile launcher");
		missileLauncher.start();
	}

	public void addMissileDestructor(){
		MissileDestructors theMissileDestructor = new MissileDestructors();
		missileDestructors.add(theMissileDestructor);

		Thread missileDestructor = new Thread(new MissileDestructors());
		missileDestructor.start();
	}

	public void addMissileLauncherDestructor(String theType) throws IllegalArgumentException { 
		MissileLauncherDestructors.type t = type.valueOf(theType);
		MissileLauncherDestructors theMissileLauncherDestructor = new MissileLauncherDestructors(t);
		missileLauncherDestructors.add(theMissileLauncherDestructor);

		Thread missileLauncherDestructor = new Thread(new MissileDestructors());
		missileLauncherDestructor.start();
		
	}

	public void launchMissile(String destination, int flyTime, int damage){
		MissileLaunchers launcher = findMissileLauncher();
		if(launcher == null){
			//end of war
		}
		else {
			Missile theMissile = new Missile(destination, flyTime, damage, launcher);
			allMissiles.add(theMissile);
			launcher.addMissile(theMissile); //not sure it's needed, in "flying" there's already "addMissile"
			//maybe theMissile.start(); here
			totalMissilesLaunched++;
		} 
	}


	private MissileLaunchers findMissileLauncher(){
		for(MissileLaunchers theMissileLauncher : missileLaunchers){
			if(!theMissileLauncher.isDestroyed()){
				return theMissileLauncher;
			}
		}
		return null;
	}

	public void destructMissileLauncher(){
		MissileLauncherDestructors theMissileLauncherDestructor = findMissileLauncherDestructor();
		if (theMissileLauncherDestructor == null){
			//No Missile launcher destructor exist
		}
		else{
			MissileLaunchers theMissileLauncher = findMissileLauncher(); //Any missile Launcher that exist in the array and is not destroyed
			if(theMissileLauncherDestructor.destructMissileLauncher(theMissileLauncher))
				totalMissileLauncherDestroyed++;
			//interrupt the thread
		}

	}

	private MissileLauncherDestructors findMissileLauncherDestructor(){
		if (!missileLauncherDestructors.isEmpty()){
			return missileLauncherDestructors.get(0);
		}
		return null;
	}

	public void destructMissile(){
		MissileDestructors theMissileDestructor = findMissileDestructor();
		if (theMissileDestructor == null){
			//No missile destructor exist
		}
		else {
			//Missile theMissile = findMissile();
			for(Missile theMissile : allMissiles)
				if(theMissile.getLaunchTime() > 0 && theMissileDestructor.destructMissile(theMissile)) {
					totalMissilesDestructed++;
					break;
			}
		}
	}

//	private Missile findMissile() {
//		for(Missile theMissile : allMissiles)
//			if(theMissile.destructMissile(getCurrentTime()))
//				return theMissile;
//		return null;
//	}

	private MissileDestructors findMissileDestructor(){
		if (!missileDestructors.isEmpty())
			return missileDestructors.get(0);
		return null;
	}
	
	public void destructMissileLauncherDestructor() {
		MissileLauncherDestructors theMissileLauncherDestructor = findMissileLauncherDestructor();
		if(theMissileLauncherDestructor != null)
			theMissileLauncherDestructor.destructSelf();
	}

	public void sumUp() {
		// TODO 
	}
	
	public static long getCurrentTime() {
		return Calendar.getInstance().getTimeInMillis() - timer;
	}

	public int getTotalMissilesLaunched() {
		return totalMissilesLaunched;
	}

	public int getTotalMissilesDestructed() {
		return totalMissilesDestructed;
	}

	public int getTotalSuccessfulLaunched() {
		return totalSuccessfulLaunched;
	}

	public int getTotalDamage() {
		return totalDamage;
	}

	public int getTotalMissileLauncherDestroyed() {
		return totalMissileLauncherDestroyed;
	}

}


