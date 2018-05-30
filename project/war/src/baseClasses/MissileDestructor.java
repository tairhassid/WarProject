package baseClasses;

import java.util.HashMap;
import java.util.Map;
//import java.util.Random;

import BL.War;

public class MissileDestructor implements Runnable{
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	private static int idGenerator = 200;
	private String id;
	private Map<Missile,Long> destructedMissile;
	private long destructAfterLaunch;

	public MissileDestructor(){
		this.id = "D" + (++idGenerator);
		this.destructedMissile = new HashMap<>();
	}

	@Override
	public void run() {

	}

	public boolean destructMissile(Missile theMissile){
				//try {
					//Thread.sleep(War.randomNumber(MIN_TIME, MAX_TIME));
		destructAfterLaunch = (War.getCurrentTime());
		if(!destructedMissile.containsKey(theMissile)) {
			destructedMissile.put(theMissile, destructAfterLaunch);
		}
		return theMissile.destructMissile(destructAfterLaunch);
		
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		//		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<Missile, Long> getDestructedMissile() {
		return destructedMissile;
	}

	public void setDestructedMissile(Map<Missile, Long> destructedMissile) {
		this.destructedMissile = destructedMissile;
	}

	public long getDestructAfterLaunch() {
		return destructAfterLaunch;
	}

	public void setDestructAfterLaunch(long destructAfterLaunch) {
		this.destructAfterLaunch = destructAfterLaunch;
	}

//	public int randomNumber(int from, int to){
//		Random rand = new Random();
//		int number = rand.nextInt(to) + from;
//
//		return number;
//	}
}




