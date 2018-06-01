package baseClasses;

import java.util.LinkedList;
import java.util.Queue;
//import java.util.Random;
import java.util.Vector;

import BL.War;

public class MissileLauncher implements Runnable {
	private final static int ZERO = 0;
	private final static int TWO = 2;
	private final static int MIN_TIME = 1000;
	private final static int MAX_TIME = 5000;

	private static int idGenerator = 100;
	private String id;
	private boolean isHidden;
	private Vector<Missile> missiles;
	private Queue<Missile> waitingMissiles;
	private boolean isDestroyed;
	private long destructTime;


	public MissileLauncher() {
		this.id = "L" + (++idGenerator);
		missiles = new Vector<>();
		waitingMissiles = new LinkedList<>();
		setDestroyed(false);
		setIsHidden();
	}

	public void setDestructTime(long destructTime){
		this.destructTime = destructTime;
	}

	public void setIsHidden() {
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
					if (isHidden){
						isHidden = false; // WE need to hide again launcher who has been hidden
						launch();
						isHidden = true;
					}
					else {
						launch();
					}
				}
				else {
					synchronized (this) {
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


	public synchronized void launch() throws InterruptedException {
		Missile theMissile = waitingMissiles.poll();
		System.out.println("in launcher missile "+ theMissile.getId() + "chosen");
		//Thread.sleep(1000); //if 4 pressed too fast and there is no sleep it's not working well
		if(theMissile != null){
			System.out.println("launcher notifies missile " +theMissile.getId());
			synchronized (theMissile) {
				theMissile.notifyAll();
			}
			System.out.println("launcher waits for missile to finish flying");
			wait();
		}
	}

	public void addMissile(Missile theMissile) {
		missiles.add(theMissile);
		System.out.println("In launcher- addMissile " + theMissile.getId());
		theMissile.start();
	}

	public void addWaitingMissile(Missile theMissile) {
		waitingMissiles.add(theMissile);
		System.out.println("In launcher addWaitingMissile " + theMissile.getId() + " there are " + waitingMissiles.size() + " waiting missiles");
		synchronized (this) {
			if(waitingMissiles.size() == 1)
				notify();
		}
	}

	public boolean destructSelf(MissileLauncherDestructor destructor){
		if(isHidden){
			setDestroyed(false);
		}
		else {
			setDestroyed(true);
		}
		System.out.println("Is destroyed: "+getIsDestroyed());
		return isDestroyed;
	}

	//	public int randomNumber(int from, int to){
	//		Random rand = new Random();
	//		int number = rand.nextInt(to) + from;
	//
	//		return number;
	//	}

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
		return missiles;
	}

	public void setMissiles(Vector<Missile> missiles) {
		this.missiles = missiles;
	}

	public long getDestructTime() {
		return destructTime;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}



}
