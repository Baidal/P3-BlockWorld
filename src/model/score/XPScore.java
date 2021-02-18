package model.score;

import java.util.ArrayList;
import java.util.List;

import model.entities.Player;
import model.exceptions.score.ScoreException;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 */
public class XPScore extends Score<Player>{
	
	

	/**
	 * Player
	 */
	private Player player;
	/**
	 * List<Score<Player>>
	 */
	private List<Score<?>> scores;
	
	/**
	 * Constructor sobrecargado a partir de un jugador. 
	 * Reserva memoria para una lista de puntuaciones.
	 * @param pn String
	 */
	public XPScore(Player p) {
		
		super(p.getName());
		scores = new ArrayList<>();
		player = p;
		
	}

	@Override
	public int compareTo(Score<Player> o) {
		if(this.getScoring() == o.getScoring())
			return 0;
		else if(this.getScoring() > o.getScoring())
			return -1;
		else
			return 1;
	}

	@Override
	public void score(Player p) throws ScoreException {
		
		double media = 0;
		
		if(!player.equals(p))
			throw new ScoreException("El jugador pasado por parámetro no es el mismo con el que se creó la puntuación.");
		else {
		
			for(Score<?> paux:scores) {
				
				media += paux.getScoring();
				
			}
			
			if(scores.size() != 0)
				media = media/scores.size();
			
			score = media + p.getHealth() + p.getFoodLevel();
		
		
		}
			
	}
	
	/**
	 * Recalcula la puntuación y la devuelve.
	 * @return double
	 */
	public double getScoring() {
		double puntuacion = 0;
		
		for(Score<?> paux:scores) {
			
			puntuacion += paux.getScoring();
			
		}
		
		if(scores.size() != 0)
			puntuacion = puntuacion/scores.size() + player.getHealth() + player.getFoodLevel();
		else
			puntuacion = player.getHealth() + player.getFoodLevel();
		
		
		return puntuacion;
	}
	
	
	/**
	 * Añade una nueva puntuación y recalcula la puntuación agregada.
	 * @param s Score<?>
	 */
	public void addScore(Score<?> s) {
		
		scores.add(s);
		score(player);
		
		
	}
	
	
	
}
