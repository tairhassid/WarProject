package war;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MissileLauncherDestructors implements Runnable{
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	private enum type {Plane, Ship};
	private type theType;
	private Map<MissileLaunchers,Long> destructedMissile;
	private long destructAfterLaunch;
	
	public MissileLauncherDestructors(type theType) {
		this.theType = theType;
		this.destructedMissile = new HashMap<>();
	}

	@Override
	public void run() {
		//TBD
	}
	
	public void destructMissileLauncher(MissileLaunchers theMissileLauncher){
		try {
			destructAfterLaunch = Calendar.getInstance().getTimeInMillis() / 1000;
			destructedMissile.put(theMissileLauncher, destructAfterLaunch);
			Thread.sleep(randomNumber(MIN_TIME, MAX_TIME));
			theMissileLauncher.destructMissileLauncher();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;
		
		return number;
	}
	
	
	
	
	
}
