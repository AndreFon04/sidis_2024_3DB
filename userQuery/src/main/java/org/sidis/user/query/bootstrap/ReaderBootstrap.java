package org.sidis.user.query.bootstrap;

import org.sidis.user.query.model.Reader;
import org.sidis.user.query.model.Role;
import org.sidis.user.query.model.User;
import org.sidis.user.query.repositories.ReaderRepository;
import org.sidis.user.query.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
public class ReaderBootstrap implements CommandLineRunner {

    private final ReaderRepository readerRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public ReaderBootstrap(ReaderRepository readerRepo, UserRepository userRepo, PasswordEncoder encoder) {
        this.readerRepo = readerRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        System.out.println("ReaderBootstrapper running...");

        final Optional<Reader> r = readerRepo.findTopByOrderByReaderIDDesc();
        if (r.isPresent()) {
            System.out.println("Last readerID is " + r.get().getReaderID());
            r.get().initCounter(r.get().getReaderID());
        }

        System.out.println(readerRepo.findByEmail("josefinoDasCOUBES@email.com").isEmpty());
        if (readerRepo.findByEmail("josefinoDasCOUBES@email.com").isEmpty()) {
            System.out.println("rdBt.r1");
            final Reader r1 = new Reader("Josefino Amigalhaço das Coubes", encoder.encode("Password1"), "josefinoDasCOUBES@email.com", "1991-1-1", "946325614", true);
            readerRepo.save(r1);
            System.out.println("rdBt.r1.save");
        }

        addReaderIndividually();
        System.out.println("ReaderBootstrapper finished.");
    }

    private void addReaderIndividually() {
        addReader("Josefino Amigalhaço das Coubes", encoder.encode("Password1"), "josefinoDasCOUBES@email.com", "1991-01-01", "946325614", true);
        addReader("Josefino Amigalhaço das Batatas", encoder.encode("Password2"), "josefinoDasBATATAS@email.com", "1991-08-02", "123456789", true);
        addReader("Joao Gomes", encoder.encode("Password3"), "joaogomes@mail.com", "2000-01-02", "123456789", true);
        addReader("Guilherme Vilas-Boas", encoder.encode("Password4"), "guivilasboas@mail.com", "1652-01-06", "123456789", true);
        addReader("Francisco", encoder.encode("Password5"), "francisco77@mail.com", "1956-04-08", "123456789", true);
        addReader("Gui", encoder.encode("Password6"), "guizalha85@mail.com", "1977-07-07", "123456789", true);
        addReader("Maria Silva", encoder.encode("Password7"), "maria.silva@email.com", "1985-10-10", "987654321", true);
        addReader("Ana Pereira", encoder.encode("Password8"), "ana.pereira@email.com", "1992-05-14", "912345678", true);
        addReader("Pedro Costa", encoder.encode("Password9"), "pedro.costa@email.com", "1975-11-23", "923456789", true);
        addReader("Luis Mendes", encoder.encode("Password10"), "luis.mendes@email.com", "1988-02-20", "934567890", true);
        addReader("Carla Ferreira", encoder.encode("Password11"), "carla.ferreira@email.com", "1990-12-12", "945678901", true);
        addReader("Rui Santos", encoder.encode("Password12"), "rui.santos@email.com", "1993-06-15", "956789012", true);
        addReader("Isabel Oliveira", encoder.encode("Password13"), "isabel.oliveira@email.com", "1982-09-18", "967890123", true);
        addReader("Miguel Rodrigues", encoder.encode("Password14"), "miguel.rodrigues@email.com", "1978-07-30", "978901234", true);
        addReader("Sofia Almeida", encoder.encode("Password15"), "sofia.almeida@email.com", "1995-03-22", "989012345", true);

        addReader("Joana Marques", encoder.encode("Password16"), "joana.marques@email.com", "1987-12-12", "912345671", true);
        addReader("Tiago Santos", encoder.encode("Password17"), "tiago.santos@email.com", "1990-05-18", "923456782", true);
        addReader("Mariana Nogueira", encoder.encode("Password18"), "mariana.nogueira@email.com", "1985-08-30", "934567893", true);
        addReader("Ricardo Alves", encoder.encode("Password19"), "ricardo.alves@email.com", "1993-03-05", "945678904", true);
        addReader("Fernanda Rocha", encoder.encode("Password20"), "fernanda.rocha@email.com", "1984-06-07", "956789015", true);
        addReader("Paula Costa", encoder.encode("Password21"), "paula.costa@email.com", "1996-11-28", "967890126", true);
        addReader("Nuno Oliveira", encoder.encode("Password22"), "nuno.oliveira@email.com", "1975-01-17", "978901237", true);
        addReader("Carolina Ferreira", encoder.encode("Password23"), "carolina.ferreira@email.com", "1980-04-22", "989012348", true);
        addReader("Bruno Ribeiro", encoder.encode("Password24"), "bruno.ribeiro@email.com", "1983-02-14", "912345679", true);
        addReader("Daniela Sousa", encoder.encode("Password25"), "daniela.sousa@email.com", "1991-07-26", "923456780", true);
        addReader("António Monteiro", encoder.encode("Password26"), "antonio.monteiro@email.com", "1986-09-03", "934567891", true);
        addReader("Patrícia Gonçalves", encoder.encode("Password27"), "patricia.goncalves@email.com", "1978-03-08", "945678902", true);
        addReader("Vitor Mendes", encoder.encode("Password28"), "vitor.mendes@email.com", "1981-12-10", "956789013", true);
        addReader("Sandra Pinto", encoder.encode("Password29"), "sandra.pinto@email.com", "1994-08-06", "967890124", true);
        addReader("Diogo Lima", encoder.encode("Password30"), "diogo.lima@email.com", "1977-05-09", "978901235", true);
        addReader("Catarina Martins", encoder.encode("Password31"), "catarina.martins@email.com", "1989-01-22", "989012346", true);
        addReader("José Matos", encoder.encode("Password32"), "jose.matos@email.com", "1983-11-05", "912345678", true);
        addReader("Helena Sousa", encoder.encode("Password33"), "helena.sousa@email.com", "1980-10-17", "923456789", true);
        addReader("André Carvalho", encoder.encode("Password34"), "andre.carvalho@email.com", "1987-06-19", "934567890", true);
        addReader("Marta Cruz", encoder.encode("Password35"), "marta.cruz@email.com", "1992-07-21", "945678901", true);
        addReader("Rafael Silva", encoder.encode("Password36"), "rafael.silva@email.com", "1985-02-24", "956789012", true);
        addReader("Diana Costa", encoder.encode("Password37"), "diana.costa@email.com", "1993-08-29", "967890123", true);
        addReader("Fernando Pereira", encoder.encode("Password38"), "fernando.pereira@email.com", "1984-09-01", "978901234", true);
        addReader("Inês Rocha", encoder.encode("Password39"), "ines.rocha@email.com", "1990-12-15", "989012345", true);
        addReader("Gustavo Almeida", encoder.encode("Password40"), "gustavo.almeida@email.com", "1976-10-12", "912345676", true);
        addReader("Tânia Mendes", encoder.encode("Password41"), "tania.mendes@email.com", "1982-11-27", "923456789", true);
        addReader("Fábio Nogueira", encoder.encode("Password42"), "fabio.nogueira@email.com", "1989-06-18", "934567890", true);
        addReader("Sónia Lima", encoder.encode("Password43"), "sonia.lima@email.com", "1995-03-23", "945678901", true);
        addReader("Alexandre Ribeiro", encoder.encode("Password44"), "alexandre.ribeiro@email.com", "1983-05-14", "956789012", true);
        addReader("Carla Santos", encoder.encode("Password45"), "carla.santos@email.com", "1980-07-30", "967890123", true);
        addReader("Eduardo Rodrigues", encoder.encode("Password46"), "eduardo.rodrigues@email.com", "1988-02-11", "978901234", true);
        addReader("Verónica Silva", encoder.encode("Password47"), "veronica.silva@email.com", "1991-08-03", "989012345", true);
        addReader("Nélson Costa", encoder.encode("Password48"), "nelson.costa@email.com", "1977-04-16", "912345674", true);
        addReader("Susana Martins", encoder.encode("Password49"), "susana.martins@email.com", "1984-10-29", "923456789", true);
        addReader("João Almeida", encoder.encode("Password50"), "joao.almeida@email.com", "1981-09-07", "934567890", true);
        addReader("Patrícia Ribeiro", encoder.encode("Password51"), "patricia.ribeiro@email.com", "1990-01-18", "945678901", true);
        addReader("Carlos Ferreira", encoder.encode("Password52"), "carlos.ferreira@email.com", "1987-11-30", "956789012", true);
        addReader("Raquel Sousa", encoder.encode("Password53"), "raquel.sousa@email.com", "1975-05-05", "967890123", true);
        addReader("Hugo Gomes", encoder.encode("Password54"), "hugo.gomes@email.com", "1994-07-09", "978901234", true);
        addReader("Margarida Silva", encoder.encode("Password55"), "margarida.silva@email.com", "1988-06-28", "989012345", true);
        addReader("Rodrigo Rocha", encoder.encode("Password56"), "rodrigo.rocha@email.com", "1985-03-12", "912345672", true);
        addReader("Andreia Costa", encoder.encode("Password57"), "andreia.costa@email.com", "1976-02-17", "923456789", true);
        addReader("Francisco Nogueira", encoder.encode("Password58"), "francisco.nogueira@email.com", "1992-10-13", "934567890", true);
        addReader("Lara Pereira", encoder.encode("Password59"), "lara.pereira@email.com", "1983-04-24", "945678901", true);
        addReader("Márcio Almeida", encoder.encode("Password60"), "marcio.almeida@email.com", "1991-01-06", "956789012", true);
    }

    private void addReader(final String name, final String password, final String email, final String birthdate, final String phoneNumber, final boolean GDPR) {
        if (readerRepo.findByEmail(email).isEmpty() && userRepo.findByUsername(email).isEmpty()) {
            Reader reader = new Reader(name, password, email, birthdate, phoneNumber, GDPR);
            readerRepo.save(reader);

            final User user = new User(email, password);
            user.setFullName(name);
            user.addAuthority(new Role(Role.READER));
            userRepo.save(user);
        }
    }
}
