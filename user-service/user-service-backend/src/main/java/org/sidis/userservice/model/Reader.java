package org.sidis.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk; // database primary key

    @Version
    private long version;

    @Column(unique = false, updatable = true, nullable = false)
    @NotNull
    @NotBlank
    @Size(max=150)
    private String name;

    @Column(unique = false, updatable = true, nullable = false)
    @NotNull
    @NotBlank
    private String password;

    @Column(unique = true, updatable = false, nullable = false)
    private String readerID;

    @Column(unique = false, updatable = true, nullable = false)
    @Email
    @NotNull
    @NotBlank
    private String email; // serves as username

    @Column(unique = false, updatable = true, nullable = false)
    @NotNull
    @NotBlank
    private String birthdate;

    @Column(unique = false, updatable = true, nullable = false)
    @NotNull
    @NotBlank
    @Pattern(regexp = "[1-9][0-9]{8}")
    private String phoneNumber;

    @Column(unique = false, updatable = true, nullable = false)
    @NotNull
    private boolean GDPR;

    @ElementCollection
    private final Set<String> interests = new HashSet<>();

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "readerimage_id")
    private ReaderImage readerImage;

    private static final long serialVersionUID = 1L;

    // auditing info
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    // auditing info
    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    private LocalDateTime modifiedAt;

    // auditing info
    @CreatedBy
    @Column(nullable = false, updatable = false)
    @Getter
    private String createdBy;

    // auditing info
    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy;

    @Setter
    @Getter
    private boolean enabled = true;

    public Reader() {
    }

    private static int currentYear = Year.now().getValue();
    private static int counter = 0;


    public void initCounter(String lastReaderID) {
        if (lastReaderID != null && !lastReaderID.isBlank()) {
            // Split the lastReaderID into year and counter
            String[] parts = lastReaderID.split("/");
            if (parts.length == 2) {
                currentYear = Integer.parseInt(parts[0]);
                counter = Integer.parseInt(parts[1]);
            }
        }
    }

    private String generateUniqueReaderID() {
        if (Year.now().getValue() != currentYear) {
            currentYear = Year.now().getValue();
            counter = 0;
        }

        counter++;
        String idCounter = String.format("%d", counter);
        return currentYear + "/" + idCounter;
    }

    public Reader(final String name, final String password, final String email, final String birthdate,
                  final String phoneNumber, final boolean GDPR) {
        this.name = name;
        setPassword(password);
        this.readerID = generateUniqueReaderID();
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.GDPR = GDPR;
//		this.createdAt = this.modifiedAt = LocalDateTime.now();
//		this.createdBy = this.modifiedBy = "xxx";
    }

    public String getReaderID() { return readerID; }
    public void setReaderID(String readerID) { this.readerID = readerID; }
    public void setUniqueReaderID() {
        this.readerID = generateUniqueReaderID();
    }

    public String getName() { return name; }
    public void setName(final String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be null, nor blank");
        this.name = name;
    }

    public String getPassword() { return password; }
    public void setPassword(final String password) {
        this.password = Objects.requireNonNull(password);
    }

    public String getEmail() { return email; }
    public void setEmail(final String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be null, nor blank");
        this.email = email;
    }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(final String birthdate) {
        if (birthdate == null) throw new IllegalArgumentException("Birthdate cannot be null");
        if (!birthdate.isBlank()) {
            // Split the birthdate into day, month and year (YYYY-MM-DD)
            String[] parts = birthdate.split("-");
            if (parts.length != 3) throw new IllegalArgumentException("Birthdate must be in the format YYYY-MM-DD");

            try {
                int birthdateDay = Integer.parseInt(parts[2]);
                int birthdateMonth = Integer.parseInt(parts[1]);
                int birthdateYear = Integer.parseInt(parts[0]);

                if (birthdateYear <= 0) throw new IllegalArgumentException("Year must be positive");
                if (birthdateMonth < 1 || birthdateMonth > 12) throw new IllegalArgumentException("Month must be between 1 and 12");
                if (birthdateDay < 1 || birthdateDay > 31) throw new IllegalArgumentException("Day must be between 1 and 31");
                // Check if the day is valid for the given month
                if ((birthdateMonth == 4 || birthdateMonth == 6 || birthdateMonth == 9 || birthdateMonth == 11) && birthdateDay > 30) {
                    throw new IllegalArgumentException("Day must be between 1 and 30 for the given month");
                }
                if (birthdateMonth == 2) {
                    boolean isLeapYear = (birthdateYear % 4 == 0 && birthdateYear % 100 != 0) || (birthdateYear % 400 == 0);
                    int maxDayInFebruary = isLeapYear ? 29 : 28;
                    if (birthdateDay > maxDayInFebruary) {
                        throw new IllegalArgumentException("Day must be between 1 and " + maxDayInFebruary + " for February");
                    }
                }
                if (LocalDate.of(birthdateYear, birthdateMonth, birthdateDay).isAfter(LocalDate.now().minusYears(12))) {
                    throw new IllegalArgumentException("Minimum age is 12");  }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Birthdate must contain valid integers for day, month, and year", e);
            }
        }
        this.birthdate = birthdate;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(final String phoneNumber) {
        if (phoneNumber == null) throw new IllegalArgumentException("Phone number cannot be null");
        this.phoneNumber = phoneNumber;
    }

    public boolean getGDPR() { return GDPR; }
    public void setGDPR(final boolean GDPR) {
        if (!GDPR) throw new IllegalArgumentException("GDPR consent cannot be false!");
        this.GDPR = true;
    }

    public Long getVersion() { return version; }

    public void applyPatch(final long desiredVersion, final String name, final String password, final String email,
                           final String birthdate, final String phoneNumber, final boolean GDPR, final Set<String> interests) {

        if (this.version != desiredVersion) throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        if (name != null) setName(name);
        if (password != null) setPassword(password);
        if (email != null) setEmail(email);
        if (birthdate != null) setBirthdate(birthdate);
        if (phoneNumber != null) setPhoneNumber(phoneNumber);
        setGDPR(GDPR);
        addInterests(interests);
    }

    public void addInterests(final Set<String> i) {
        interests.addAll(i);
    }

    public void setInterests(final Set<String> i) {
        interests.clear();
        interests.addAll(i);
    }

    public Set<String> getInterests() {
        return interests;
    }
}
