package assignment2;

import java.io.Serializable;

public abstract class Guest implements Serializable
{
	private String guestName;
	
	public Guest(String guestName) 
	{
		this.guestName = guestName;
	}

	/**
	 * @return the guestName
	 */
	public String getGuestName() {
		return guestName;
	}

	/**
	 * @param guestName the guestName to set
	 */
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public abstract String getContactDetails();
}
