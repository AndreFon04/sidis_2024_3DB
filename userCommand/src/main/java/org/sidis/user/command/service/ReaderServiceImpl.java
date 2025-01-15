package org.sidis.user.command.service;

import jakarta.validation.ValidationException;
import org.sidis.user.command.client.*;
import org.sidis.user.command.exceptions.ConflictException;
import org.sidis.user.command.exceptions.NotFoundException;
import org.sidis.user.command.message_broker.MessagePublisher;
import org.sidis.user.command.model.Reader;
import org.sidis.user.command.model.ReaderDTO;
import org.sidis.user.command.model.User;
import org.sidis.user.command.repositories.ReaderRepository;
import org.sidis.user.command.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final UserRepository userRepository;
    private final EditReaderMapper editReaderMapper;
    private final EditUserMapper editUserMapper;
    private final PasswordEncoder passwordEncoder;
//    private final ReaderServiceClient readerServiceClient;
    private final MessagePublisher sender;


    public ReaderServiceImpl(ReaderRepository readerRepository, UserRepository userRepository, EditReaderMapper editReaderMapper, EditUserMapper editUserMapper,
                             PasswordEncoder passwordEncoder, ReaderServiceClient readerServiceClient, MessagePublisher sender) {
        this.readerRepository = readerRepository;
        this.userRepository = userRepository;
        this.editReaderMapper = editReaderMapper;
        this.editUserMapper = editUserMapper;
        this.passwordEncoder = passwordEncoder;
//        this.readerServiceClient = readerServiceClient;
        this.sender = sender;
    }

    @Override
    public Reader create(CreateReaderRequest request) {
        System.out.println("CREATE: request email: " + request.getEmail() + " name: " + request.getName());
        //if (readerRepository.findByEmail(request.getEmail()).isPresent()) {
        final Optional<Reader> a = readerRepository.findByEmail(request.getEmail());
        if (a.isPresent()) {
            System.out.println("CREATE: " + a.get().getName() + " " + a.get().getEmail());
            throw new ConflictException("Email already exists! Cannot create a new reader.");
        }
        if (userRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists! Cannot create a new user.");
        }
        String p = request.getPassword();
        if (!(p.matches(".*[a-z].*") && p.matches(".*[A-Z].*") && p.matches(".*[0-9!@#$%&*()_+=|<>?{}\\[\\]~./-].*"))) {
            throw new ValidationException("Password must contain 1 lower and 1 upper case letter and 1 digit or special character!");
        }
        if (!p.equals(request.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }
        validateBirthdate(request.getBirthdate());

        final Reader reader = editReaderMapper.create(request);

        CreateUserRequest userRequest = new CreateUserRequest(request.getEmail(), request.getName(), request.getPassword());
        Set<String> authorities = new HashSet<>();
        authorities.add("READER");
        userRequest.setAuthorities(authorities);
        final User user = editUserMapper.create(userRequest);

        reader.setUniqueReaderID();

        reader.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        System.out.println("CREATE: reader: " + reader.getName() + " " + reader.getEmail() + " " + reader.getReaderID());

        readerRepository.save(reader);
        userRepository.save(user);

        //sender.publishUserCreated(user);

        ReaderDTO readerDTO = new ReaderDTO(reader.getName(), reader.getEmail(), reader.getBirthdate(), reader.getPhoneNumber(), reader.getReaderID());
        sender.publishReaderCreated(readerDTO);

//        readerServiceClient.saveReader(reader);
//        readerServiceClient.saveUser(user);

        return reader;
    }

    public Reader partialUpdate(final String readerID, final EditReaderRequest request, final long desiredVersion) {
        final var reader = readerRepository.findByReaderID(readerID)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));

        if (request.getBirthdate() != null) {
            validateBirthdate(request.getBirthdate());
        }

        reader.applyPatch(desiredVersion, request.getName(), null, request.getEmail(), request.getBirthdate(),
                request.getPhoneNumber(), request.isGDPR(), request.getInterests());

        readerRepository.save(reader);
        ReaderDTO readerDTO = new ReaderDTO(reader.getName(), reader.getEmail(), reader.getBirthdate(), reader.getPhoneNumber(), reader.getReaderID());
        sender.publishReaderUpdated(readerDTO);

        return reader;
    }

    @Override
    public Optional<Reader> getReaderByID(final String readerID) {
        return readerRepository.findByReaderID(readerID);
    }


    public List<Reader> searchReaders(Page page, SearchReadersQuery query) {
        if (page == null) {
            page = new Page(1, 10);
        }
        if (query == null) {
            query = new SearchReadersQuery("", "", "");
        }
        return readerRepository.searchReaders(page, query);
    }

    private void validateBirthdate(final String birthdate) {
        if (birthdate == null) throw new IllegalArgumentException("Birthdate cannot be null");
        if (!birthdate.isBlank()) {
            String[] parts = birthdate.split("-");
            if (parts.length != 3) throw new IllegalArgumentException("Birthdate must be in the format YYYY-MM-DD");

            try {
                int birthdateDay = Integer.parseInt(parts[2]);
                int birthdateMonth = Integer.parseInt(parts[1]);
                int birthdateYear = Integer.parseInt(parts[0]);

                if (birthdateYear <= 0) throw new IllegalArgumentException("Year must be positive");
                if (birthdateMonth < 1 || birthdateMonth > 12) throw new IllegalArgumentException("Month must be between 1 and 12");
                if (birthdateDay < 1 || birthdateDay > 31) throw new IllegalArgumentException("Day must be between 1 and 31 for the given month");

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
                    throw new IllegalArgumentException("Minimum age is 12");
                }

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Birthdate must contain valid integers for day, month, and year", e);
            }
        }
    }
}
