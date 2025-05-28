package assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GeneralGuestBookingGUI extends JFrame {
    private Hotel hotel;

    private JTextField nameField, addressField, phoneField, idField, startDayField, nightsField, depositField;
    private JComboBox<String> roomTypeBox;
    private JCheckBox breakfastBox, applyMidweekOfferBox, applyWeekendOfferBox;
    private JButton quoteButton, confirmButton;
    private JTextArea outputArea;

    public GeneralGuestBookingGUI(Hotel hotel) {
        this.hotel = hotel;
        setTitle("General Guest Booking");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Top form panel
        JPanel form = new JPanel(new GridLayout(10, 2));

        nameField = new JTextField();
        addressField = new JTextField();
        phoneField = new JTextField();
        idField = new JTextField();
        startDayField = new JTextField();
        nightsField = new JTextField();
        depositField = new JTextField();

        roomTypeBox = new JComboBox<>(new String[] { "BASIC", "DELUXE", "EXECUTIVE" });
        breakfastBox = new JCheckBox("Include breakfast");
        applyMidweekOfferBox = new JCheckBox("Apply Midweek Offer");
        applyWeekendOfferBox = new JCheckBox("Apply Weekend Getaway Offer");

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Address:")); form.add(addressField);
        form.add(new JLabel("Phone:")); form.add(phoneField);
        form.add(new JLabel("ID (passport/license):")); form.add(idField);
        form.add(new JLabel("Start Day (1–30):")); form.add(startDayField);
        form.add(new JLabel("Number of Nights:")); form.add(nightsField);
        form.add(new JLabel("Room Type:")); form.add(roomTypeBox);
        form.add(breakfastBox); form.add(new JLabel());
        form.add(applyMidweekOfferBox); form.add(applyWeekendOfferBox);
        form.add(new JLabel("Deposit:")); form.add(depositField);

        add(form, BorderLayout.NORTH);

        // Center output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttons = new JPanel();
        quoteButton = new JButton("Get Quote");
        confirmButton = new JButton("Confirm Booking");

        buttons.add(quoteButton);
        buttons.add(confirmButton);
        add(buttons, BorderLayout.SOUTH);

        // Listeners
        quoteButton.addActionListener(e -> getQuote());
        confirmButton.addActionListener(e -> confirmBooking());
    }

    private void getQuote() {
        try {
            RoomType type = getRoomType();
            int start = Integer.parseInt(startDayField.getText());
            int nights = Integer.parseInt(nightsField.getText());
            boolean breakfast = breakfastBox.isSelected();

            Offer offer = getSelectedOffer(type, start, nights);
            double baseCost = hotel.calculateCost(type, start, nights, breakfast);

            if (offer != null) {
                int[] days = new int[nights];
                for (int i = 0; i < nights; i++) days[i] = (start + i - 1) % 7;
                baseCost = offer.applyDiscount(baseCost, type, days);
            }

            outputArea.setText(String.format("Estimated Cost: $%.2f", baseCost));
        } catch (Exception ex) {
            outputArea.setText("Error: Please ensure all fields are filled correctly.");
        }
    }

    private void confirmBooking() {
        try {
            GeneralGuest guest = new GeneralGuest(
                nameField.getText(),
                addressField.getText(),
                phoneField.getText(),
                idField.getText()
            );

            RoomType type = getRoomType();
            int start = Integer.parseInt(startDayField.getText());
            int nights = Integer.parseInt(nightsField.getText());
            boolean breakfast = breakfastBox.isSelected();
            double deposit = Double.parseDouble(depositField.getText());

            Offer offer = getSelectedOffer(type, start, nights);
            int[] days = new int[nights];
            for (int i = 0; i < nights; i++) days[i] = (start + i - 1) % 7;

            double totalCost = hotel.calculateCost(type, start, nights, breakfast);
            if (offer != null) {
                totalCost = offer.applyDiscount(totalCost, type, days);
            }

            if (deposit < totalCost * 0.5 || deposit > totalCost) {
                outputArea.setText("Deposit must be at least 50% and not more than total.");
                return;
            }

            ExtendedBooking booking = hotel.addGeneralGuestBooking(guest, start, nights, type, totalCost, deposit, offer, breakfast);
            if (booking != null) {
                outputArea.setText("Booking confirmed! ID: " + booking.getId() +
                                   "\nRoom: " + booking.getRoom().getRoomNumber() +
                                   "\nTotal: $" + totalCost +
                                   "\nDeposit Paid: $" + deposit);
            } else {
                outputArea.setText("No room available for the selected type and dates.");
            }

        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private RoomType getRoomType() {
        switch (roomTypeBox.getSelectedItem().toString()) {
            case "BASIC": return RoomType.BASIC_SINGLE_SUITE;
            case "DELUXE": return RoomType.DELUXE_SUITE;
            case "EXECUTIVE": return RoomType.EXECUTIVE_SUITE;
            default: throw new IllegalArgumentException("Invalid room type");
        }
    }

    private Offer getSelectedOffer(RoomType type, int start, int nights) {
        int[] days = new int[nights];
        for (int i = 0; i < nights; i++) days[i] = (start + i - 1) % 7;

        if (applyMidweekOfferBox.isSelected()) return new MidweekOffer();
        if (applyWeekendOfferBox.isSelected()) return new WeekendGetawayOffer();
        return null;
    }

    // Launch method
    public static void launch(Hotel hotel) {
        SwingUtilities.invokeLater(() -> new GeneralGuestBookingGUI(hotel).setVisible(true));
    }
}
