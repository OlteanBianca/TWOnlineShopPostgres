package SpringBoot.DTO;

import SpringBoot.Models.Shop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class FilterPageDTO {

    public String searchedText;

    public String authorizationToken;
    public List<Shop> selectedShops;


    public FilterPageDTO(String searchedText, String authorizationToken) {
        this.searchedText = searchedText;
        this.authorizationToken = authorizationToken;
    }
}
