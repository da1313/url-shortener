package org.example.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUrlListRequest {
    @PositiveOrZero
    private int pageSize;
    @PositiveOrZero
    private int pageNumber;
}
