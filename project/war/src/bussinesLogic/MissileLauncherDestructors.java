package bussinesLogic;


import java.util.ArrayList;
import baseClasses.MissileLauncher;
import baseClasses.MissileLauncherDestructor;


public class MissileLauncherDestructors {
	private ArrayList<MissileLauncherDestructor> destructor = new ArrayList<>();
	//private int totalMissileLauncherDestroyed;
	
	
	public MissileLauncherDestructors(){
		//totalMissileLauncherDestroyed = 0;
	}
	
	public void addMissileLauncherDestructor(MissileLauncherDestructor.DestructorType t) throws IllegalArgumentException { 
		MissileLauncherDestructor theMissileLauncherDestructor = new MissileLauncherDestructor(t);
		destructor.add(theMissileLauncherDestructor);

		//Thread missileLauncherDestructor = new Thread(new MissileDestructor());
		//System.out.println("Missile distructor - "+ missileLauncherDestructor.getId());
		//missileLauncherDestructor.start();
		
	}
	
	
	@Override 
	public String toString(){
		String str ="";
		for (MissileLauncherDestructor mld: destructor){
			str += mld.toString();
		}
		return str;
	}
	
	
	public void destructMissileLauncher(MissileLauncher theMissileLauncher){
		MissileLauncherDestructor theMissileLauncherDestructor = findMissileLauncherDestructor();
		if (theMissileLauncherDestructor == null){
			//No Missile launcher destructor exist
		}
		else{
			theMissileLauncherDestructor.add(theMissileLauncher);
//			if(theMissileLauncher.getIsDestroyed())
//				totalMissileLauncherDestroyed++;
//			//theMissileLauncher.destructMissileLauncher(); //interrupt the thread
//			System.out.println("Missile is hidden="+ theMissileLauncher.isHidden());
//			System.out.println("Missile Launcher destroyed="+theMissileLauncher.getIsDestroyed());
		}
	}
	
	private MissileLauncherDestructor findMissileLauncherDestructor(){
		if (!destructor.isEmpty()){
			return destructor.get(0);
		}
		return null;
	}

	public void startMissileLauncherDestructors() {
		for(MissileLauncherDestructor mld : destructor) {
			mld.setHandler();
			mld.addFromGson();
		}
		
	}
	
	public void initMissileDestructor(MissileLauncher theMissileLauncher) {
		for (MissileLauncherDestructor mld: destructor ){
			mld.initMissileLauncherDestructed(theMissileLauncher);
		}
		
	}
	

	
 
	
}
