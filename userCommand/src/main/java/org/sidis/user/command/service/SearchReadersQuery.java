package org.sidis.user.command.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchReadersQuery {
    private String name;
    private String email;
    private String phoneNumber;
}
