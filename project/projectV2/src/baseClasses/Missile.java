package baseClasses;


import com.google.gson.annotations.SerializedName;

import bussinesLogic.War;

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


	private MissileLauncher launcher;
	private boolean isDestructed;
	
	public Missile(){}

	public Missile(String destination, int flyTime, int damage, MissileLauncher launcher) {
		this.missileId = "M" + (++idGenerator);
		this.destination = destination;
		this.flyTime = flyTime;
		this.damage = damage;
		this.launcher = launcher;
		this.isDestructed = false;
		this.launchTime = 0;
		this.destructAfterLaunch = 0;
	}

/*	@Override
	public void run() {
		try {
			flying();
		} catch (InterruptedException e) {
			setMissileDestructed();
		}
	}*/
	
	@Override
	public void run() {
		synchronized (launcher) {
			try {
				System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId() + " starts flying");
				setLaunchTime();
				Thread.sleep(flyTime*1000);
				System.out.println(War.getCurrentTime()+"--> Missile finished flying " + this.getMissileId() );
			} catch (InterruptedException e) {
				setMissileDestructed();
			}
		}
	}
	
	public void setMissileDestructed(){
		System.out.println(War.getCurrentTime()+"--> thread "+this.getMissileId() +" interrupted");
		isDestructed = true;
		setDestructAfterLaunch(War.getCurrentTime());
		synchronized (launcher) {
			launcher.notifyAll();			
		}
		
	}

	public boolean destructMissile() {
		if (War.getCurrentTime() >= flyTime + launchTime){
			System.out.println(War.getCurrentTime()+"--> Failed to destruct missile "+this.getMissileId());
			return false;
		}
		else {
//			try {
				System.out.println(War.getCurrentTime() +"--> Missile "+this.getMissileId() +" - Desctruct succeeded");
				this.interrupt();
				//Thread.sleep(War.randomNumber(MIN_TIME, MAX_TIME));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
		}
		return true;
	}

	public void flying() throws InterruptedException {
		synchronized (this) {
			launcher.addWaitingMissile(this);
			System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId()  + " wait");
			wait();
		}
		synchronized (launcher) {
			System.out.println(War.getCurrentTime()+"--> Missile " + this.getMissileId() + " starts flying");
			setLaunchTime();
			Thread.sleep(flyTime*1000);
			System.out.println(War.getCurrentTime()+"--> Missile finished flying " + this.getMissileId() );
			launcher.notifyAll();
		}
			
		isDestructed = true;
		
	}

	public void setLaunchTime(){
		this.launchTime = War.getCurrentTime();
	}

//	public int randomNumber(int from, int to){
//		Random rand = new Random();
//		int number = rand.nextInt(to) + from;
//
//		return number;
//	}

	public long getLaunchTime(){
		return launchTime;
	}
	

	public String getMissileId() {
		return missileId;
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
