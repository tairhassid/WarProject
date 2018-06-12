package baseClasses;

import java.util.Random;
import java.util.Vector;

import BL.War;

public class MissileLauncherDestructor {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	public enum DestructorType {Plane, Ship};
	public DestructorType type;
	//private Map<MissileLauncher,Long> destructedLauncher;
	private Vector<MissileLauncher> destructedLauncher = new Vector<>();


	private long destructTime;
	private DestructingMissile destructingMissile;


	public MissileLauncherDestructor(){}

	public MissileLauncherDestructor(DestructorType type) {
		this.type= type;
		this.destructingMissile = new DestructingMissile();
		destructingMissile.start();
	}

/*	@Override
	public void run() {
		while(true){
			if(!destructedLauncher.isEmpty()){
				destructMissileLauncher();
			}
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}*/

	public DestructorType getType() {
		return type;
	}

	public void setType(DestructorType type) {
		this.type = type;
	}

	public boolean destructMissileLauncher(){
//		try {
			MissileLauncher theMissileLauncher = destructedLauncher.remove(destructedLauncher.size()-1);
			if(theMissileLauncher != null) {
				destructTime = War.getCurrentTime();
				System.out.println(destructTime+"--> destruct after launch " + theMissileLauncher.getId()); 
				theMissileLauncher.setDestructTime(destructTime);
				return theMissileLauncher.destructSelf(this);
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

	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}

	public void add(MissileLauncher theMissileLauncher) {
		//destructedLauncher.add(theMissileLauncher);
		
		destructingMissile.add(theMissileLauncher);
/*		synchronized (destructingMissile) {
			if(destructedLauncher.size() == 1)
				destructingMissile.notify();
		}*/

	}
	
	public Vector<MissileLauncher> getDestructedLauncher() {
		return destructedLauncher;
	}

	public void setDestructedLauncher(Vector<MissileLauncher> destructedLauncher) {
		this.destructedLauncher = destructedLauncher;
	}
	
	
	
	//inner class
	private class DestructingMissile extends Thread {
		//Vector<MissileLauncher> destructedLauncher;
		
		public DestructingMissile() {
			//this.destructedLauncher = new Vector<>();
		}
		
		@Override
		public void run() {
			while(true) {
				if(!destructedLauncher.isEmpty()) {
					destructMissileLauncher();
				}
				else {
					synchronized (this) {
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
		
		public void add(MissileLauncher ml) {
			destructedLauncher.add(ml);
			synchronized (this) {
				if(destructedLauncher.size() == 1)
					System.out.println(War.getCurrentTime()+"--> detructingMissile adding launcher");
					notify();
			}
		}
	}
}
