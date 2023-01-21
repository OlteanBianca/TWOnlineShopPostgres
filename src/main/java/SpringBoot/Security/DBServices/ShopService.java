package SpringBoot.Security.DBServices;

import SpringBoot.Models.*;
import SpringBoot.Repository.ShopFunctionRepository;
import SpringBoot.Repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopFunctionRepository shopFunctionRepository;


    public Shop getShopByUserId(Long id) {
        return shopRepository.findByUserId(id);
    }

    public List<Shop> findAllShopsByShopFunctionId(Long id) {
        return shopRepository.findAllByShopFunctionId(id);
    }

    public void addNewShop(Request request, User user) {

        long id = 2L;
        if (request.getRole().getName() == ERole.ROLE_ADMIN_BB) {
            id = 1L;
        }
        ShopFunction shopFunction = shopFunctionRepository.findShopFunctionById(id);
        Shop shop = new Shop(request.getShopName(), request.getAddress(), request.getCode(), shopFunction, user);
        shopRepository.save(shop);
    }
}
