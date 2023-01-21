package SpringBoot.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;

@Getter
@Setter
@RequiredArgsConstructor
public class FilterPageDTO {

    @Transient
    public String searchedText;

    @Transient
    public String authorizationToken;

    public FilterPageDTO(String searchedText, String authorizationToken){
        this.authorizationToken = authorizationToken;
        this.searchedText = searchedText;
    }
}
