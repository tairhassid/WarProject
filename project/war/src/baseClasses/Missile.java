package baseClasses;


import java.util.Comparator;

import com.google.gson.annotations.SerializedName;

import bussinesLogic.War;
import bussinesLogic.WarSummary;

public class Missile extends Thread implements Comparable<Missile> {

	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	//	private final static int ZERO = 0;
	//	private final static int ONE =1;
	private static int idGenerator = 0;
	@SerializedName("id")
	private String missileId;
	private String destination;
	private long launchTime;
	private long flyTime;
	private int damage;
	private long destructAfterLaunch;
	private boolean didLaunched;
	private boolean isFlying;
	
	


	private MissileLauncher launcher;
	private boolean isDestructed;

	public Missile(){
		++idGenerator;
	}

	public Missile(String destination, int flyTime, int damage, MissileLauncher launcher) {
		this.missileId = "M" + (++idGenerator);
		this.destination = destination;
		this.flyTime = flyTime;
		this.damage = damage;
		this.launcher = launcher;
		this.isDestructed = false;
		this.launchTime = 0;
		this.destructAfterLaunch = 0;
		this.didLaunched = false;
		this.isFlying = false;
		
		
	}

	@Override
	public void run() {
		try {
			flying();
			logMissile();
		} catch (InterruptedException e) {
			setMissileDestructed();
		}
	}


	public void setMissileDestructed(){
		System.out.println(War.getCurrentTime()+"--> thread "+this.getMissileId() +" interrupted");
		//isDestructed = true;
		isFlying = false;
		setDestructAfterLaunch(War.getCurrentTime());
		logMissile(); 
		synchronized (launcher) {
			launcher.notifyAll();			
		}

	}

	public void destructMissile(MissileDestructor theMissileDestructor) {
		if (War.getCurrentTime() >= flyTime + launchTime){
			System.out.println(War.getCurrentTime()+"--> Failed to destruct missile "+this.getMissileId());
			//return false;
		}
		else {
			System.out.println(War.getCurrentTime() +"--> Missile "+this.getMissileId() +" - Desctruct succeeded");
			WarSummary.getInstance().addDestructedMissile();
			isDestructed = true;
			this.interrupt();
		}
		theMissileDestructor.logMissile(logDestructedMissile());
		//return true;
	}
	
	public String logDestructedMissile(){
		StringBuffer buf = new StringBuffer();
		
		buf.append("Target Missile: "+ missileId);
		if(isDestructed){
			buf.append("\nMissile destroyed!\n");
		}
		else{
			buf.append("\nDestruct failed");
			buf.append("\nDamage: "+damage+"\n");
		}
		
		return buf.toString();
	}

	public void flying() throws InterruptedException {
		synchronized (this) {
			launcher.addWaitingMissile(this);
			System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId()  + " wait");
			wait();
			System.out.println(War.getCurrentTime()+"--> Missile "+ this.getMissileId()+" woke up");
		}
		synchronized (launcher) {
			isFlying = true;
			didLaunched = true;
			System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId() + " starts flying for "+this.getFlyTime()+ " sec");
			setLaunchTime();
			Thread.sleep(flyTime*1000);
			isFlying = false;
			System.out.println(War.getCurrentTime()+"--> Missile finished flying " + this.getMissileId() );
			launcher.setBusy(false);
			launcher.notify();
		}

		hit();

	}
	
	public void hit(){
		//isDestructed = true; // missile landed safely
		WarSummary.getInstance().addMissileHit();
		WarSummary.getInstance().addDamage(this.damage);
	}
	
	public void addWitingMissile() throws InterruptedException{
		synchronized (this) {
			launcher.addWaitingMissile(this);
			System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId()  + " wait");
			wait();
			System.out.println(War.getCurrentTime()+"--> Missile "+ this.getMissileId()+" woke up");
		}
	}

	public void setLaunchTime(){
		this.launchTime = War.getCurrentTime();
	}

	public void logMissile(){
		StringBuffer buf = new StringBuffer();
		
		buf.append("Missile id: "+ missileId);
		buf.append("\nDestination: "+ destination);
		buf.append("\nLaunch time:"+ launchTime);
		
		if(isDestructed){
			buf.append("\nMissile destroyed: "+ destructAfterLaunch+"\n");
		}
		else{
			buf.append("\nMissile landed: "+ (launchTime + flyTime));
			buf.append("\nDamage: "+damage+"\n");
		}
		
		launcher.logMissile(buf);
	}

	public long getLaunchTime(){
		return launchTime;
	}


	public String getMissileId() {
		return missileId;
	}
	
	public boolean isFlying(){
		return this.isFlying;
	}


	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public long getFlyTime() {
		return flyTime;
	}

	public void setFlyTime(long flyTime) {
		this.flyTime = flyTime;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public boolean getIsDestructed() {
		return isDestructed;
	}

	public long getDestructAfterLaunch() {
		return destructAfterLaunch;
	}

	public void setDestructAfterLaunch(long destructAfterLaunch) {
		this.destructAfterLaunch = destructAfterLaunch;
	}
	
	public boolean getDidLaunched(){
		return this.didLaunched;
	}
	
	public void setDidLaunched(boolean didLaunched){
		this.didLaunched = didLaunched;
	}

	@Override
	public String toString(){
		return " id:"+this.missileId + " destination:" + this.destination + " launchTime:"+this.launchTime +
				" flyTime:"+this.flyTime + " damage" + this.damage;

	}


	public void setMissileId(String missileId) {
		this.missileId = missileId;
	}

	public void setLauncher(MissileLauncher launcher){
		this.launcher = launcher;
	}

	public MissileLauncher getLauncher(){
		return this.launcher;
	}

	public void setLaunchTime(long launchTime){
		this.launchTime = launchTime;
	}

	@Override
	public int compareTo(Missile o) {
		return  (int)(this.launchTime - o.getLaunchTime());
	}
	
}
	
class SortByLaunchTime implements Comparator<Missile> {

	@Override
	public int compare(Missile m1, Missile m2) {
		return (int) (m1.getLaunchTime() - m2.getLaunchTime());
	}
	
}
	
	






