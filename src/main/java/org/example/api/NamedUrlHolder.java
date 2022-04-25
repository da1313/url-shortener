package org.example.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NamedUrlHolder {
    @NotBlank
    @URL
    private String url;
    @NotBlank
    @Size(max = 40)
    private String name;
}
