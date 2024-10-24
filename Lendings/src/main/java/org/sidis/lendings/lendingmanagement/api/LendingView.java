package org.sidis.lendings.lendingmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "A Lending")
public class LendingView {
	private String id;
	private LocalDate startDate;
	private LocalDate expectedReturnDate;
	private LocalDate returnDate;
	private boolean overdue;
	private int fine;
	private String readerID;
	private Long bookID;
	private String bookTitle;
	private long numberOfDaysInOverdue;
	private String notes;
}
