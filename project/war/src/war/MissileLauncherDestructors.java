package war;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MissileLauncherDestructors implements Runnable {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	public enum type {Plane, Ship};
	private Map<MissileLaunchers,Long> destructedMissile;
	private long destructAfterLaunch;
	private boolean isDesructed;
	
	public MissileLauncherDestructors(type theType) {
		this.destructedMissile = new HashMap<>();
		this.isDesructed = false;
	}

	@Override
	public void run() {
		while(!isDesructed) {
			// TODO
		}
	}
	
	public boolean destructMissileLauncher(MissileLaunchers theMissileLauncher){
		try {
			destructAfterLaunch = BusinessLogic.getCurrentTime();
			destructedMissile.put(theMissileLauncher, destructAfterLaunch);
			Thread.sleep(randomNumber(MIN_TIME, MAX_TIME));
			return theMissileLauncher.destructMissileLauncher();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean destructSelf() {
		return isDesructed = true;
	}
	
	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;
		
		return number;
	}
}
