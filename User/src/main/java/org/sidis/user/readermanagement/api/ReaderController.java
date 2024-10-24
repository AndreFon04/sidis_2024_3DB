package org.sidis.user.readermanagement.api;

import com.example.library.bookmanagement.api.BookView;
import com.example.library.bookmanagement.api.BookViewMapper;
import com.example.library.bookmanagement.model.Book;
import com.example.library.bookmanagement.services.BookServiceImpl;
import com.example.library.exceptions.NotFoundException;
import com.example.library.filestorage.api.UploadFileResponse;
import com.example.library.filestorage.services.FileStorageService;
import com.example.library.readermanagement.model.Reader;
import com.example.library.readermanagement.services.EditReaderRequest;
import com.example.library.readermanagement.services.ReaderServiceImpl;
import com.example.library.readermanagement.services.SearchReadersQuery;
import com.example.library.usermanagement.api.ListResponse;
import com.example.library.usermanagement.services.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Tag(name = "Readers", description = "Endpoints for managing readers")
@RestController
@RequestMapping("/api/readers")
class ReaderController {

	private static final String IF_MATCH = "If-Match";

	private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

	private final ReaderServiceImpl readerService;
	private final ReaderViewMapper readerMapper;
	private final FileStorageService fileStorageService;
	private final BookViewMapper bookMapper;
	private final BookServiceImpl bookService;

	@Autowired
	public ReaderController(ReaderServiceImpl readerService, ReaderViewMapper readerMapper, FileStorageService fileStorageService,
							BookViewMapper bookMapper, BookServiceImpl bookService) {
		this.readerService = readerService;
		this.readerMapper = readerMapper;
		this.fileStorageService = fileStorageService;
		this.bookMapper = bookMapper;
		this.bookService = bookService;
	}

	@Operation(summary = "Gets all readers")
	@ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = ReaderView.class))) })
	@GetMapping
	public Iterable<ReaderView> findAll() {
		return readerMapper.toReaderView(readerService.findAll());
	}

	@Operation(summary = "Gets a specific Reader by id")
	@GetMapping(value = "/id/{id1}/{id2}")
	public ResponseEntity<ReaderView> findById(
			@PathVariable("id1") @Parameter(description = "The id of the reader to find") final String id1,
			@PathVariable("id2") final String id2) {
		System.out.println("apiRdId");
		String readerID = id1 + "/" + id2;
		final var reader = readerService.getReaderByID(readerID).orElseThrow(() -> new NotFoundException(Reader.class, readerID));

		return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerMapper.toReaderView(reader));
	}

	@Operation(summary = "Gets a specific Reader by email")
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<ReaderView> findByEmail(
			@PathVariable("email") @Parameter(description = "The email of the reader to find") final String email) {
		System.out.println("apiRdEmail");
		Reader reader = readerService.getReaderByEmail(email).orElseThrow(() -> new NotFoundException(Reader.class, email));

		return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerMapper.toReaderView(reader));
	}

	@Operation(summary = "Gets the Top 5 Readers")
	@GetMapping(value = "/top5")
	public List<ReaderView> findTop5() {
		return readerMapper.toReaderView(readerService.getTop5Readers());
	}

	@Operation(summary = "Gets book suggestions based on the list of interests of the Reader")
	@GetMapping(value = "/suggestions/{id1}/{id2}")
	public List<BookView> findSuggestions(@PathVariable("id1") final String id1, @PathVariable("id2") final String id2) {
		String readerID = id1 + "/" + id2;
		Reader reader = readerService.getReaderByID(readerID).orElseThrow(() -> new NotFoundException(Reader.class, readerID));

		Set<String> interests = readerService.getInterestsByReader(reader);

		List<Book> books = new ArrayList<>();
		for (String genre : interests) {
			books.addAll(bookService.getBookByGenre(genre));
		}

		return bookMapper.toBookView(books);
	}


	private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
		if (ifMatchHeader.startsWith("\"")) {
			return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
		}
		return Long.parseLong(ifMatchHeader);
	}


	@Operation(summary = "Partially updates an existing reader")
	@PatchMapping(value = "/{id1}/{id2}")
	public ResponseEntity<ReaderView> partialUpdate(final WebRequest request,
				@PathVariable("id1") final String id1, @PathVariable("id2") final String id2,
				@Valid @RequestBody final EditReaderRequest resource) {
		String readerID = id1 + "/" + id2;

		// Validar se o user autenticado tem o mesmo readerID que o readerID acima, se nao, é FORBIDDEN
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		currentUsername = currentUsername.replaceFirst(".*,", ""); //PREGO

		Reader r = readerService.getReaderByID(readerID).orElseThrow(() -> new NotFoundException(Reader.class, readerID));

		System.out.println(readerID + " " + currentUsername);
		System.out.println(r.getReaderID() + " " + r.getEmail());

		if (!currentUsername.equals(r.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this object.");
		}
		// Fim de validação

		final String ifMatchValue = request.getHeader(IF_MATCH);
		if (ifMatchValue == null || ifMatchValue.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must issue a conditional PATCH using 'if-match'");
		}

		final var reader = readerService.partialUpdate(readerID, resource, getVersionFromIfMatchHeader(ifMatchValue));
		return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerMapper.toReaderView(reader));
	}

	@Operation(summary = "Searches any existing Reader by name, email or phone number")
	@PostMapping("search")
	public ListResponse<ReaderView> search(@RequestBody final SearchRequest<SearchReadersQuery> request) {
		final List<Reader> searchReaders = readerService.searchReaders(request.getPage(), request.getQuery());
		return new ListResponse<>(readerMapper.toReaderView(searchReaders));
	}

	@Operation(summary = "Uploads a photo of a reader")
	@PostMapping("/{id1}/{id2}/photo")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UploadFileResponse> uploadFile(
			@PathVariable("id1") final String id1, @PathVariable("id2") final String id2,
			@RequestParam("file") final MultipartFile file) throws URISyntaxException {
		String readerIDfile = id1 + "_" + id2;

		// Validar o user autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		currentUsername = currentUsername.replaceFirst(".*,", ""); //PREGO

		Reader r = readerService.getReaderByID(readerIDfile).orElseThrow(() -> new NotFoundException(Reader.class, readerIDfile));

		System.out.println(readerIDfile + " " + currentUsername);
		System.out.println(r.getReaderID() + " " + r.getEmail());

		if (!currentUsername.equals(r.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this object.");
		}
		// Fim de validação

		final UploadFileResponse up = doUploadFile(readerIDfile, file);
		return ResponseEntity.created(new URI(up.getFileDownloadUri())).body(up);
	}

	public UploadFileResponse doUploadFile(final String readerID, final MultipartFile file) {

		final String fileName = fileStorageService.storeFile(readerID, file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(fileName)
				.toUriString();
		// since we are reusing this method both for single file upload and multiple
		// file upload and have different urls we need to make sure we always return the
		// right url for the single file download
		fileDownloadUri = fileDownloadUri.replace("/photos/", "/photo/");

		// TODO save info of the file on the database

		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@Operation(summary = "Downloads a photo of a reader")
	@GetMapping("/{id}/photo/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable final String fileName,
												 final HttpServletRequest request) {
		// Load file as Resource
		final Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (final IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
