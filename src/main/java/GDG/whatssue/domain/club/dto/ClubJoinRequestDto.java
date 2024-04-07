package GDG.whatssue.domain.club.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubJoinRequestDto {

    @Size(min = 6, max = 6)
    private String privateCode;
}
