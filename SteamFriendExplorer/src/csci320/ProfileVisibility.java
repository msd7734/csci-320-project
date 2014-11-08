package csci320;

public enum ProfileVisibility {
	hidden(1),
	visible(3);
	
	private final int id;
	ProfileVisibility(int id) {
		this.id = id;
	}
	
	public int getValue() { return id; }
}
