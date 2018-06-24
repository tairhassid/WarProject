package baseClasses;

import java.util.Random;
import java.util.Vector;

import bussinesLogic.War;

public class MissileLauncherDestructor {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	public enum DestructorType {Plane, Ship};
	public DestructorType type;
	//private Map<MissileLauncher,Long> destructedLauncher;
	private Vector<MissileLauncher> destructedLanucher = new Vector<>();


	private long destructTime;
	private DestructingMissile destructingMissile;


	public MissileLauncherDestructor(){
		this.destructingMissile = new DestructingMissile();
	}

	public MissileLauncherDestructor(DestructorType type) {
		this.type= type;
		this.destructingMissile = new DestructingMissile();
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
			MissileLauncher theMissileLauncher = destructedLanucher.remove(0);
			System.out.println("******MissileLauncherDestructor chose missile launcher ");
			if(theMissileLauncher != null) {
				while(theMissileLauncher.getDestructTime() > War.getCurrentTime());
				destructTime = War.getCurrentTime();
				System.out.println(destructTime+"--> trying to destruct missile launcher " + theMissileLauncher.getId());
				if(theMissileLauncher.getDestructTime() == 0)
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
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		destructedLanucher.add(theMissileLauncher);
		destructingMissile.notifyDestructor();
/*		synchronized (destructingMissile) {
			if(destructedLauncher.size() == 1)
				destructingMissile.notify();
		}*/

	}
	
	public void addFromGson(){
		System.out.println("~~~~~~destructedLauncher " + this.getType() + " size " + destructedLanucher.size());
		if(!destructingMissile.isAlive())
			destructingMissile.start();
//		for (MissileLauncher m : destructedLauncher){
//			destructingMissile.notifyDestructor();
//		}
	}
	

	public void initMissileLauncherDestructed(MissileLauncher theMissileLauncher) {
		for(int i=0 ; i < destructedLanucher.size() ; i++) {
			if(destructedLanucher.get(i).getId().equals(theMissileLauncher.getId())) {
				theMissileLauncher.setDestructTime(destructedLanucher.get(i).getDestructTime());
				destructedLanucher.remove(i);
				destructedLanucher.add(i, theMissileLauncher);
				System.out.println("in initMissileLauncherDestructor missileLauncher: "+destructedLanucher.get(i).toString());
				break;
			}
		}
	}
	
	public Vector<MissileLauncher> getDestructedLauncher() {
		return destructedLanucher;
	}

	public void setDestructedLauncher(Vector<MissileLauncher> destructedLauncher) {
		this.destructedLanucher = destructedLauncher;
	}
	
	public DestructorType getType() {
		return type;
	}

	public void setType(DestructorType type) {
		this.type = type;
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
				if(!destructedLanucher.isEmpty()) {
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
		
		public void notifyDestructor() {
			synchronized (this) {
				if(destructedLanucher.size() == 1)
					System.out.println(War.getCurrentTime()+"--> detructingMissile adding launcher");
					notify();
			}
		}
	}
}
