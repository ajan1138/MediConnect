package dev.ahmedajan.mediconnect.rate.DTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateRequestDTO {

    @Min(1)
    @Max(5)
    private int rate;

}
