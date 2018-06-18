package baseClasses;

import java.util.Random;
import java.util.Vector;

import com.google.gson.annotations.SerializedName;

import bussinesLogic.War;
import bussinesLogic.WarSummary;

public class MissileLauncherDestructor {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	public enum DestructorType {
		@SerializedName("plane")
		Plane, 
		@SerializedName("ship")
		Ship};
	public DestructorType type;
	private Vector<MissileLauncher> destructedLauncher = new Vector<>();

	private boolean isBusy = false;
	private long destructTime;
	private DestructingMissile destructingMissile;


	public MissileLauncherDestructor(){
		this.destructingMissile = new DestructingMissile();
	}

	public MissileLauncherDestructor(DestructorType type) {
		this.type= type;
		this.destructingMissile = new DestructingMissile();
	}


	public boolean destructMissileLauncher(){
		MissileLauncher theMissileLauncher = destructedLauncher.remove(0);
		System.out.println("******MissileLauncherDestructor chose missile launcher ");
		if(theMissileLauncher != null) {
			while(theMissileLauncher.getDestructTime() > War.getCurrentTime());
			destructTime = War.getCurrentTime();
			System.out.println(destructTime+"--> trying to destruct missile launcher " + theMissileLauncher.getId());
			if(theMissileLauncher.getDestructTime() == 0)
				theMissileLauncher.setDestructTime(destructTime);
			

			return theMissileLauncher.destructSelf(this);
		}
		isBusy = false;
		return false;
	}

	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}

	public void add(MissileLauncher theMissileLauncher) {
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		destructedLauncher.add(theMissileLauncher);
		isBusy = true;
		destructingMissile.notifyDestructor();
	}

	public void addFromGson(){
		System.out.println("~~~~~~destructedLauncher " + this.getType() + " size " + destructedLauncher.size());
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		isBusy = true;
		//		for (MissileLauncher m : destructedLauncher){
		//			destructingMissile.notifyDestructor();
		//		}
	}


	public void initMissileLauncherDestructed(MissileLauncher theMissileLauncher) {
		for(int i=0 ; i < destructedLauncher.size() ; i++) {
			if(destructedLauncher.get(i).getId().equals(theMissileLauncher.getId())) {
				theMissileLauncher.setDestructTime(destructedLauncher.get(i).getDestructTime());
				destructedLauncher.remove(i);
				destructedLauncher.add(i, theMissileLauncher);
				break;
			}
		}
	}

	public Vector<MissileLauncher> getDestructedLauncher() {
		return destructedLauncher;
	}

	public void setDestructedLauncher(Vector<MissileLauncher> destructedLauncher) {
		this.destructedLauncher = destructedLauncher;
	}

	public DestructorType getType() {
		return type;
	}

	public void setType(DestructorType type) {
		this.type = type;
	}
	
	@Override
	public String toString(){
		String str = "";
		str += "type: "+ this.getType();
		for(MissileLauncher ml : destructedLauncher){
			str +=ml.toString();
		}
		return str;
	}



	//inner class
	private class DestructingMissile extends Thread {

		public DestructingMissile() {
		}

		@Override
		public void run() {
			while(true) {
				if(!destructedLauncher.isEmpty()) {
					isBusy = true;
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
				if(destructedLauncher.size() == 1 && !isBusy)
					notify();
			}
		}
	}
}
