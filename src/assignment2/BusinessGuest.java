package assignment2;

import java.io.Serializable;

/**
 * Represents a guest who is part of a business.
 * Inherits from Guest and adds business-specific details such as phone number,
 * membership number, and associated business.
 */
public class BusinessGuest extends Guest implements Serializable {

    private String phone;              // Phone number of the business guest.
    private int memberNumber;          // Membership number of the business guest.
    private Business businessName;     // The business associated with the guest.

    /**
     * Constructs a BusinessGuest with the provided details.
     *
     * @param guestName     the name of the guest
     * @param phone         the phone number of the guest
     * @param memberNumber  the membership number
     * @param businessName  the associated business
     */
    public BusinessGuest(String guestName, String phone, int memberNumber, Business businessName) {
        super(guestName);
        this.phone = phone;
        this.memberNumber = memberNumber;
        this.businessName = businessName;
    }

    /**
     * Returns the phone number of the guest.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the guest.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the membership number.
     *
     * @return the membership number
     */
    public int getMemberNumber() {
        return memberNumber;
    }

    /**
     * Sets the membership number.
     *
     * @param memberNumber the new membership number
     */
    public void setMemberNumber(int memberNumber) {
        this.memberNumber = memberNumber;
    }

    /**
     * Returns the associated business.
     *
     * @return the business
     */
    public Business getBusinessName() {
        return businessName;
    }

    /**
     * Sets the associated business.
     *
     * @param businessName the business to associate with
     */
    public void setBusinessName(Business businessName) {
        this.businessName = businessName;
    }

    /**
     * Returns the formatted contact details of the guest.
     *
     * @return contact details string
     */
    @Override
    public String getContactDetails() {
        return "Phone: " + this.getPhone() + ", Business: " + this.getBusinessName().getName() + ", Member #: " + this.getMemberNumber();
    }
}
