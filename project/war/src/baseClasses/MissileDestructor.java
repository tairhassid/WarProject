package baseClasses;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import bussinesLogic.LoggerManager;
import bussinesLogic.War;
import bussinesLogic.WarFormatter;


public class MissileDestructor {
	public final static int MIN_TIME = 1000;
	public final static int MAX_TIME = 5000;
	private static int idGenerator = 200;
	private String id;
	private Vector<Missile> destructedMissile = new Vector<>(); //all missiles we tried to destroy
	private DestructingMissile destructingMissile; //inner class
	private FileHandler handler;
	
	private boolean isBusy = false;

	public MissileDestructor(){
		this.id = "D" + (++idGenerator);
		this.destructingMissile = new DestructingMissile();
		setHandler();
	}

	@Override
	public String toString(){
		String str = "";
		for(Missile m : destructedMissile){
			str +=m.toString();
		}
		return str;
	}

	public void add(Missile theMissile) {
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		destructedMissile.add(theMissile);
		isBusy = true;
		destructingMissile.notifyDestructor(theMissile);

	}

	public void addFromGson(){
		if(!destructingMissile.isAlive())
			destructingMissile.start();
		isBusy = true;
//		for (Missile m : destructedMissile){
//			destructingMissile.notifyDestructor(m);
//		}

	}

	public synchronized void destructMissile(){
		Missile theMissile = destructedMissile.remove(0);
		if(theMissile != null ) {
			while(theMissile.getDestructAfterLaunch()+theMissile.getLaunchTime() > War.getCurrentTime());
			if(theMissile.getDestructAfterLaunch() == 0)
				theMissile.setDestructAfterLaunch(War.getCurrentTime());
			if(theMissile.getDidLaunched()){
				System.out.println(War.getCurrentTime()+"--> trying to destruct missile " + theMissile.getMissileId());
				theMissile.destructMissile(this);
				
				//logMissile(theMissile);
			}
			//return theMissile;
		}
		isBusy = false;
		System.out.println(War.getCurrentTime()+"--> There are no flying missiles in launcher ");
		//return null;
	}

	public void setHandler(){
		try {
			String log = "Missile Destructor id: "+ this.id+ "\n";
			handler = new FileHandler(War.LOG_PATH + id + ".txt", true);
			handler.setFormatter(new WarFormatter());
			handler.setFilter(new MissileDestructorFilter(this));
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
	
//	public void logMissile(Missile theMissile){
//		StringBuffer buf = new StringBuffer();
//		
//		buf.append("Target Missile: "+ theMissile.getMissileId());
//		if(theMissile.getIsDestructed()){
//			buf.append("\nMissile destroyed!\n");
//		}
//		else{
//			buf.append("\nDestruct failed");
//			buf.append("\nDamage: "+theMissile.getDamage()+"\n");
//		}
//		LoggerManager.getLogger().log(Level.INFO, buf.toString(), this);
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vector<Missile> getDestructedMissile() {
		return this.destructedMissile;
	}

	public void setDestructedMissile(Vector<Missile> destructedMissiles) {
		this.destructedMissile = destructedMissiles;
	}
	

	public void initMissileDestructed(Missile theMissile) {
		for(int i=0; i< destructedMissile.size(); i++){
			if(destructedMissile.get(i).getMissileId().equals(theMissile.getMissileId())){
				theMissile.setDestructAfterLaunch(destructedMissile.get(i).getDestructAfterLaunch());
				destructedMissile.remove(i);
				destructedMissile.add(i, theMissile);
				break;
			}
		}
		
	}


	//inner class
	private class DestructingMissile extends Thread {

		public DestructingMissile() {
		}

		@Override
		public void run() {
			while(true) {
			synchronized (this) {
					if(!destructedMissile.isEmpty()) {
						isBusy = true;
						destructMissile();
						//logMissile(theMissile);
					}

					else {

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

		public void notifyDestructor(Missile m) {
			synchronized (this) {
				if(destructedMissile.size() == 1 /*&& !isBusy*/){
					notify();
				}
			}
		}
	}

}

class MissileDestructorFilter implements Filter {

    private MissileDestructor destructor;

    public MissileDestructorFilter(MissileDestructor destructor) {
        this.destructor = destructor;
    }

    @Override
    public boolean isLoggable(LogRecord rec) {
        if (rec.getSourceClassName().equalsIgnoreCase(MissileDestructor.class.getName()) &&
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




