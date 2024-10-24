package org.sidis.lendings.lendingmanagement.services;

import com.example.library.lendingmanagement.model.Lending;
import com.example.library.lendingmanagement.repositories.LendingRepository;
import jakarta.validation.ValidationException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EditLendingMapper {

	@Autowired
	private LendingRepository lendingRepository;

	@Mapping(source = "returnDate", target = "returnDate")
	@Mapping(source = "notes", target = "notes")
	@Mapping(target = "lendingID", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "book", ignore = true)
	@Mapping(target = "reader", ignore = true)
	@Mapping(target = "startDate", ignore = true)
	@Mapping(target = "expectedReturnDate", ignore = true)
	@Mapping(target = "fine", ignore = true)
	@Mapping(target = "overdue", ignore = true)  // Atualizado para 'overdue'
	public abstract void update(EditLendingRequest request, @MappingTarget Lending lending);

	public Lending toLending(final String lendingID) {
		return lendingRepository.findByLendingID(String.valueOf(Long.valueOf(lendingID))).orElseThrow(() -> new ValidationException("Select an existing lending"));
	}
}
