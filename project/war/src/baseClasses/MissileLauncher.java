package baseClasses;

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
	private boolean isDestroyed;
	private long destructTime;


	public MissileLauncher() {
		this.id = "L" + (++idGenerator);
		missiles = new Vector<>();
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

	public boolean isDestroyed(){
		return this.isDestroyed;
	}

	@Override
	public void run() {
		while(!isDestroyed) {
			if(!missiles.isEmpty()) {
				try {
					if (isHidden){
						isHidden = false; // WE need to hide again launcher who has been hidden
						launch();
						isHidden = true;
					}
					else {
						launch();
					}
				} catch (InterruptedException e) {
					setDestroyed(true);
					e.printStackTrace();
				}

			}
			//if defined as hidden, sleep for x seconds being exposed- isHidden = false
			//try catch Interrupted exception - if destroyed isDestroyed = true
		}
	}


	public void launch() throws InterruptedException {
		Missile theMissile = missiles.remove(missiles.size()-1);
		if(theMissile != null){
			synchronized (this) {
				wait();
			}
		}
	}

	public void addMissile(Missile theMissile) {
		missiles.add(theMissile);
		System.out.println("In launcher- addMissile " + theMissile.getId());
		theMissile.start();
	}

	public boolean destructMissileLauncher(MissileLauncherDestructor destructor){
		if(isHidden){
			setDestroyed(false);
		}
		else {
			setDestroyed(true);
		}
		synchronized (destructor) {
			destructor.notifyAll();
		}
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
