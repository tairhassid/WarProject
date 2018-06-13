package bussinesLogic;

import java.util.ArrayList;

import baseClasses.Missile;
import baseClasses.MissileDestructor;

public class MissileDestructors {
	private ArrayList<MissileDestructor> destructor = new ArrayList<>();
	private int totalMissilesDestructed;
	
	public MissileDestructors(){
		totalMissilesDestructed = 0;
	}
	
	public void addMissileDestructor(){
		MissileDestructor theMissileDestructor = new MissileDestructor();
		destructor.add(theMissileDestructor);

		//Thread missileDestructor = new Thread(new MissileDestructor());
		//missileDestructor.start();
	}
	
	public void startMissileDestructors(){
		for (MissileDestructor md : destructor){
			md.addFromGson();
		}
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
}
