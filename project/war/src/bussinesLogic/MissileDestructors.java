package bussinesLogic;

import java.util.ArrayList;
import java.util.Collections;

import baseClasses.Missile;
import baseClasses.MissileDestructor;

public class MissileDestructors {
	private ArrayList<MissileDestructor> destructor = new ArrayList<>();
//	private int totalMissilesDestructed;
	
	public MissileDestructors(){
//		totalMissilesDestructed = 0;
	}
	
	
	public void addMissileDestructor(){
		MissileDestructor theMissileDestructor = new MissileDestructor();
		destructor.add(theMissileDestructor);
	}
	
	public void startMissileDestructors(){
		for (MissileDestructor md : destructor){
			Collections.sort(md.getDestructedMissile(), new SortByDestructTime());
			md.addFromGson();
		}
	}
	
	@Override
	public String toString(){
		String str ="";
		for (MissileDestructor md: destructor){
			str += md.toString();
		}
		return str;
	}
	
//	public void destructMissile(ArrayList<Missile> allMissiles){
//		MissileDestructor theMissileDestructor = findMissileDestructor();
//		if (theMissileDestructor == null){
//			//No missile destructor exist
//		}
//		else {
//			//Missile theMissile = findMissile();
//			for(Missile theMissile : allMissiles)
//				if(theMissile.getLaunchTime() > 0 && theMissileDestructor.destructMissile(theMissile)) {
//					totalMissilesDestructed++;
//					break;
//			}
//		}
//	}
	
	public void destructMissile(Missile theMissile){
		MissileDestructor theMissileDestructor = findMissileDestructor();
		if (theMissileDestructor == null){
			//No missile destructor exist
		}
		else {
			//Missile theMissile = findMissile();
			theMissileDestructor.add(theMissile);
			
//			for(Missile theMissile : allMissiles)
//				if(theMissile.getLaunchTime() > 0 && theMissileDestructor.destructMissile(theMissile)) {
//					totalMissilesDestructed++;
//					break;
//			}
		}
	}
	
	
	
	private MissileDestructor findMissileDestructor(){
		if (!destructor.isEmpty())
			return destructor.get(0);
		return null;
	}


	public void initMissileDestructor(Missile theMissile) {
		for (MissileDestructor md: destructor ){
			md.initMissileDestructed(theMissile);
		}
		
	}

	public ArrayList<MissileDestructor> getDestructors() {
		return destructor;
	}


	public void endWar() {
		System.out.println("in missile destructors");
		for(MissileDestructor md : destructor)
			md.endWar();	
	}
	
	
}
