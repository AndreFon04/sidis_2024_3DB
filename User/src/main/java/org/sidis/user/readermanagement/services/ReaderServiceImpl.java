package org.sidis.user.readermanagement.services;

import com.example.library.bookmanagement.model.Genre;
import com.example.library.bookmanagement.repositories.GenreRepository;
import com.example.library.exceptions.ConflictException;
import com.example.library.exceptions.NotFoundException;
import com.example.library.readermanagement.model.Reader;
import com.example.library.readermanagement.repositories.ReaderRepository;
import com.example.library.usermanagement.model.User;
import com.example.library.usermanagement.repositories.UserRepository;
import com.example.library.usermanagement.services.CreateUserRequest;
import com.example.library.usermanagement.services.EditUserMapper;
import com.example.library.usermanagement.services.Page;
import jakarta.validation.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final UserRepository userRepository;
    private final EditReaderMapper editReaderMapper;
    private final EditUserMapper editUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final GenreRepository genreRepository;

    public ReaderServiceImpl(ReaderRepository readerRepository, UserRepository userRepository, EditUserMapper editUserMapper, EditReaderMapper editReaderMapper, PasswordEncoder passwordEncoder, GenreRepository genreRepository) {
        this.readerRepository = readerRepository;
        this.userRepository = userRepository;
        this.editReaderMapper = editReaderMapper;
        this.editUserMapper = editUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    @Override
    public Reader create(CreateReaderRequest request) {
        if (readerRepository.findByEmail(request.getEmail()).isPresent()) {
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

        // Validar se a(s) string(s) em interests equivalem a Genre
        Set<Genre> g = new HashSet<>();
        for (Genre genre : reader.getInterests()) {
            Genre h = genreRepository.findByInterest(genre.getInterest());
            if (h != null) g.add(h); //PREGO
        }
        reader.setInterests(g);
        // Fim de validação

        CreateUserRequest userRequest = new CreateUserRequest(request.getEmail(), request.getName(), request.getPassword());
        Set<String> authorities = new HashSet<>();
        authorities.add("READER");
        userRequest.setAuthorities(authorities);
        final User user = editUserMapper.create(userRequest);

        reader.setUniqueReaderID();

        reader.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        readerRepository.save(reader);
        userRepository.save(user);

        return reader;
    }

    public Reader partialUpdate(final String readerID, final EditReaderRequest request, final long desiredVersion) {
        final var reader = readerRepository.findByReaderID(readerID).orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));

        if (request.getBirthdate() != null) {
            validateBirthdate(request.getBirthdate());
        }
        if (request.getPassword() != null) {
            String p = request.getPassword();
            if (!(p.matches(".*[a-z].*") && p.matches(".*[A-Z].*") && p.matches(".*[0-9!@#$%&*()_+=|<>?{}\\[\\]~./-].*"))) {
                throw new ValidationException("Password must contain 1 lower and 1 upper case letter and 1 digit or special character!");
            }
            if (request.getRePassword() == null || !request.getPassword().equals(request.getRePassword())) {
                throw new ValidationException("Passwords don't match!");
            }
        } else if (request.getRePassword() != null) throw new ValidationException("Passwords don't match!");

        final var user = userRepository.findByUsername(reader.getEmail()).orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));


        // Update reader details
        reader.applyPatch(desiredVersion, request.getName(), request.getPassword(), request.getEmail(), request.getBirthdate(),
                request.getPhoneNumber(), request.isGDPR(), stringToGenre(request.getInterests()));

        if (request.getPassword() != null || request.getEmail() != null) {
            user.applyPatch(request.getEmail(), request.getPassword());
        }

        if(request.getPassword() != null) {
            reader.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // ---
//        Set<Genre> g = new HashSet<>();
//        for (Genre genre : reader.getInterests()) {
//            Genre h = genreRepository.findByInterest(genre.getInterest());
//            if (h != null) g.add(h);
//        }
//        reader.setInterests(g);
        // ---


        readerRepository.save(reader);
        userRepository.save(user);

        return reader;
    }

    @Override
    public List<Reader> getTop5Readers() {
        List<Reader> reader = readerRepository.findTop5Readers();
        System.out.println("Size: " + reader.size());
        return reader;
    }

    @Override
    public Optional<Reader> getReaderByID(final String readerID) {
        return readerRepository.findByReaderID(readerID);
    }

    @Override
    public Optional<Reader> getReaderByEmail(final String email) {
        return readerRepository.findByEmail(email);
    }

    @Override
    public List<Reader> getReaderByName(final String name) {
        return readerRepository.findByName(name);
    }

    @Override
    public Set<String> getInterestsByReader(Reader reader) {
        return reader.getInterests().stream().map(Genre::getInterest).collect(toSet());
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

    protected Set<Genre> stringToGenre(final Set<String> interests) {
        if (interests != null) {
            Set<Genre> g = new HashSet<>();
            for (String i : interests) {
                Genre h = genreRepository.findByInterest(i);
                if (h != null) g.add(h);
            }
            return g;
        }
        return new HashSet<>();
    }

    private void validateBirthdate(final String birthdate) {
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
    }
}
