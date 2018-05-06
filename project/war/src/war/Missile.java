package war;


import java.util.Calendar;
import java.util.Random;

public class Missile implements Runnable {
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
	}

	@Override
	public void run() {
		while(!isDestructed) {
			try {
				flying();
			} catch (InterruptedException e) {
				isDestructed = true;
				e.printStackTrace();
			}
		}

	}
	
	public boolean destructMissile(long currentTime){
		if ( currentTime > flyTime)
			return isDestructed = false;
		else
			return isDestructed = true;
	}

	public void flying() throws InterruptedException {
		setLaunchTime();
		launcher.addMissile(this);
		wait();
		Thread.sleep(flyTime);
		launcher.notify();
	}
	
	public void setLaunchTime(){
		this.launchTime = Calendar.getInstance().getTimeInMillis()/1000;
	}
	
	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;
		
		return number;
	}
	
	public long getLaunchTime(){
		return launchTime;
	}


}
