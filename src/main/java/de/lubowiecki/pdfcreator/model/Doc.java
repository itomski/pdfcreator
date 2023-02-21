package de.lubowiecki.pdfcreator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doc {

    private String header;
    private String author;
    private String body;
    private String footer;

}
