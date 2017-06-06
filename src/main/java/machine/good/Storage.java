package machine.good;

import java.util.EnumMap;
import java.util.Map;

public final class Storage {
	public static Storage getInstance() {
		if (instance == null) {
			synchronized (Storage.class) {
				if (instance == null) {
					instance = new Storage();
				}
			}
		}

		return instance;
	}
		
	private static Storage instance = null;
	
	private Map<Item, Integer> allItems;
	
	private Storage() {
		allItems = new EnumMap<>(Item.class);
		
		for(Item i : Item.values())
			allItems.put(i, 0);
	}
	
	public boolean fillInItem(Item item, int count) {
		Integer v = allItems.get(item);
		
		v = v.intValue() + count;
		
		allItems.put(item, v);
		
		return true;
	}
	
	public boolean hasItem(Item item) {
		Integer v = allItems.get(item);
		
		if (v.intValue() > 0) {
			v = v.intValue() - 1;
			allItems.put(item, v);
			return true;
		} else
			return false;
	}
	
	public int getCount(Item item) {
		return allItems.get(item);
	}
	
	public void reset() {
		allItems.clear();
		for(Item i : Item.values())
			allItems.put(i, 0);
	}
}
