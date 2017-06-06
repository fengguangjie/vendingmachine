package machine.good;

public enum Item {
	ITEM1(0.6f),
	ITEM2(1.00f),
	ITEM3(1.7f);
	
	private final float value;
		
	private Item(float value) {
		this.value = value;
	}
	
	public float getValue(){
		return this.value;
	}
}
