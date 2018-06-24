package exe1_personsLoggerWithObject;

public class Program {

	public static void main(String[] args) {
		Person gogo = new Person("gogo");
		Person momo = new Person("momo");
		Person yoyo = new Person("yoyo");
		
		gogo.goToTheSea();
		momo.goToTheSea();
		gogo.goToMovie();
		yoyo.goToThePark();
	}
}
