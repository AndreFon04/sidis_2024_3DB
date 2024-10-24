package org.sidis.booksauthors.authormanagement.api;

import com.example.library.authormanagement.model.Author;
import com.example.library.authormanagement.model.AuthorImage;
import com.example.library.authormanagement.model.CoAuthorDTO;
import com.example.library.authormanagement.services.AuthorServiceImpl;
import com.example.library.authormanagement.services.CreateAuthorRequest;
import com.example.library.authormanagement.services.EditAuthorRequest;
import com.example.library.bookmanagement.api.BookViewMapper;
import com.example.library.bookmanagement.services.BookServiceImpl;
import com.example.library.exceptions.NotFoundException;
import com.example.library.filestorage.api.UploadFileResponse;
import com.example.library.filestorage.services.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


@Tag(name = "Authors", description = "Endpoints for managing Authors")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

	private static final String IF_MATCH = "If-Match";

	private final AuthorServiceImpl authorService;
	private final BookServiceImpl bookService;
	private final BookViewMapper bookViewMapper;
	private final FileStorageService fileStorageService;
	private final AuthorViewMapper authorViewMapper;

	private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);


	@Autowired
	public AuthorController(AuthorServiceImpl authorService, BookServiceImpl bookService, BookViewMapper bookViewMapper, FileStorageService fileStorageService, AuthorViewMapper authorViewMapper) {
		this.authorService = authorService;
		this.bookService = bookService;
		this.bookViewMapper = bookViewMapper;
		this.fileStorageService = fileStorageService;
		this.authorViewMapper = authorViewMapper;
	}

	@Operation(summary = "Gets a specific Author by Name")
	@GetMapping(value = "/name/{name}")
	public List<AuthorView> findByName(
			@PathVariable("name") @Parameter(description = "The Name of the Author to find") final String name) {
		List<Author> authors = authorService.findByName(name);
		return authors.stream().map(authorViewMapper::toAuthorView).toList();
	}

	@Operation(summary = "Gets a specific Author by id")
	@GetMapping(value = "/id/{id1}/{id2}")
	public ResponseEntity<AuthorView> findByAuthorID(
			@PathVariable("id1") @Parameter(description = "The id of the author to find") final String id1,
			@PathVariable("id2") final String id2) {
		String authorID = id1 + "/" + id2;
		final var author = authorService.findByAuthorID(authorID).orElseThrow(() -> new NotFoundException(Author.class, authorID));
		AuthorView authorView = authorViewMapper.toAuthorView(author);
		authorView.setImageUrl(authorService.getAuthorImageUrl(authorID));
		System.out.println("Author ID: " + authorID + " Image URL: " + authorView.getImageUrl());
		return ResponseEntity.ok(authorView);
	}

	@Operation(summary = "Creates a new Author")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Author> create(@Valid @RequestBody final CreateAuthorRequest request) {
		final var author = authorService.create(request);
		return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(author);
	}

	@Operation(summary = "Partially updates an existing author")
	@PatchMapping(value = "/{id1}/{id2}")
	public ResponseEntity<Author> partialUpdate(final WebRequest request,
												@PathVariable("id1") @Parameter(description = "The id of the author to update") final String id1,
												@PathVariable("id2") final String id2,
												@Valid @RequestBody final EditAuthorRequest resource) {
		String authorID = id1 + "/" + id2;

		// Validar se o user autenticado tem o mesmo authorID que o authorID acima
		// se não, é FORBIDDEN

		final String ifMatchValue = request.getHeader(IF_MATCH);
		if (ifMatchValue == null || ifMatchValue.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must issue a conditional PATCH using 'if-match'");
		}

		final var author = authorService.partialUpdate(authorID, resource, getVersionFromIfMatchHeader(ifMatchValue));
		return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(author);
	}

	private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
		if (ifMatchHeader.startsWith("\"")) {
			return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
		}
		return Long.parseLong(ifMatchHeader);
	}






	//SprintB
	//SprintB
	//SprintB
	//SprintB

	@Operation(summary = "Get coauthors")
	@GetMapping("/{id1}/{id2}/coauthors")
	public ResponseEntity<List<CoAuthorDTO>> getCoAuthorsAndBooks(@PathVariable String id1, @PathVariable String id2) {
		String authorId = id1 + "/" + id2;

		List<CoAuthorDTO> coAuthors = authorService.getCoAuthorsAndBooks(authorId);
		return ResponseEntity.ok(coAuthors);
	}



	@Operation(summary = "Get top 5 authors")
	@GetMapping("/top5")
	public ResponseEntity<Map<String, Long>> getTop5AuthorsLendings() {
		Map<String, Long> statistics = authorService.getTop5AuthorsLendings();
		return ResponseEntity.ok(statistics);
	}

	@Operation(summary = "Uploads a photo of a author")
	@PostMapping("/{id1}/{id2}/photo")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UploadFileResponse> uploadFile(
			@PathVariable("id1") final String id1, @PathVariable("id2") final String id2,
			@RequestParam("file") final MultipartFile file) throws URISyntaxException {
		String authorIDfile = id1 + "/" + id2;

		Author author = authorService.findByAuthorID(authorIDfile).orElseThrow(() -> new NotFoundException(Author.class, authorIDfile));

		final UploadFileResponse up = doUploadFile(authorIDfile, file);

		try {
			// Save the image information to the author
			AuthorImage authorImage = new AuthorImage(author, file.getBytes(), file.getContentType());
			author.setImage(authorImage);
			authorService.saveAuthor(author);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the uploaded file", e);
		}

		return ResponseEntity.created(new URI(up.getFileDownloadUri())).body(up);
	}





	public UploadFileResponse doUploadFile(final String authorID, final MultipartFile file) {

		final String fileName = fileStorageService.storeFile(authorID, file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(fileName)
				.toUriString();
		// since we are reusing this method both for single file upload and multiple
		// file upload and have different urls we need to make sure we always return the
		// right url for the single file download
		fileDownloadUri = fileDownloadUri.replace("/photos/", "/photo/");

		// TODO save info of the file on the database

		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@Operation(summary = "Downloads a photo of a author")
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
