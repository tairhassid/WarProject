package baseClasses;

import java.util.Vector;

import bussinesLogic.War;


public class MissileDestructor {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	private static int idGenerator = 200;
	private String id;
	private Vector<Missile> destructedMissile = new Vector<>(); //all missiles we tried to destroy
	private long destructAfterLaunch;
	private DestructingMissile destructingMissile; //inner class

	public MissileDestructor(){
		this.id = "D" + (++idGenerator);
		this.destructingMissile = new DestructingMissile();
		
	}

	@Override
	public String toString(){
		String str = "";
		synchronized(this) {
		for(Missile m : destructedMissile){
			str +=m.toString();
		}
		}
		return str;
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
		Missile theMissile = destructedMissile.remove(0/*destructedMissile.size()-1*/);
		if(theMissile != null) {
			while(theMissile.getDestructAfterLaunch()+theMissile.getLaunchTime() > War.getCurrentTime());
			System.out.println(War.getCurrentTime()+"--> trying to destruct missile " + theMissile.getMissileId());
//			System.out.println("missile "+theMissile.getMissileId()+" destruct after launch = "+theMissile.getDestructAfterLaunch());
//			System.out.println("missile "+theMissile.getMissileId()+" launch time = "+ theMissile.getLaunchTime());
//			System.out.println("missile "+theMissile.getMissileId()+" fly time = "+ theMissile.getFlyTime());
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
	

	public void initMissileDestructed(Missile theMissile) {
		for(int i=0; i< destructedMissile.size(); i++){
			if(destructedMissile.get(i).getMissileId().equals(theMissile.getMissileId())){
				theMissile.setDestructAfterLaunch(destructedMissile.get(i).getDestructAfterLaunch());
				destructedMissile.remove(i);
				destructedMissile.add(i, theMissile);
				//setDestructedMissileAsMissile(destructedMissile.get(i), theMissile);
				System.out.println("in initMissileDestructor missile:"+destructedMissile.get(i).toString());
				break;
			}
		}
		
	}
	
	public void setDestructedMissileAsMissile(Missile theMissile, Missile obj){//not used
		theMissile.setDestination(obj.getDestination());
		theMissile.setLaunchTime(obj.getLaunchTime());
		theMissile.setFlyTime(obj.getFlyTime());
		theMissile.setDamage(obj.getDamage());
		theMissile.setLauncher(obj.getLauncher());
		//theMissile.setDestructAfterLaunch(obj.getDestructAfterLaunch());
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




