package war;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MissileDestructors implements Runnable{
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	private static int idGenerator = 200;
	private String id;
	private Map<Missile,Long> destructedMissile;
	private long destructAfterLaunch;

	public MissileDestructors(){
		this.id = "D" + (++idGenerator);
		this.destructedMissile = new HashMap<>();
	}

	@Override
	public void run() {

	}

	public boolean destructMissile(Missile theMissile){
		//		try {
		//			Thread.sleep(randomNumber(MIN_TIME, MAX_TIME));
		destructAfterLaunch = (BusinessLogic.getCurrentTime());
		if(!destructedMissile.containsKey(theMissile)) {
			destructedMissile.put(theMissile, destructAfterLaunch);
		}
		return theMissile.destructMissile(destructAfterLaunch);
		
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		//		return false;
	}

	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}
}




