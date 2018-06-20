package bussinesLogic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.FileHandler;

import baseClasses.Missile;
import baseClasses.MissileLauncher;

public class MissileLaunchers {
	private ArrayList<MissileLauncher> launcher = new ArrayList<>();
	private ArrayList<MissileLauncher> activeLauncher = new ArrayList<>();

	
	

	public MissileLaunchers(){
	}

	public void addMissileLauncher(){
		int rndHidden = War.randomNumber(0, 2);
		MissileLauncher theMissileLauncher = new MissileLauncher(rndHidden);
		launcher.add(theMissileLauncher);
		activeLauncher.add(theMissileLauncher);

		Thread missileLauncher = new Thread(theMissileLauncher);
		//theMissileLauncher.setLauncherThread(missileLauncher);
		//theMissileLauncher.setHandler();
		System.out.println("add missile launcher id:"+theMissileLauncher.getId());
		System.out.println("isHidden="+ theMissileLauncher.getIsHidden());
		missileLauncher.start();
	}
	
	

	public void launchMissile(String destination, int flyTime, int damage, ArrayList<Missile> allMissiles){
			MissileLauncher randLauncher = getMissileLauncher();
			Missile theMissile = new Missile(destination, flyTime, damage, randLauncher);
			allMissiles.add(theMissile);
			randLauncher.addMissile(theMissile); 
		} 
	

	public MissileLauncher findMissileLauncher(){
		MissileLauncher randLauncher = getMissileLauncher();
		if(!randLauncher.getIsHidden())
			activeLauncher.remove(randLauncher);
		return randLauncher;
	}
	
	
	
	public MissileLauncher getMissileLauncher(){
		if(activeLauncher.size() == 0)
			return null;
		int randomIndex = War.randomNumber(0, activeLauncher.size());
		MissileLauncher randLauncher = activeLauncher.get(randomIndex);
		return randLauncher;
	}

	public ArrayList<MissileLauncher> getLauncher() {
		return launcher;
	}
	


	public void setLauncher(ArrayList<MissileLauncher> launcher) {
		this.launcher = launcher;
	}
	
	public ArrayList<Missile> getAllMissiles(){
		ArrayList<Missile> allMissiles = new ArrayList<>();
		for(MissileLauncher ml : launcher){
			allMissiles.addAll(ml.getMissile());
		}
		return allMissiles;
	}


	public void setActiveLaunchers() {
		activeLauncher.addAll(launcher);
	}
	
	public ArrayList<MissileLauncher> getActiveLaunchers(){
		return this.activeLauncher;
	}


	public void startLaunchers() {
		for(MissileLauncher ml : launcher){
			ml.setHandler();
			Collections.sort(ml.getMissile());
			Thread t = new Thread(ml);
			//ml.setLauncherThread(t);
			t.start();
		}
	}

	public void startMissiles() {
		for(MissileLauncher ml : launcher){
			ml.addMissileFromGson(ml);
		}
	}
	
	@Override
	public String toString(){
		String str="";
		for(MissileLauncher ml: launcher){
			str += ml.toString();
		}
		return str;
	}
}
