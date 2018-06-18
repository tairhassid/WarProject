package bussinesLogic;

import java.util.Comparator;

import baseClasses.Missile;



	
	class SortByDestructTime implements Comparator<Missile> {

		@Override
		public int compare(Missile m1, Missile m2) {
			return (int) ((m1.getDestructAfterLaunch()+m1.getLaunchTime()) - 
					(m2.getDestructAfterLaunch()+m2.getLaunchTime()));
		}
		
	}

