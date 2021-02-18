package model.score;

import model.ItemStack;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 */
public class CollectedItemsScore extends Score<ItemStack> {

	/**
	 * Constructor sobrecargado a partir de del nombre de un jugador
	 * @param pn String
	 */
	public CollectedItemsScore(String pn) {
		super(pn);
		
		
	}

	@Override
	public int compareTo(Score<ItemStack> o) {
		if(this.score == o.getScoring())
			return 0;
		else if(this.score > o.getScoring())
			return -1;
		else
			return 1;
		
	}

	@Override
	public void score(ItemStack t) {
		
		score += t.getAmount()*t.getType().getValue();
		
	}

	

}
