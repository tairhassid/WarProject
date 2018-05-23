package war;

import java.util.Random;
import java.util.Vector;

public class MissileLaunchers implements Runnable {
	private final static int ZERO = 0;
	private final static int TWO = 2;
	private final static int MIN_TIME = 1000;
	private final static int MAX_TIME = 5000;

	private static int idGenerator = 100;
	private String id;
	private boolean isHidden;
	private Vector<Missile> missiles;
	private boolean isDestroyed;


	public MissileLaunchers() {
		this.id = "L" + (++idGenerator);
		missiles = new Vector<>();
		setDestroyed(false);
		setIsHidden();
		System.out.println("create");
	}

	public void setIsHidden() {
		int  n = randomNumber(ZERO,TWO);
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
			if(!missiles.isEmpty())
				if (isHidden) {
					isHidden = false;
					try {
						launch();
						//Thread.sleep(randomNumber(MIN_TIME, MAX_TIME));
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
			synchronized (theMissile) {
				theMissile.notifyAll();
			}
			synchronized (this) {
				wait();
			}
		}

	}

	public void addMissile(Missile theMissile) {
		missiles.add(theMissile);
		Thread theMissileThread = new Thread(theMissile);
		theMissileThread.start();
	}

	public boolean destructMissileLauncher(){
		if(isHidden){
			setDestroyed(false);
			return false;
		}
		else {
			int random = randomNumber(ZERO, TWO); // succeeded or not
			setDestroyed((random == 0)? true : false);
		}
		return true;
	}

	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}



}
