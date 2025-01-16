package org.sidis.suggestion.query.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;


@Entity
//@EntityListeners(AuditingEntityListener.class)
public class ReaderS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk; // database primary key

    @Version
    private long version;

    @Column(unique = false, updatable = true, nullable = false)
//    @NotNull
//    @NotBlank
//    @Size(max=150)
    @Getter
    @Setter
    private String name;

    @Column(unique = false, updatable = true, nullable = false)
//    @NotNull
//    @NotBlank
    @Getter
    @Setter
    private String password;

    @Column(unique = true, updatable = false, nullable = false)
    @Getter
    @Setter
    private String readerID;

    @Column(unique = false, updatable = true, nullable = false)
//    @Email
//    @NotNull
//    @NotBlank
    @Getter
    @Setter
    private String email; // serves as username

    @Column(unique = false, updatable = true, nullable = false)
//    @NotNull
//    @NotBlank
    @Getter
    @Setter
    private String birthdate;

    @Column(unique = false, updatable = true, nullable = false)
//    @NotNull
//    @NotBlank
//    @Pattern(regexp = "[1-9][0-9]{8}")
    @Getter
    @Setter
    private String phoneNumber;

    @Column(unique = false, updatable = true, nullable = false)
//    @NotNull
    @Getter
    @Setter
    private boolean GDPR;

    @ElementCollection
    private final Set<String> interests = new HashSet<>();

    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    private boolean enabled = true;

    public ReaderS() {
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

    public ReaderS(final String name, final String password, final String email, final String birthdate,
                   final String phoneNumber, final boolean GDPR) {
        this.name = name;
        this.password = password;
        this.readerID = generateUniqueReaderID();
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.GDPR = GDPR;
//		this.createdAt = this.modifiedAt = LocalDateTime.now();
//		this.createdBy = this.modifiedBy = "xxx";
    }

    public void addInterests(final Set<String> i) {
        interests.addAll(i);
    }

    public void setInterests(final Set<String> i) {
        interests.clear();
        interests.addAll(i);
    }
}
