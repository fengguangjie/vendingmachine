package machine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import machine.coin.ChangePool;
import machine.coin.Coin;
import machine.good.Item;
import machine.good.Storage;

/**
 * Unit tests for {@link VendingMachine}
 */
public class VendingMachineTest {
	
	@Test
	public void defaultStateIsOff() {
		final VendingMachine machine = new VendingMachine();
		assertFalse(machine.isOn());
	}
	
	@Test
	public void turnsOn() {
		final VendingMachine machine = new VendingMachine();
		machine.setOn();
		assertTrue(machine.isOn());		
	}
	
	@Test
	public void insertedMoney(){
		final List<Coin> expected = new ArrayList<Coin>();
		expected.add(Coin.PENCE10);
		expected.add(Coin.PENCE20);
		expected.add(Coin.PENCE50);
		expected.add(Coin.PENCE20);
		expected.add(Coin.PENCE10);
		expected.add(Coin.POUND);
		
		final VendingMachine machine = new VendingMachine();
		machine.setOn();
		
		for(Coin e : expected) {
			machine.addInput(e);
		}
	
		final List<Coin> insertedMoney = machine.currentInsertedMoney();
		
		assertEquals(expected, insertedMoney);
		
		final List<Coin> reject = machine.reject();
	
		assertEquals(expected, reject);
		
		assertTrue(machine.currentInsertedMoney().isEmpty());
	}
	
	@Test
	public void insertedMoneyTest() {
		final List<Coin> expected = new ArrayList<Coin>();
		expected.add(Coin.PENCE10);
		expected.add(Coin.PENCE20);
		expected.add(Coin.PENCE50);
		expected.add(Coin.PENCE20);
		expected.add(Coin.PENCE10);
		expected.add(Coin.POUND);
		
		final VendingMachine machine = new VendingMachine();
		
		machine.setOn();
		
		for(Coin e : expected) {
			machine.addInput(e);
		}

		final List<Coin> insertedMoney = machine.currentInsertedMoney();
		
		assertEquals(expected, insertedMoney);
		
		final List<Coin> reject = machine.reject();
	
		assertEquals(expected, reject);
		
		assertTrue(machine.currentInsertedMoney().isEmpty());

	}
	
	@Test
	public void itemTest(){
		final Storage storage = Storage.getInstance();
		storage.reset();
	
		final VendingMachine machine = new VendingMachine();
		
		assertFalse(machine.fillInItem(Item.ITEM1, 100));
		
		machine.setOn();
		
		assertTrue(machine.fillInItem(Item.ITEM1, 1));
		assertTrue(machine.fillInItem(Item.ITEM2, 2));
		assertTrue(machine.fillInItem(Item.ITEM3, 3));
		
		assertTrue(machine.addPurchaseItem(Item.ITEM1));
		assertTrue(machine.addPurchaseItem(Item.ITEM2));
		assertTrue(machine.addPurchaseItem(Item.ITEM3));
		
		assertFalse(machine.addPurchaseItem(Item.ITEM1));
		
		assertEquals(0, storage.getCount(Item.ITEM1));
		assertEquals(1, storage.getCount(Item.ITEM2));
		assertEquals(2, storage.getCount(Item.ITEM3));
	}

	@Test
	public void changeTest(){
		final Storage storage = Storage.getInstance();
		storage.reset();
	
		final ChangePool changePool = ChangePool.getInstance();
		changePool.reset();
		
		final VendingMachine machine = new VendingMachine();
		
		assertFalse(machine.fillInItem(Item.ITEM1, 100));
		
		machine.setOn();
		
		assertTrue(machine.fillInItem(Item.ITEM1, 1));
		assertTrue(machine.fillInItem(Item.ITEM2, 2));
		assertTrue(machine.fillInItem(Item.ITEM3, 3));
		
		assertTrue(machine.fillInChange(Coin.PENCE10, 10));
		assertTrue(machine.fillInChange(Coin.PENCE20, 20));
		assertTrue(machine.fillInChange(Coin.PENCE50, 25));
		assertTrue(machine.fillInChange(Coin.POUND, 5));
		
		
		assertTrue(machine.addPurchaseItem(Item.ITEM1));
		assertTrue(machine.addPurchaseItem(Item.ITEM2));
		assertTrue(machine.addPurchaseItem(Item.ITEM3));
		assertTrue(machine.addPurchaseItem(Item.ITEM2));
				
		assertFalse(machine.addPurchaseItem(Item.ITEM1));
		
		assertEquals(0, storage.getCount(Item.ITEM1));
		assertEquals(0, storage.getCount(Item.ITEM2));
		assertEquals(2, storage.getCount(Item.ITEM3));
		
		machine.addInput(Coin.POUND);
		machine.addInput(Coin.PENCE20);
		machine.addInput(Coin.PENCE50);
		machine.addInput(Coin.POUND);
		machine.addInput(Coin.PENCE10);
		machine.addInput(Coin.PENCE50);
		machine.addInput(Coin.PENCE50);
		machine.addInput(Coin.PENCE20);
		machine.addInput(Coin.POUND);
		
		final Map<Coin, Integer> changes = machine.purchase();
		assertEquals(2, changes.size());
		assertEquals(1, changes.get(Coin.PENCE20).intValue());
		assertEquals(1, changes.get(Coin.PENCE50).intValue());
				
		final ChangePool pool = ChangePool.getInstance();
		final Map<Coin, Integer> availableChanges = pool.getPool();
		assertEquals(11, availableChanges.get(Coin.PENCE10).intValue());
		assertEquals(21, availableChanges.get(Coin.PENCE20).intValue());
		assertEquals(27, availableChanges.get(Coin.PENCE50).intValue());
		assertEquals(8, availableChanges.get(Coin.POUND).intValue());
	}
}
