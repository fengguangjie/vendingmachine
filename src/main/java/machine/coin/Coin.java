package machine.coin;

public enum Coin {
	PENCE10(0.1f),
	PENCE20(0.2f),
	PENCE50(0.5f),
	POUND(1.0f);
	
	private final float value;
	private final int intValue;
	private Coin(float value) {
		this.value = value;
		this.intValue = (int)(this.value * 100);
	}
	
	public float getValue(){
		return this.value;
	}
	
	public int getPence(){
		return this.intValue;
	}
}
