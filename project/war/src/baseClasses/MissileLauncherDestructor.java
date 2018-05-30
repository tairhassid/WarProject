package baseClasses;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import BL.War;

public class MissileLauncherDestructor implements Runnable {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	public enum type {Plane, Ship};
	public type destructorType;
	//private Map<MissileLauncher,Long> destructedLauncher;
	private Vector<MissileLauncher> destructedLauncher = new Vector<>();
	private long destructAfterLaunch;


	public MissileLauncherDestructor(){}

	public MissileLauncherDestructor(type destructorType) {
		this.destructorType= destructorType;
	}

	@Override
	public void run() {
		while(true){
			if(!destructedLauncher.isEmpty()){
				//destructMissileLauncher();
			}
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean destructMissileLauncher(MissileLauncher theMissileLauncher){
		try {
			destructAfterLaunch = War.getCurrentTime();
			destructedLauncher.add(theMissileLauncher);
			theMissileLauncher.setDestructTime(destructAfterLaunch);
			//destructedLauncher.put(theMissileLauncher, destructAfterLaunch);
			Thread.sleep(randomNumber(MIN_TIME, MAX_TIME));
			return theMissileLauncher.destructMissileLauncher(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}


	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}
}
