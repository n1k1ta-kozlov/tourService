package domain;

import common.BusinessException;
import java.util.UUID;
import java.util.regex.Pattern;

public class Customer {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{9,14}$");

    private final UUID customerId;
    private String name;
    private String emailAddress;
    private String contactNumber;
    private String documentId;
    private int bonusPoints;

    public Customer(String name, String emailAddress, String contactNumber, String documentId, int bonusPoints) {
        validateName(name);
        validateEmail(emailAddress);
        validatePhone(contactNumber);
        validateDocument(documentId);
        validatePoints(bonusPoints);

        this.customerId = UUID.randomUUID();
        this.name = name.trim();
        this.emailAddress = emailAddress;
        this.contactNumber = contactNumber;
        this.documentId = documentId;
        this.bonusPoints = bonusPoints;
    }

    private void validateName(String name) {
        if (name == null || name.trim().split("\\s+").length < 2) {
            throw new BusinessException("Имя должно состоять как минимум из двух слов.");
        }
        for (String part : name.trim().split("\\s+")) {
            if (part.length() < 2) {
                throw new BusinessException("Каждая часть имени должна состоять как минимум из 2 символов.");
            }
        }
    }

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException("Неверный формат электронного письма");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new BusinessException("Номер телефона должен начинаться с символа «+» и содержать от 9 до 14 цифр.");
        }
    }

    private void validateDocument(String document) {
        if (document == null || document.length() != 9) {
            throw new BusinessException("Идентификатор документа должен состоять ровно из 9 символов.");
        }
    }

    private void validatePoints(int points) {
        if (points < 0) {
            throw new BusinessException("Бонусные баллы не могут быть отрицательными.");
        }
    }

    public UUID getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmailAddress() { return emailAddress; }
    public String getContactNumber() { return contactNumber; }
    public String getDocumentId() { return documentId; }
    public int getBonusPoints() { return bonusPoints; }

    public void addBonusPoints(int points) {
        if (points <= 0) throw new BusinessException("Дополнительные пункты должны быть положительными.");
        this.bonusPoints += points;
    }

    public double getDiscountRate() {
        if (bonusPoints >= 4000) return 0.18;
        if (bonusPoints >= 800) return 0.12;
        if (bonusPoints >= 300) return 0.07;
        if (bonusPoints >= 50) return 0.03;
        return 0.0;
    }

    public String getMaskedDocument() {
        String prefix = "******";
        return prefix + documentId.substring(prefix.length());
    }

    @Override
    public String toString() {
        return String.format("Клиент[id=%s, имя=%s, email=%s, телефон=%s, баллы=%d]",
                customerId, name, emailAddress, contactNumber, bonusPoints);
    }
}