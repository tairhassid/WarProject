package baseClasses;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.google.gson.annotations.SerializedName;

import bussinesLogic.LoggerManager;
import bussinesLogic.War;
import bussinesLogic.WarFormatter;
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
	private FileHandler handler;
	private Vector<MissileLauncher> realMissileLaunchers = new Vector<>();


	public MissileLauncherDestructor(){
		this.destructingMissile = new DestructingMissile();
		//setHandler();
	}

	public MissileLauncherDestructor(DestructorType type) {
		this.type= type;
		this.destructingMissile = new DestructingMissile();
		setHandler();
	}
	
	public void setHandler(){
		try {
			String log = "Launcher Destructor type: "+ this.type + "\n";
			handler = new FileHandler(War.LOG_PATH + this.type + ".txt", true);
			handler.setFormatter(new WarFormatter());
			handler.setFilter(new LauncherDestructorFilter(this));
			LoggerManager.getLogger().setUseParentHandlers(false);
			LoggerManager.addHandler(handler);
			
			
			LoggerManager.getLogger().log(Level.INFO, log, this);
			
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void logMissile(String log){
		LoggerManager.getLogger().log(Level.INFO, log, this);
	}


	public synchronized boolean destructMissileLauncher(){
		MissileLauncher theMissileLauncher = destructedLauncher.remove(0);
		System.out.println("******MissileLauncherDestructor chose missile launcher ");
		
		if(theMissileLauncher != null) {
			System.out.println("in destructMissileLaumcher " + theMissileLauncher.getId());
			
			while(theMissileLauncher.getDestructTime() > War.getCurrentTime());
			destructTime = War.getCurrentTime();
			System.out.println(destructTime+"--> trying to destruct missile launcher " + theMissileLauncher.getLauncherId());
			
			if(theMissileLauncher.getDestructTime() == 0)
				theMissileLauncher.setDestructTime(destructTime);
			
			System.out.println("@@@@@@@@@@@@@@ is gsonGame " + War.gsonGame);
			if(War.gsonGame) {
				for(MissileLauncher ml : realMissileLaunchers) {
					if(ml.getLauncherId().equals(theMissileLauncher.getLauncherId())) {
						ml.setDestructTime(theMissileLauncher.getDestructTime());
//						destructedLauncher.remove(theMissileLauncher);
//						
//						destructedLauncher.add(ml);
						return ml.destructSelf(this);
					}
						
				}
			}
			else {
				return theMissileLauncher.destructSelf(this);
			}
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
	//setHandler();
		realMissileLaunchers.add(theMissileLauncher);
//		System.out.println("@@@@@@@@@@@@ destructedLauncher.size(): " + destructedLauncher.size() + " thread ID " + theMissileLauncher.getId());
//		for(int i=0 ; i < destructedLauncher.size() ; i++) {
//			if(destructedLauncher.get(i).getLauncherId().equals(theMissileLauncher.getLauncherId())) {
//				theMissileLauncher.setDestructTime(destructedLauncher.get(i).getDestructTime());
//				destructedLauncher.remove(i);
//				destructedLauncher.add(i, theMissileLauncher);
//				System.out.println("@@@@@@@@@@@@@@@" + destructedLauncher.get(i).getId());
//			}
//		}
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
							System.out.println(War.getCurrentTime()+"--> destructingLauncher waiting to run");
							wait();
							System.out.println(War.getCurrentTime()+"--> destructingLauncher finished waiting to run");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		public void notifyDestructor() {
			synchronized (this) {
				if(destructedLauncher.size() == 1 /*&& !isBusy*/)
					notify();
			}
		}
	}
}

class LauncherDestructorFilter implements Filter {

    private MissileLauncherDestructor destructor;

    public LauncherDestructorFilter(MissileLauncherDestructor destructor) {
        this.destructor = destructor;
    }

    @Override
    public boolean isLoggable(LogRecord rec) {
        if (rec.getSourceClassName().equalsIgnoreCase(MissileLauncherDestructor.class.getName()) &&
        		rec.getParameters()[0] == destructor){
        	//System.out.println(rec.getParameters()[0]);
        	return true;
        }
        else{
        	//System.out.println("in LaunchFilter isLoggable = false");
            return false;
        }
    }

}
