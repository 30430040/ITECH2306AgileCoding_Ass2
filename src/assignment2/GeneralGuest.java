package assignment2;

import java.io.Serializable;

public class GeneralGuest extends Guest implements Serializable
{
	private String address;
    private String phone;
    private String id;
	
    public GeneralGuest(String guestName, String address, String phone, String id) {
		super(guestName);
		this.address = address;
		this.phone = phone;
		this.id = id;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
    
	@Override
    public String getContactDetails() 
	{
        return "Phone: " + this.getPhone() + ", Address: " + this.getAddress() + ", ID: " + this.getId();
    }
}
