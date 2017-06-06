package machine.trade;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import machine.coin.ChangePool;
import machine.coin.Coin;
import machine.good.Item;

public class Purchase {
	private static final ChangePool changePool = ChangePool.getInstance();
	
	List<Coin> inputs;
	List<Item> items;
	Map<Coin, Integer> changes;
	
	public Purchase() {
		inputs = new ArrayList<>();
		items = new ArrayList<>();
		changes = new EnumMap<>(Coin.class);
	}
	
	public void addCoin(Coin coin) {
		inputs.add(coin);
	}
	
	public void addItems(Item item) {
		items.add(item);
	}
	
	public Map<Coin, Integer> getChanges() throws Exception {
		float sum = 0;
		for(Coin c : inputs) {
			sum += c.getValue();
			
			changePool.fillInCoin(c, 1);
		}
		
		float buy = 0;
		for(Item item : items) {
			buy += item.getValue();
		}
		
		changes.clear();
		
		int totalChange = (int)Math.round(((sum - buy) * 100));
		
		if (totalChange < 0) {
			throw new Exception("Insert more money.");
		}
		
		if (totalChange == 0)
			return changes;
		
		Coin coin = Coin.POUND;
		totalChange = moreChange(totalChange, coin);
		
		if (totalChange == 0)
			return changes;
		
		coin = Coin.PENCE50;
		totalChange = moreChange(totalChange, coin);
		
		if (totalChange == 0)
			return changes;

		coin = Coin.PENCE20;
		totalChange = moreChange(totalChange, coin);
		
		if (totalChange == 0)
			return changes;
		
		coin = Coin.PENCE10;
		totalChange = moreChange(totalChange, coin);
		
		if (totalChange == 0)
			return changes;
		else {
			throw new Exception("Not enough changes. " 
						+ (int)(totalChange/100) + " is needed.");
		}
	}
	
	private int moreChange(int totalChange, Coin coin) {
		if (totalChange <= 0)
			return 0;
		
		int numCoin = (int)(totalChange / coin.getPence());
				
		if (numCoin > 0) {
			final int numFromPool = changePool.getAvailable(coin);
		
			if (numCoin > numFromPool) {
				numCoin = numFromPool;
			}
			
			changes.put(coin, numCoin);
			changePool.payChange(coin, numCoin);
			return totalChange - numCoin*coin.getPence();
		} else {
			return totalChange;
		}
	}
	
	public List<Coin> getInput(){
		return this.inputs;
	}
	
	public List<Item> getItemInBasket() {
		return items;
	}
	
	public void reset(){
		inputs.clear();
		items.clear();
		changes.clear();
	}
}
