package baseClasses;

import java.util.Random;
import java.util.Vector;

import BL.War;

public class MissileLauncherDestructor {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	public enum type {Plane, Ship};
	public type destructorType;
	//private Map<MissileLauncher,Long> destructedLauncher;
	private Vector<MissileLauncher> destructedLauncher = new Vector<>();
	private long destructAfterLaunch;
	private DestructingMissile destructingMissile;


	public MissileLauncherDestructor(){}

	public MissileLauncherDestructor(type destructorType) {
		this.destructorType= destructorType;
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

	public boolean destructMissileLauncher(){
//		try {
			MissileLauncher theMissileLauncher = destructedLauncher.remove(destructedLauncher.size()-1);
			if(theMissileLauncher != null) {
				destructAfterLaunch = War.getCurrentTime();
				System.out.println("destruct after launch " + theMissileLauncher.getId()); 
				theMissileLauncher.setDestructTime(destructAfterLaunch);
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
		this.destructingMissile = new DestructingMissile();
		destructingMissile.start();
		destructingMissile.add(theMissileLauncher);
/*		synchronized (destructingMissile) {
			if(destructedLauncher.size() == 1)
				destructingMissile.notify();
		}*/

	}
	
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
							System.out.println("destructingMissile waiting to run");
							wait();
							System.out.println("destructingMissile finished waiting to run");
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
					System.out.println("detructingMissile adding launcher");
					notify();
			}
		}
	}
}
