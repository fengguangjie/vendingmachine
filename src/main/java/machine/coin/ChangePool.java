package machine.coin;

import java.util.EnumMap;
import java.util.Map;

import machine.good.Storage;

public class ChangePool {
	public static ChangePool getInstance() {
		if (instance == null) {
			synchronized (Storage.class) {
				if (instance == null) {
					instance = new ChangePool();
				}
			}
		}

		return instance;
	}
		
	private static ChangePool instance = null;
	
	private Map<Coin, Integer> allCoins;
	
	private ChangePool() {
		allCoins = new EnumMap<>(Coin.class);
		
		for(Coin i : Coin.values())
			allCoins.put(i, 0);
	}
	
	public boolean fillInCoin(Coin coin, int count) {
		Integer v = allCoins.get(coin);
		
		v = v.intValue() + count;
		
		allCoins.put(coin, v);
		
		return true;
	}
	
	public int getAvailable(Coin coin) {
		return allCoins.get(coin);
	}
	
	public boolean payChange(Coin coin, int count) {
		Integer v = allCoins.get(coin);
		
		v = v.intValue() - count;
		
		allCoins.put(coin, v);
		
		return true;
	}
	
	public boolean hasItem(Coin coin) {
		Integer v = allCoins.get(coin);
		
		if (v.intValue() > 0) {
			v = v.intValue() - 1;
			allCoins.put(coin, v);
			return true;
		} else
			return false;
	}
	
	public int getCount(Coin coin) {
		return allCoins.get(coin);
	}
	
	public void reset() {
		allCoins.clear();
		for(Coin i : Coin.values())
			allCoins.put(i, 0);
	}
	
	public Map<Coin, Integer> getPool() {
		return allCoins;
	}
}
