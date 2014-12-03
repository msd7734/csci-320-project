package csci320;

public enum ProfileVisibility {
	
	/**
	 * Explanation of this class via https://developer.valvesoftware.com/wiki/Steam_Web_API#GetPlayerSummaries_.28v0001.29
	 * 
	 * 'communityvisibilitystate'
	 * This represents whether the profile is visible or not, and if it is visible,
	 * why you are allowed to see it. Note that because this WebAPI does not use authentication,
	 * there are only two possible values returned: 1 - the profile is not visible to you
	 * (Private, Friends Only, etc), 3 - the profile is "Public", and the data is visible.
	 * Mike Blaszczak's post on Steam forums says, "The community visibility state this API returns
	 * is different than the privacy state. It's the effective visibility state from the account making
	 * the request to the account being viewed given the requesting account's relationship to the viewed account."
	 */
	invalid(0),
	hidden(1),
	visible(3);
	
	private final int id;
	ProfileVisibility(int id) {
		this.id = id;
	}
	
	public int getValue() { return id; }
	
	public static ProfileVisibility valueOf(int i) {
		switch(i) {
		case 1:
			return hidden;
		case 3:
			return visible;
		default:
			return invalid;
		}
	}
}
