package baseClasses;

import java.util.LinkedList;
import java.util.Queue;
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
	private Queue<Missile> waitingMissiles;
	private boolean isDestroyed;
	private long destructTime;


	public MissileLauncher() {
		this.id = "L" + (++idGenerator);
		missile = new Vector<>();
		waitingMissiles = new LinkedList<>();
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
				synchronized (this) {
					if(!waitingMissiles.isEmpty()) {
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

					//				else {
					//					//System.out.println("Launcher is waiting for missiles");
					//					synchronized (this) {
					//					//	wait();
					//					}
				}
			} catch (InterruptedException e) {
				setDestroyed(true);
				e.printStackTrace();
			}
			//if defined as hidden, sleep for x seconds being exposed- isHidden = false
			//try catch Interrupted exception - if destroyed isDestroyed = true
		}
	}


	public synchronized void launch() throws InterruptedException {
		Missile theMissile = waitingMissiles.poll();
		while(War.getCurrentTime() < theMissile.getLaunchTime()); //dummy while, waits until missile launch time 
		System.out.println(War.getCurrentTime()+"--> in launcher missile "+ theMissile.getMissileId()+ " chosen");
		//Thread.sleep(1000); //if 4 pressed too fast and there is no sleep it's not working well
		if(theMissile != null){
			System.out.println(War.getCurrentTime()+"--> launcher notifies missile " +theMissile.getMissileId());


			synchronized (theMissile) {
				theMissile.notifyAll();
			}
			System.out.println(War.getCurrentTime()+"--> launcher waits for missile to finish flying");
			wait();
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
	
	

	public synchronized void addWaitingMissile(Missile theMissile) {
		waitingMissiles.add(theMissile);
		System.out.println(War.getCurrentTime()+"--> In launcher addWaitingMissile " + theMissile.getMissileId() + " there are " + waitingMissiles.size() + " waiting missiles");
		//		synchronized (this) {
		//			if(waitingMissiles.size() == 1)
		//				notify();
		//		}
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


	public boolean isHidden(){
		return this.isHidden;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vector<Missile> getMissiles() {
		return missile;
	}

	public void setMissiles(Vector<Missile> missiles) {
		this.missile = missiles;
	}

	public long getDestructTime() {
		return destructTime;
	}

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
