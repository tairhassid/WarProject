package war;

import java.util.Random;

public class Missile extends Thread {
	private final static int ZERO = 0;
	private final static int ONE =1;
	private static int idGenerator = 0;
	private String id;
	private String destination;
	private long launchTime;
	private long flyTime;
	private int damage;
	//private long destructAfterLaunch;
	private MissileLaunchers launcher;
	private boolean isDestructed;

	public Missile(String destination, int flyTime, int damage, MissileLaunchers launcher) {
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
			setLaunchTime();
			Thread.sleep(flyTime);
			System.out.println("finishedFlying " + this.getId());
			isDestructed = true;
		}
		synchronized (launcher) {
			launcher.notifyAll();
		}
	}

	public void setLaunchTime(){
		this.launchTime = BusinessLogic.getCurrentTime();
	}

	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}

	public long getLaunchTime(){
		return launchTime;
	}

	/*	public boolean getIsDestructed() {
		return isDestructed;
	}*/


}
