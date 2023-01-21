package SpringBoot.Security.DBServices;

import SpringBoot.Models.Command;
import SpringBoot.Models.CommandProducts;
import SpringBoot.Repository.CommandProductsRepository;
import SpringBoot.Repository.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;
    @Autowired
    private CommandProductsRepository commandProductsRepository;
    @Autowired
    private ShopInventoryService shopInventoryService;


    public List<Command> getAllActiveCommandsForOneShop(Long shopId){
        return commandRepository.findAllByShopIdAndAcceptedFalse(shopId);
    }

    public List<Command> getAllCommandsForOneShop(Long shopId){
        return commandRepository.findAllByShopId(shopId);
    }

    public List<CommandProducts> getAllProductsInCommand(Long id){
        return commandProductsRepository.findAllByCommandId(id);
    }

    public boolean acceptCommand(Long id){
        var command = commandRepository.findById(id);

        if(command.isPresent()){
            Command updateCommand = command.get();

            List<CommandProducts> products = getAllProductsInCommand(updateCommand.getId());

            if(areCommandProductsAvailable(updateCommand,products)) {
                shopInventoryService.updateShopInventory(updateCommand.getShop().getId(), products);
                updateCommand.setAccepted(true);
                commandRepository.save(updateCommand);
                return true;
            }
        }
        return false;
    }

    public boolean areCommandProductsAvailable(Command command, List<CommandProducts> products){

        for (CommandProducts product: products) {
             if(!shopInventoryService.isProductQuantityInInventory(
                     product.getProduct().getId(), command.getShop().getId(), product.getQuantity())){
                 return false;
             }
        }
        return true;
    }
}
