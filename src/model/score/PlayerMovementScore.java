package model.score;

import model.Location;

/**
 * 
 * @author PC Luis Vidal Rico 45927898A
 *
 */
public class PlayerMovementScore extends Score<Location>{

	/**
	 * Location	
	 */
	private Location previousLocation;
	
	/**
	 * Constructor sobrecargado a partir del nombre de un jugador.
	 * Inicializa a ‘null’ previousLocation.
	 * @param pn
	 */
	public PlayerMovementScore(String pn) {
		
		super(pn);
		previousLocation = null;
		
	}

	@Override
	public int compareTo(Score<Location> o) {
		if(this.score == o.getScoring())
			return 0;
		else if(this.score > o.getScoring())
			return 1;
		else
			return -1;
	}

	@Override
	public void score(Location loc) {
		
		if(previousLocation == null) { //Caso en el que se invoca por primera vez a score 
			previousLocation = new Location(loc);
			
			score = 0;
			
		}else {//Caso en el que ya se ha invocado score
			
			score += loc.distance(previousLocation);
			previousLocation = new Location(loc);
			
		}
		
		
	}

}
