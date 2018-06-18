package bussinesLogic;

public class WarSummary {
	public static WarSummary warInstance = null;
	private int totalLaunchedMissiles;
	private int totalDestructedMissiles;
	private int totalMissilesHit;
	private int totalDestroyedMissilesDestructors;
	private int totalDamage;
	
	private WarSummary(){
		this.totalLaunchedMissiles = 0;
		this.totalDestructedMissiles = 0;
		this.totalMissilesHit = 0;
		this.totalDestroyedMissilesDestructors = 0;
		this.totalDamage = 0;
	}
	
	public static WarSummary getInstance() {
		if (warInstance == null) {
			warInstance = new WarSummary();
		}
		return warInstance;
	}
	
	
	public void addLaunchedMissile(){
		this.totalLaunchedMissiles++;
	}
	
	public void addDestructedMissile(){
		this.totalDestructedMissiles++;
	}
	
	public void addMissileHit(){
		this.totalMissilesHit++;
	}
	
	public void addDestroyedMissileDestructor(){
		this.totalDestroyedMissilesDestructors++;
	}
	
	public void addDamage(int damage){
		this.totalDamage+= damage;
	}
	
	public void setTotalLaunchedMissiles(int totalLaunchedMissiles){
		this.totalLaunchedMissiles = totalLaunchedMissiles;
	}
	
	public void setTotalDestroyedMissilesDestructors(int total){
		this.totalDestroyedMissilesDestructors = total;
	}
	
	public String getWarSummary(){
		String str = "";
		str += "Summary:\n" + "Total missiles launched: "+ WarSummary.getInstance().getTotalLaunchedMissiles()+"\n"+
				"Total missiels hit the target: "+ WarSummary.getInstance().getTotalMissilesHit()+"\n"+
				"Total missiles destroyed: "+ WarSummary.getInstance().getTotalDestructedMissiles()+"\n"+
				"Total launchers destroyed: "+ WarSummary.getInstance().getTotalDestroyedMissilesDestructors()+"\n"+
				"Total damage: "+ WarSummary.getInstance().getTotalDamage()+"\n";
		return str;
	}

	public int getTotalLaunchedMissiles() {
		return totalLaunchedMissiles;
	}

	public int getTotalDestructedMissiles() {
		return totalDestructedMissiles;
	}

	public int getTotalMissilesHit() {
		return totalMissilesHit;
	}

	public int getTotalDestroyedMissilesDestructors() {
		return totalDestroyedMissilesDestructors;
	}

	public int getTotalDamage() {
		return totalDamage;
	}
	
	
	
	
}
