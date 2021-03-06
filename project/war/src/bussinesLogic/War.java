package bussinesLogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import baseClasses.Missile;
import baseClasses.MissileLauncher;
import baseClasses.MissileLauncherDestructor;
import baseClasses.MissileLauncherDestructor.DestructorType;

public class War {
	Scanner s = new Scanner(System.in);
	public static final String LOG_PATH = "logFiles\\";
	public static long timer;
	public static boolean warIsOn = true;
	private ArrayList<Missile> allMissiles = new ArrayList<>();
	private int totalDamage;
	private MissileLaunchers missileLaunchers = new MissileLaunchers();
	private MissileDestructors missileDestructors = new MissileDestructors();
	private MissileLauncherDestructors missileLauncherDestructors = new MissileLauncherDestructors();
	public static boolean gsonGame;
	public static boolean nowGsonGame;
	
	private static War war = null;
	
	public static War getInstance(){
		if(war == null)
			war = new War();
		return war;
	}

	
	private War() {
		warIsOn = true;
		totalDamage = 0;
		timer = System.currentTimeMillis();
		gsonGame = false;
		
	}

	public void initMissileDestructors(){
		for (Missile m : allMissiles){
			missileDestructors.initMissileDestructor(m);
		}
	}

	public static void setCurrentTime(long currentTime){
		timer = currentTime;
	}
	
	public static long getCurrentTime() {
		return (System.currentTimeMillis() - timer)/1000;
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
	
	public void launchMissile(String destination){
		int randDamage = randomNumber(1000, 5001);
		int flyTime = randomNumber(2,9);
		missileLaunchers.launchMissile(destination, flyTime, randDamage, allMissiles);
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
		System.out.println("Missile found to destruct: "+ theMissile);
		if(theMissile == null){
			//No missiles to destruct
			System.out.println("No missiles to destruct");
			return;
		}
		missileDestructors.destructMissile(theMissile);
	}
	
	public Missile findMissile(){//returns the first missile that hasn't been destructed yet and already launched
		for (Missile m : allMissiles){// need to check if isFlying!!!
			if(checkForFlyingMissiles(m)) //checking if is flying
				return m;
		}
		return null;
	}
	
	public boolean checkForFlyingMissiles(Missile m) {
		if(!m.getIsDestructed() && m.getLaunchTime() > 0 && m.getLaunchTime() <= getCurrentTime()
				&& m.isFlying())
			return true;
		return false;
	}
	
	public static int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}

	public boolean ifGsonGame() {
		
		String answer;
		System.out.println("Do you wish to load game from file? y/n");
		answer = s.nextLine();
		if (answer.equalsIgnoreCase("y")){
			return true;
		}
		return false;	
	}
	
//	public void setWarSummary(){
//		if(allMissiles.isEmpty()){
//			//do nothing
//			System.err.println("The war haven't started yet!");
//			return;
//		}
//		for (Missile m : allMissiles){
//			if(m.getDestructAfterLaunch() < m.getFlyTime()){ //missile got destroyed my missile destructor
//				warSummary.addDestructedMissile();
//			}
//			else{ //missile landed safely
//				warSummary.addMissileHit();
//				warSummary.addDamage(m.getDamage());
//			}
//		}
//		warSummary.setTotalLaunchedMissiles(allMissiles.size());
//	}
	@Override
	public String toString(){
		//String str = missileDestructors.toString();
		//String str = missileLauncherDestructors.toString();
		return "War:" + missileLauncherDestructors.toString();
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

	public void initMissileLauncherDestructors() {
		for (MissileLauncher ml : missileLaunchers.getLauncher()){
			System.out.println(ml.getId());
			missileLauncherDestructors.initMissileDestructor(ml);
		}
		
	}
	
	public void getWarSummary(){
		Iterator<Missile> iterMissiles = allMissiles.iterator();
		Iterator<MissileLauncher> iterLaunchers = missileLaunchers.getActiveLaunchers().iterator();
		
//		if(allMissiles.isEmpty()){
//			System.out.println("the war hasn't began yet!");
//			return;
//		}
		
		if(gsonGame){ //there is a chance that a missile from gson never got launched
			while(iterMissiles.hasNext()){
				Missile m = iterMissiles.next();
				if(!m.getDidLaunched() && allMissiles.contains(m))
					iterMissiles.remove();
			}
			
//			while(iterLaunchers.hasNext()){
//				MissileLauncher ml = iterLaunchers.next();
//				if(ml.getIsDestroyed())
//					iterLaunchers.remove();
//			}
		}
		WarSummary.getInstance().setTotalDestroyedMissilesDestructors(missileLaunchers.getLauncher().size() - missileLaunchers.getActiveLaunchers().size());
		WarSummary.getInstance().setTotalLaunchedMissiles(allMissiles.size());
		System.out.println(WarSummary.getInstance().getWarSummary());
	}
	
	public void setGsonGame(boolean gsonGame){
		War.gsonGame = gsonGame;
	}
	
	public void endWar() {
		System.out.println("in war- end war");
		warIsOn = false;
		missileLaunchers.endWar();
		missileDestructors.endWar();
		missileLauncherDestructors.endWar();
		
	}


}


