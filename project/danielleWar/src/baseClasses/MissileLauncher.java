package baseClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import bussinesLogic.War;

public class MissileLauncher implements Runnable, Comparable<MissileLauncher> {
	private final static int ZERO = 0;
	private final static int TWO = 2;
	//private final static int MIN_TIME = 1000;
	//private final static int MAX_TIME = 5000;

	private static int idGenerator = 100;
	private String id;
	private boolean isHidden;
	private Vector<Missile> missile;
	//private ArrayList<Missile> missile;
//	private Queue<Missile> waitingMissiles;
	private List<Missile> waitingMissiles;
	private boolean isDestroyed;
	private long destructTime;

	private boolean isBusy = false;


	public MissileLauncher() {
		this.id = "L" + (++idGenerator);
		missile = new Vector<>();
		//missile = new ArrayList<>();
		waitingMissiles = new ArrayList<>();
		setDestroyed(false);
		setHidden();
	}

	public void setDestructTime(long destructTime){
		this.destructTime = destructTime;
	}

	public void setHidden() {
		int  n = War.randomNumber(ZERO, TWO);
		this.isHidden = ((n == 0)? true : false);
	}


	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;


	}

	public boolean getIsDestroyed(){
		return this.isDestroyed;
	}

	@Override
	public void run() {
		while(!isDestroyed) {
			try {
				if(!waitingMissiles.isEmpty()) {
					isBusy = true; 

					if (isHidden){
						isHidden = false; 
						launch();
						System.out.println(War.getCurrentTime()+"--> finished launch");
						isHidden = true;
					}
					else {
						launch();
					}

				}
				else {
					System.out.println("Launcher is waiting for missiles");
					synchronized (this) {
						isBusy = false;
						wait();
					}
				}

			} catch (InterruptedException e) {
				setDestroyed(true);
				e.printStackTrace();
			}
			//if defined as hidden, sleep for x seconds being exposed- isHidden = false
			//try catch Interrupted exception - if destroyed isDestroyed = true
		}
	}
	//Yogev and may launcher
	//	@Override
	//	public void run(){
	//
	//		for(Missile m : missile){
	//			while(War.getCurrentTime()< m.getLaunchTime());
	//			m.setLauncher(this);
	//			m.start();
	//
	//		}
	//	}


	public synchronized void launch() throws InterruptedException {
		Collections.sort(waitingMissiles, new SortByLaunchTime());
		Missile theMissile = waitingMissiles.remove(0);
		//System.out.println("missile launch time set: "+theMissile.getLaunchTime());
		while(War.getCurrentTime() < theMissile.getLaunchTime()); //dummy while, waits until missile launch time 
		System.out.println(War.getCurrentTime()+"--> in launcher missile "+ theMissile.getMissileId()+ " chosen");
		//Thread.sleep(1000); //if 4 pressed too fast and there is no sleep it's not working well
		if(theMissile != null){
			System.out.println(War.getCurrentTime()+"--> launcher notifies missile " +theMissile.getMissileId());

			synchronized (theMissile) {
				theMissile.notifyAll();
			}
			System.out.println(War.getCurrentTime()+"--> launcher waits for missile to finish flying");
			synchronized (this) {
				
			
			isBusy = true;
			wait();
			}
		}
	}

	public void addMissile(Missile theMissile) {
		missile.add(theMissile);
		System.out.println(War.getCurrentTime()+"--> In launcher " + this.id+"- addMissile " + theMissile.getMissileId());
		theMissile.start();
	}

	public void addMissileFromGson(MissileLauncher launcher){
		for (Missile m : missile){
			System.out.println(War.getCurrentTime()+"--> Missile "+m.getMissileId()+" started");
			m.setLauncher(launcher);
			m.start();
		}
	}



	public void addWaitingMissile(Missile theMissile) throws InterruptedException {
		waitingMissiles.add(theMissile);
		System.out.println(War.getCurrentTime()+"--> In launcher "+this.id+" addWaitingMissile " + theMissile.getMissileId() 
		+ " there are " + waitingMissiles.size() + " waiting missiles");


		if(waitingMissiles.size() == 1 && !isBusy) {// && not busy
			synchronized (this) {
				System.out.println("Launcher is busy = "+ isBusy);
				notify();
			}
		}
		System.out.println("******* After synchrnize *********");

	}

	public boolean destructSelf(MissileLauncherDestructor destructor){
		if(isHidden){
			setDestroyed(false);
		}
		else {
			setDestroyed(true);
		}
		System.out.println(War.getCurrentTime()+"--> Is destroyed: "+getIsDestroyed());
		return isDestroyed;
	}



	public int howManyWaiting(){
		return waitingMissiles.size();
	}

	public boolean isHidden(){
		return this.isHidden;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vector<Missile> getMissile() {
		return missile;
	}

	public void setMissile(Vector<Missile> missile) {
		this.missile = missile;
	}



	public long getDestructTime() {
		return destructTime;
	}

	public boolean isBusy(){
		return this.isBusy;
	}

	public void setBusy(boolean isBusy){
		this.isBusy = isBusy;
	}



	//	public ArrayList<Missile> getMissile() {
	//		return missile;
	//	}
	//
	//	public void setMissile(ArrayList<Missile> missile) {
	//		this.missile = missile;
	//	}

	@Override
	public String toString(){
		String str="";
		str += " Launcher id:"+this.id + " isHidden:"+this.isHidden;
		for(Missile m : missile){
			str += m.toString();
		}
		return str;
	}

	@Override
	public int compareTo(MissileLauncher o) {
		return (int)(this.destructTime - o.destructTime);
	}

}
