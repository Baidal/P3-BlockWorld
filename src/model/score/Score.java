package model.score;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 * @param <T> generic
 */
public abstract class Score<T> implements Comparable<Score<T>> {

	/**
	 * String
	 */
	private String playerName;

	/**
	 * score
	 */
	protected double score;
	
	
	/**
	 * Inicializa el score a 0, y asigna a playerName la String pasada 
	 * por parametro.
	 * @param pn String
	 */
	public Score(String pn) {
		
		score = 0;
		playerName = pn;
	
	}


	@Override
	public String toString() {
		return playerName + ":" + score;
	}

	/**
	 * Getter
	 * @return String
	 */
	public String getPlayerName() {
		return playerName;
	}

	
	
	/**
	 * Getter
	 * @return double
	 */
	public double getScoring() {
		return score;
	}

	abstract public void score(T t);

	public String getName() {
		
		return playerName;
		
	}
	
	
	
	
}
