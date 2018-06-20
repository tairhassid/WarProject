package baseClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.google.gson.annotations.SerializedName;

import bussinesLogic.LoggerManager;
import bussinesLogic.ObjectFilter;
import bussinesLogic.War;
import bussinesLogic.WarFormatter;

public class MissileLauncher implements Runnable, Comparable<MissileLauncher> {
	//	private final static int ZERO = 0;
	//	private final static int TWO = 2;


	private static int idGenerator = 100;
	@SerializedName("id")
	private String id;
	private boolean isHidden;
	private boolean isCurrentlyHidden;
	private Vector<Missile> missile = new Vector<>();
	private List<Missile> waitingMissiles = new ArrayList<Missile>();
	private boolean isDestroyed;
	private long destructTime;

	private boolean isBusy;
	private FileHandler handler;
	//private Thread launcherThread;

	//Gson constructor
	public MissileLauncher(){
		++idGenerator;
	//	setHandler();
		//System.err.println("Launcher id: "+ id);
		this.isCurrentlyHidden = this.isHidden;
	}

	public MissileLauncher(int isHidden) {
		this.id = "L" + (++idGenerator);
		setHidden(isHidden);
		setHandler();
	}

	public void setDestructTime(long destructTime){
		this.destructTime = destructTime;
	}

	public void setHidden(int isHidden) {
		if(isHidden == 1){
			this.isHidden = true;
			this.isCurrentlyHidden = true;
		}
		else
			this.isHidden = false;
		}


	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;


	}
	

	public boolean getIsDestroyed(){
		return this.isDestroyed;
	}

	@Override
	public void run() {
	
		try {
			while(!isDestroyed) {
				if(!waitingMissiles.isEmpty()) {
					isBusy = true; 
					launch();
					System.out.println(War.getCurrentTime()+"--> finished launch");

				}
				else {
					System.out.println(War.getCurrentTime()+"--> Launcher "+this.getId()+" is waiting for missiles");
					synchronized (this) {
						isBusy = false;
						wait();
					}
				}
			}

			System.out.println(War.getCurrentTime()+"--> Launcher "+ this.getId()+ " is destroyed!!!");
		} catch (InterruptedException e) {
			setDestroyed(true);
			e.printStackTrace();
		}


	}



	public synchronized void launch() throws InterruptedException {
		Collections.sort(waitingMissiles);
		Missile theMissile = waitingMissiles.remove(0);
		if(theMissile != null){
			while(War.getCurrentTime() < theMissile.getLaunchTime()); //dummy while, waits until missile launch time 
			isCurrentlyHidden = false;
			System.out.println(War.getCurrentTime()+"--> in launcher missile "+ theMissile.getMissileId()+ " chosen");
			System.out.println(War.getCurrentTime()+"--> launcher notifies missile " +theMissile.getMissileId());

			synchronized (theMissile) {
				theMissile.notifyAll();
			}
			System.out.println(War.getCurrentTime()+"--> launcher waits for missile to finish flying");
			isBusy = true;

			synchronized (this) {
				wait();				
			}
			if(isHidden)
				isCurrentlyHidden = true;
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
			synchronized (this){ 
				notify();
			}
		}
	}


	public boolean destructSelf(MissileLauncherDestructor destructor){ 
		if(isCurrentlyHidden){
			setDestroyed(false);
		}
		else {
			setDestroyed(true);
		}
		System.out.println(War.getCurrentTime()+"--> Launcher "+this.getId()+" is destroyed: "+ isDestroyed);
		destructor.logMissile(logLauncher());
		return isDestroyed;
	}
	
	public String logLauncher(){
		StringBuffer buf = new StringBuffer();
		
		buf.append("Target Launcher: "+ id);
		
		if(isDestroyed){
			buf.append("\nLauncher destroyed!\n");
		}
		else{
			buf.append("\nDestruct failed\n");
		}
		return buf.toString();
	}



	public int howManyWaiting(){
		return waitingMissiles.size();
	}

	public boolean getIsHidden(){
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

	public void setHandler(){
		try {
			String log = "Launcher id: "+ this.id+ "\n";
			handler = new FileHandler(War.LOG_PATH + id + ".txt", true);
			handler.setFormatter(new WarFormatter());
			handler.setFilter(new LauncherFilter(this));
			LoggerManager.getLogger().setUseParentHandlers(false);
			LoggerManager.addHandler(handler);
			
			
			LoggerManager.getLogger().log(Level.INFO, log, this);
			
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

	}

	public void logMissile(StringBuffer log){
		//LoggerManager.addHandler(handler);
		LoggerManager.getLogger().log(Level.INFO, log.toString(), this);
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
    class LauncherFilter implements Filter {

        private MissileLauncher launcher;

        public LauncherFilter(MissileLauncher launcher) {
            this.launcher = launcher;
        }

        @Override
        public boolean isLoggable(LogRecord rec) {
            if (rec.getSourceClassName().equalsIgnoreCase(MissileLauncher.class.getName()) &&
            		rec.getParameters()[0] == launcher){
            	return true;
            }
            else{
                return false;
            }
        }

    }


