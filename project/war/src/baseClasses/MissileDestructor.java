package baseClasses;

import java.util.Vector;

import bussinesLogic.War;


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
		//this.destructedMissile = new HashMap<>();
	}

	public void add(Missile theMissile) {
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		//destructedLauncher.add(theMissileLauncher);
		destructedMissile.add(theMissile);
		destructingMissile.notifyDestructor(theMissile);
		/*		synchronized (destructingMissile) {
			if(destructedLauncher.size() == 1)
				destructingMissile.notify();
		}*/

	}

	public void addFromGson(){
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		for (Missile m : destructedMissile){
			destructingMissile.notifyDestructor(m);

		}

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
		Missile theMissile = destructedMissile.remove(0/*destructedMissile.size()-1*/);
		if(theMissile != null) {
			while(theMissile.getDestructAfterLaunch()+theMissile.getLaunchTime() > War.getCurrentTime());
			//destructAfterLaunch = War.getCurrentTime();
			System.out.println(War.getCurrentTime()+"--> trying to destruct missile " + theMissile.getMissileId()); 
			//theMissile.setDestructTime(destructAfterLaunch);
			if(theMissile.getDestructAfterLaunch() == 0)
				theMissile.setDestructAfterLaunch(War.getCurrentTime());

			
//				try {
//					wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			
			return theMissile.destructMissile();
		}
		//Thread.sleep(randomNumber(MIN_TIME, MAX_TIME));
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		System.out.println(War.getCurrentTime()+"--> There are no flying missiles!");
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

	//	public long getDestructAfterLaunch() {
	//		return destructAfterLaunch;
	//	}
	//
	//	public void setDestructAfterLaunch(long destructAfterLaunch) {
	//		this.destructAfterLaunch = destructAfterLaunch;
	//	}


	//inner class
	private class DestructingMissile extends Thread {

		public DestructingMissile() {
		}

		@Override
		public void run() {
			while(true) {
				synchronized (this) {
					if(!destructedMissile.isEmpty()) {
						destructMissile();
					}

					else {

						try {
							System.out.println(War.getCurrentTime()+"--> destructingMissile waiting to run");
							wait();
							System.out.println(War.getCurrentTime()+"--> destructingMissile finished waiting to run");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		public void notifyDestructor(Missile m) {
			synchronized (this) {
				if(destructedMissile.size() == 1){
					//System.out.println(War.getCurrentTime()+"--> detructingMissile adding launcher");
					notify();
				}
			}
		}
	}
}




