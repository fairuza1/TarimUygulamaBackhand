
package ercankara.uygulamam_backhad.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;


@Data
public class PlantDTO {
    private Long id;

    @NotNull()
    private String name;

    @NotNull()  // Kategori ID'yi de ekliyoruz
    private Long categoryId;

    private String categoryName;
}
