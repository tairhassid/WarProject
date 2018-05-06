package war;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

public class MissileLaunchers implements Runnable {
	private final static int ZERO = 0;
	private final static int ONE =1;
	private final static int MIN_TIME = 1000;
	private final static int MAX_TIME = 5000;

	private static int idGenerator = 100;
	private String id;
	private boolean isHidden;
	//private Queue<Missile> missiles = new LinkedList<>();
	private Vector<Missile> missiles;
	private boolean isDestroyed;


	public MissileLaunchers() {
		this.id = "L" + (++idGenerator);
		missiles = new Vector<>();
		setDestroyed(false);
		setIsHidden();
	}

	public void setIsHidden() {
		int  n = randomNumber(ZERO,ONE);
		this.isHidden = ((n == 0)? true : false);
	}


	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	@Override
	public void run() {
		while(!isDestroyed) {
			if (isHidden){
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
		Missile theMissile = missiles.remove(missiles.capacity()-1);
		if(theMissile != null){
			theMissile.notifyAll();
			synchronized (this) {
				wait();
			}
		}

	}

	public void addMissile(Missile theMissile) {
		missiles.add(theMissile);
	}

	public boolean destructMissileLauncher(){
		if(isHidden){
			setDestroyed(false);
			return false;
		}
		else {
			int random = randomNumber(ZERO, ONE); // succeeded or not
			setDestroyed((random == 0)? true : false);
			return true;
		}
	}

	public int randomNumber(int from, int to){
		Random rand = new Random();
		int number = rand.nextInt(to) + from;

		return number;
	}



}
