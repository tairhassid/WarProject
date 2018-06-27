package baseClasses;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.google.gson.annotations.SerializedName;

import bussinesLogic.LoggerManager;
import bussinesLogic.War;
import bussinesLogic.WarFormatter;

public class MissileLauncherDestructor {

	private static boolean warIsOn;
	
	public enum DestructorType {
		@SerializedName("plane")
		Plane, 
		@SerializedName("ship")
		Ship};
	public DestructorType type;
	
	private Vector<MissileLauncher> destructedLauncher = new Vector<>();
	private long destructTime;
	private DestructingMissile destructingMissile;
	private FileHandler handler;
	private Vector<MissileLauncher> realMissileLaunchers = new Vector<>();

	/*gson constructor*/
	public MissileLauncherDestructor(){
		warIsOn = true;
		this.destructingMissile = new DestructingMissile();
		//setHandler();
	}

	public MissileLauncherDestructor(DestructorType type) {
		warIsOn = true;
		this.type= type;
		this.destructingMissile = new DestructingMissile();
		setHandler();
	}
	
	public synchronized void destructMissileLauncher() {
		MissileLauncher theMissileLauncher = destructedLauncher.remove(0);
		
		if(theMissileLauncher != null) {
			while(theMissileLauncher.getDestructTime() > War.getCurrentTime()); //dummy wait
			destructTime = War.getCurrentTime();
			System.out.println(destructTime+"--> trying to destruct missile launcher " + theMissileLauncher.getLauncherId());
			
			if(theMissileLauncher.getDestructTime() == 0)
				theMissileLauncher.setDestructTime(destructTime);
			
			if(War.nowGsonGame) {
				for(MissileLauncher ml : realMissileLaunchers) {
					if(ml.getLauncherId().equals(theMissileLauncher.getLauncherId())) {
						ml.setDestructTime(theMissileLauncher.getDestructTime());
						ml.destructSelf(this);
					}	
				}
			}
			else {
				theMissileLauncher.destructSelf(this);
			}
		}
	}

	public void add(MissileLauncher theMissileLauncher) {
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		destructedLauncher.add(theMissileLauncher);;
		destructingMissile.notifyDestructor();
	}

	public void addFromGson(){
		System.out.println("~~~~~~destructedLauncher " + this.getType() + " size " + destructedLauncher.size());
		if(!destructingMissile.isAlive())
			destructingMissile.start();
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
	
	public void endWar() {
		for(MissileLauncher ml : destructedLauncher) {
			synchronized (ml) {
				ml.setDestroyed(true);
				ml.notify();
			}
		}
		warIsOn = false;
		synchronized (destructingMissile) {
			destructingMissile.notify();
		}
	}
	
	//for logger
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
	
	//setters and getters
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
			while(warIsOn) {
				if(!destructedLauncher.isEmpty()) {
					destructMissileLauncher();
					
				}
				else {
					synchronized (this) {
						try {
//							System.out.println(War.getCurrentTime()+"--> destructingLauncher waiting to run");
							wait();
//							System.out.println(War.getCurrentTime()+"--> destructingLauncher finished waiting to run");
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
