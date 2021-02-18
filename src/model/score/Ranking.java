package model.score;

import java.util.SortedSet;
import java.util.TreeSet;

import model.exceptions.score.EmptyRankingException;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 * @param <ScoreType> extends Score<?>
 */
public class Ranking<ScoreType extends Score<?>>{

	
	/**
	 * SortedSet<ScoreType>
	 */
	private SortedSet<ScoreType> scores;
	
	/**
	 * Constructor que crea un SortedSet
	 */
	public Ranking() {
		
		scores = new TreeSet<>();
		
	}
	
	/**
	 * Añade una puntuación al ranking.
	 * @param st ScoreType
	 */
	public void addScore(ScoreType st) {
		
		scores.add(st);
		
	}
	
	/**
	 * Gettter del conjunto ordenado de las puntuaciones.
	 * @return
	 */
	public SortedSet<ScoreType> getSortedRanking(){
		
		return scores;
		
	}
	
	/**
	 * Obtiene la puntuación ganadora del ranking.
	 * @return ScoreType
	 */
	public ScoreType getWinner() throws EmptyRankingException {
		
		if(scores.isEmpty())
			throw new EmptyRankingException();
		else
			return scores.first();
		
	}
	
	
	
}
