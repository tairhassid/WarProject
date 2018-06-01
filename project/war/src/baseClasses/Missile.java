package baseClasses;

//import java.util.Random;

import BL.War;

public class Missile extends Thread {
//	private final static int ZERO = 0;
//	private final static int ONE =1;
	private static int idGenerator = 0;
	private String id;
	private String destination;
	private long launchTime;
	private long flyTime;
	private int damage;
	//private long destructAfterLaunch;
	private MissileLauncher launcher;
	private boolean isDestructed;
	
	public Missile(){}

	public Missile(String destination, int flyTime, int damage, MissileLauncher launcher) {
		this.id = "M" + (++idGenerator);
		this.destination = destination;
		this.flyTime = flyTime;
		this.damage = damage;
		this.launcher = launcher;
		this.isDestructed = false;
		this.launchTime = 0;
	}

	@Override
	public void run() {
		try {
			flying();
		} catch (InterruptedException e) {
			System.out.println("thread interrupted" + this.getId());
			isDestructed = true;
		}
	}

	public boolean destructMissile(long currentTime) {
		if ( currentTime > flyTime + launchTime)
			return false;
		else {
			System.out.println("in Missile - Desctruct succeeded=" + this.getId());
			this.interrupt();
		}
		return true;
	}

	public void flying() throws InterruptedException {
		synchronized (this) {
			launcher.addWaitingMissile(this);
			System.out.println("Missile " + this.getId() + " wait");
			wait();
		}
		synchronized (launcher) {
			System.out.println("Missile " + this.getId() + " starts flying");
			setLaunchTime();
			Thread.sleep(flyTime);
			System.out.println("finishedFlying " + this.getId());
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
		return id;
	}

	public void setId(String id) {
		this.id = id;
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


}
