package bussinesLogic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class JasonManager {
		private Gson gson = new Gson();
		private War war= new War();
		
		public JasonManager(War war){
			this.war = war;
		}
		
		public War readFromGson(){
			if(Files.exists(Paths.get("war.json"))){
				FileReader reader = null;
				try {
					reader = new FileReader("war.json");
				}
				catch(FileNotFoundException e){
					e.printStackTrace();
				}
			   return war = gson.fromJson(reader, War.class);
			    
			}
			return null;
		}

		
		public void setAllMissilesFromGson(){
			war.setAllMissiles((war.getMissileLaunchers()).getAllMissiles());
		}
		
		public void startLaunchers(){
			war.getMissileLaunchers().startLaunchers();
		}

		public void startMissiles() {
			war.getMissileLaunchers().startMissiles();
			
		}
		
		public void startMissileDestructors(){
			war.getMissileDestructors().startMissileDestructors();
		}
		
		
		
		
}
