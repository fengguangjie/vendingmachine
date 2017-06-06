package machine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import machine.coin.ChangePool;
import machine.coin.Coin;
import machine.good.Item;
import machine.good.Storage;
import machine.trade.Purchase;

/**
 * Encapsulates the state of a vending machine and the operations that can be performed on it
 */
public class VendingMachine {
	private static final Storage storage = Storage.getInstance();
	private static final ChangePool changePool = ChangePool.getInstance();
	
	private volatile boolean isOn = false; 
	private Purchase currentPurchase = null;
	
	private final List<Purchase> purchases;
	
	public VendingMachine() {
		super();
		
		purchases = new ArrayList<>();
	}
	
	public synchronized boolean isOn() {
		return isOn;
	}
	
	public synchronized void setOn() {
		isOn = true;
	}
	
	public synchronized void setOff() {
		if (!isOn)
			return;
		
		final List<Coin> reject = reject();
		
		System.out.println(reject);
		
		isOn = false;
	}
	
	public synchronized void addInput(Coin coin) {
		if (!isOn)
			return;
		
		if (currentPurchase == null)
			currentPurchase = new Purchase();
		
		currentPurchase.addCoin(coin);
	}
	
	public synchronized boolean addPurchaseItem(Item item){
		if (!isOn)
			return false;
		
		if (currentPurchase == null)
			currentPurchase = new Purchase();
		
		if(storage.hasItem(item)) {
			currentPurchase.addItems(item);
			return true;
		} else
			return false;
	}
	
	public synchronized List<Coin> reject() {
		if (!isOn || currentPurchase == null)
			return new ArrayList<Coin>();
		
		final List<Coin> ret = new ArrayList<>(currentPurchase.getInput());
		
		for(Item item : currentPurchase.getItemInBasket()) {
			storage.fillInItem(item, 1);
		}
		
		currentPurchase = null;
		
		return ret;
	}
	
	public synchronized List<Coin> currentInsertedMoney() {
		if (!isOn  || currentPurchase == null)
			return new ArrayList<Coin>();
		
		final List<Coin> ret = new ArrayList<>(currentPurchase.getInput());
		
		return ret;
	}
	
	public boolean fillInItem(Item item, int count) {
		if(!isOn)
			return false;
		
		return storage.fillInItem(item, count);
	}
	
	public synchronized boolean fillInChange(Coin coin, int count) {
		if(!isOn)
			return false;
		
		return changePool.fillInCoin(coin, count);
	}
	
	public synchronized Map<Coin, Integer> purchase() {
		if (!isOn)
			return new EnumMap<Coin, Integer>(Coin.class);
		
		purchases.add(currentPurchase);
			
		try {
			final Map<Coin, Integer> changes = currentPurchase.getChanges();
			
			currentPurchase = null;
			
			return changes;
		} catch (Exception e) {
			final List<Coin> reject = reject();
			System.out.println(reject);
			
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return new EnumMap<Coin, Integer>(Coin.class);
	}
}
