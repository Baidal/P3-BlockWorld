package model.score;

import model.Block;

/**
 * 
 * @author PC Luis Vidal Rico 45927898A
 *
 */
public class MiningScore extends Score<Block>{

	/**
	 * Constructor sobrecargado a partir del nombre de un jugador.
	 * @param pn String
	 */
	public MiningScore(String pn) {
		super(pn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Score<Block> b ) {
		if(this.score == b.getScoring())
			return 0;
		else if(this.score > b.getScoring())
			return -1;
		else
			return 1;
		
		
	}

	@Override
	public void score(Block t) {
		
		score += t.getType().getValue();
		
	}

	
	
	
}
