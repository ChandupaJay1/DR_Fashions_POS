package NerdTech.DR_Fashion.Validation;

import javax.swing.*;
import java.util.regex.*;

public class InputValidator {

    // Check if a text field is empty
    public static boolean isFieldEmpty(JTextField field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            showError(fieldName + " cannot be empty.");
            field.requestFocus();
            return true;
        }
        return false;
    }

    // Check if JComboBox selection is empty or none selected
    public static boolean isFieldEmpty(JComboBox<?> comboBox, String fieldName) {
        if (comboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a " + fieldName + ".", "Validation Error", JOptionPane.WARNING_MESSAGE);
            comboBox.requestFocus();
            return true;
        }
        return false;
    }

    // Check if input is a valid email
    public static boolean isValidEmail(JTextField field) {
        String email = field.getText().trim();
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(emailRegex)) {
            showError("Invalid email address.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    // Check if input is a valid integer
    public static boolean isInteger(JTextField field, String fieldName) {
        try {
            Integer.valueOf(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            showError(fieldName + " must be a valid integer.");
            field.requestFocus();
            return false;
        }
    }

    // Check if input is a valid float/double
    public static boolean isFloat(JTextField field, String fieldName) {
        try {
            Double.valueOf(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            showError(fieldName + " must be a valid number.");
            field.requestFocus();
            return false;
        }
    }

    // Check for valid phone number (basic pattern)
    public static boolean isValidPhoneNumber(JTextField field) {
        String phone = field.getText().trim();
        String phoneRegex = "^\\+?[0-9]{7,15}$";
        if (!phone.matches(phoneRegex)) {
            showError("Invalid phone number.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    // Check for specific input length
    public static boolean hasValidLength(JTextField field, int min, int max, String fieldName) {
        int length = field.getText().trim().length();
        if (length < min || length > max) {
            showError(fieldName + " must be between " + min + " and " + max + " characters.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    // Match against a custom regex pattern
    public static boolean matchesPattern(JTextField field, String pattern, String errorMessage) {
        if (!field.getText().trim().matches(pattern)) {
            showError(errorMessage);
            field.requestFocus();
            return false;
        }
        return true;
    }

    // Check if two fields match (e.g. password and confirm password)
    public static boolean fieldsMatch(JTextField field1, JTextField field2, String errorMessage) {
        if (!field1.getText().trim().equals(field2.getText().trim())) {
            showError(errorMessage);
            field2.requestFocus();
            return false;
        }
        return true;
    }

    // Show error message
    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
}
