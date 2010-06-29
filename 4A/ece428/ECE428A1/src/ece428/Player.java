package ece428;

import java.io.Serializable;

/**
 * A soccer player
 */
public class Player implements Serializable {
	public Player(String name, String country) {
		this.name = name;
		this.country = country;
	}

	private static final long serialVersionUID = 2178427011902385685L;
	
	protected String name;
	protected String country;

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}
}
