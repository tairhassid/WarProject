package baseClasses;

import java.util.Vector;
import BL.War;


public class MissileDestructor {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	private static int idGenerator = 200;
	private String id;
	//private Map<Missile,Long> destructedMissile;
	private Vector<Missile> destructedMissile = new Vector<>(); //all missiles we tried to destroy
	private long destructAfterLaunch;
	private DestructingMissile destructingMissile; //inner class

	public MissileDestructor(){
		this.id = "D" + (++idGenerator);
		this.destructingMissile = new DestructingMissile();
		destructingMissile.start();
		//this.destructedMissile = new HashMap<>();
	}
	
	public void add(Missile theMissile) {
		//destructedLauncher.add(theMissileLauncher);
		destructingMissile.add(theMissile);
/*		synchronized (destructingMissile) {
			if(destructedLauncher.size() == 1)
				destructingMissile.notify();
		}*/

	}

//	public boolean destructMissile(Missile theMissile){
//				//try {
//					//Thread.sleep(War.randomNumber(MIN_TIME, MAX_TIME));
//		destructAfterLaunch = (War.getCurrentTime());
//		if(!destructedMissile.containsKey(theMissile)) {
//			destructedMissile.put(theMissile, destructAfterLaunch);
//		}
//		return theMissile.destructMissile(destructAfterLaunch);
//		
//		//		} catch (InterruptedException e) {
//		//			e.printStackTrace();
//		//		}
//		//		return false;
//	}
	
	public synchronized boolean destructMissile(){
//		try {
			Missile theMissile = destructedMissile.remove(destructedMissile.size()-1);
			if(theMissile != null) {
				destructAfterLaunch = War.getCurrentTime();
				System.out.println(War.getCurrentTime()+"--> destruct after launch missile " + theMissile.getId()); 
				//theMissile.setDestructTime(destructAfterLaunch);
				return theMissile.destructMissile(destructAfterLaunch);
//				synchronized (theMissileLauncher) {
//					theMissileLauncher.notifyAll();
//				}
//				wait();
			}
			//Thread.sleep(randomNumber(MIN_TIME, MAX_TIME));
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vector<Missile> getDestructedMissile() {
		return this.destructedMissile;
	}

	public void setDestructedMissile(Vector<Missile> destructedMissiles) {
		this.destructedMissile = destructedMissiles;
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

//inner class
private class DestructingMissile extends Thread {
	//Vector<MissileLauncher> destructedLauncher;
	
	public DestructingMissile() {
		//this.destructedLauncher = new Vector<>();
	}
	
	@Override
	public void run() {
		while(true) {
			synchronized (this) {
				
			if(!destructedMissile.isEmpty()) {
				destructMissile();
			}
			
			//else {
//				synchronized (this) {
//					try {
//						System.out.println(War.getCurrentTime()+"--> destructingMissile waiting to run");
//						wait();
//						System.out.println(War.getCurrentTime()+"--> destructingMissile finished waiting to run");
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			}
		}
	}
	
	public void add(Missile m) {
		destructedMissile.add(m);
		synchronized (this) {
			if(destructedMissile.size() == 1)
				System.out.println(War.getCurrentTime()+"--> detructingMissile adding launcher");
				notify();
		}
	}
}
}




