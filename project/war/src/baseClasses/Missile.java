package baseClasses;

import com.google.gson.annotations.SerializedName;

import bussinesLogic.War;
import bussinesLogic.WarSummary;

public class Missile extends Thread implements Comparable<Missile> {

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

	public void flying() throws InterruptedException {
		addWaitingMissile();
		synchronized (launcher) {
			isFlying = true;
			didLaunched = true;
			System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId() + " starts flying for "+this.flyTime+ " sec");
			setLaunchTime();
			Thread.sleep(flyTime*1000);
			isFlying = false;
			System.out.println(War.getCurrentTime()+"--> Missile finished flying " + this.getMissileId() );
			launcher.setBusy(false);
			launcher.notify();
		}

		hit();
	}

	public synchronized void addWaitingMissile() throws InterruptedException {
		while(War.getCurrentTime() < this.getLaunchTime());
		launcher.addWaitingMissile(this);
//		System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId()  + " wait");
		wait();
//		System.out.println(War.getCurrentTime()+"--> Missile "+ this.getMissileId()+" woke up");
	}

	public void hit() {
		WarSummary.getInstance().addMissileHit();
		WarSummary.getInstance().addDamage(this.damage);
	}

	public void destructMissile(MissileDestructor theMissileDestructor) {
		if (War.getCurrentTime() >= flyTime + launchTime){
			System.out.println(War.getCurrentTime()+"--> Failed to destruct missile "+this.getMissileId());
		}
		else {
			System.out.println(War.getCurrentTime() +"--> Missile "+this.getMissileId() +" - Desctruct succeeded");
			WarSummary.getInstance().addDestructedMissile();
			isDestructed = true;
			this.interrupt();
		}
		theMissileDestructor.logMissile(logDestructedMissile());
	}

	public void setMissileDestructed() {
		System.out.println(War.getCurrentTime()+"--> thread "+this.getMissileId() +" interrupted");
		isFlying = false;
		setDestructAfterLaunch(War.getCurrentTime());
		logMissile(); 
		synchronized (launcher) {
			launcher.notify();			
		}
	}

	//for logger
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
	
	
	//getters and setters
	public void setLaunchTime(){
		this.launchTime = War.getCurrentTime();
	}
	
	public static int getIdGenerator() {
		return idGenerator;
	}

	public static void setIdGenerator() {
		Missile.idGenerator++;
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

//	public String getDestination() {
//		return destination;
//	}

//	public void setDestination(String destination) {
//		this.destination = destination;
//	}

//	public long getFlyTime() {
//		return flyTime;
//	}

//	public void setFlyTime(long flyTime) {
//		this.flyTime = flyTime;
//	}

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
		if(o != null)
			return (int)(this.launchTime - o.getLaunchTime());
		return 0;
	}
	
	@Override
	public String toString(){
		return " id:"+this.missileId + " destination:" + this.destination + " launchTime:"+this.launchTime +
				" flyTime:"+this.flyTime + " damage" + this.damage;

	}
}

//class SortByLaunchTime implements Comparator<Missile> {
//
//	@Override
//	public int compare(Missile m1, Missile m2) {
//		if(m1 != null && m2 != null)
//			return (int) (m1.getLaunchTime() - m2.getLaunchTime());
//		else return -1;
//	}
//	
//}
//
//class SortByDestructTime implements Comparator<Missile> {
//
//	@Override
//	public int compare(Missile m1, Missile m2) {
//		return (int) ((m1.getDestructAfterLaunch()+m1.getLaunchTime()) - 
//				(m2.getDestructAfterLaunch()+m2.getLaunchTime()));
//	}
//	
//}








