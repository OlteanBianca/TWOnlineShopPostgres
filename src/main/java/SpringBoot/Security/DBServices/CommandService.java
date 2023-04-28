package SpringBoot.Security.DBServices;

import SpringBoot.Models.*;
import SpringBoot.Repository.CommandProductsRepository;
import SpringBoot.Repository.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommandService {

    @Autowired
    private CommandProductsRepository commandProductsRepository;
    @Autowired
    private ShopInventoryService shopInventoryService;
    @Autowired
    private CommandRepository commandRepository;
    @Autowired
    private ShoppingCartService shoppingCartService;



    public List<CommandProducts> getAllCommandsForOneShop(Long shopId) {
        return commandProductsRepository.findAllByShopId(shopId);
    }

    public List<CommandProducts> getAllCommandProductsForOneClient(Long userId) {
        List<Command> allCommands = getAllCommandsForOneClient(userId);
        List<CommandProducts> allProducts = new ArrayList<>();

        for (Command command : allCommands) {
            allProducts.addAll(commandProductsRepository.findAllByCommandId(command.getId()));
        }
        return allProducts;
    }

    public List<Command> getAllCommandsForOneClient(Long userId) {
        return commandRepository.findAllByUserId(userId);
    }

    // Set accepted = true for a product in a command if quantity is enough.
    public boolean acceptCommandProduct(Long commandProductId) {
        Optional<CommandProducts> product = commandProductsRepository.findById(commandProductId);

        if (product.isPresent()) {
            CommandProducts productInCommand = product.get();

            if (isProductQuantityAvailable(productInCommand)) {

                shopInventoryService.updateShopInventory(productInCommand);

                productInCommand.setAccepted(true);
                commandProductsRepository.save(productInCommand);
                return true;
            }
        }
        return false;
    }

    public void deleteCommandProduct(Long id) {
        commandProductsRepository.deleteById(id);
    }

    public boolean isProductQuantityAvailable(CommandProducts commandProduct) {

        return shopInventoryService.isProductQuantityInInventory(commandProduct.getProduct().getId(),
                commandProduct.getShop().getId(), commandProduct.getQuantity());
    }

    //If product is available then it gets added to the command.
    //The function return the list of products that are not available.
    public Command createCommandFromShoppingCart(User user, List<ShoppingCartProducts> products) {
        Command command = new Command(user);
        command = commandRepository.save(command);

        for (ShoppingCartProducts product : products) {

            if (addProductToCommand(product, command)) {
                shoppingCartService.deleteProductInShoppingCart(product.getId());
            }
        }
        return command;
    }

    public boolean addProductToCommand(ShoppingCartProducts shoppingCartProduct, Command command) {

        CommandProducts commandProducts = new CommandProducts(
                shoppingCartProduct.getQuantity(),
                shoppingCartProduct.getShop(),
                shoppingCartProduct.getProduct(),
                command);

        if (isProductQuantityAvailable(commandProducts)) {
            commandProductsRepository.save(commandProducts);
            return true;
        }
        return false;
    }

    public void finishCommand(Command command) {

        List<CommandProducts> products = commandProductsRepository.findAllByCommandId(command.getId());
        for (CommandProducts product : products) {

            if (isProductQuantityAvailable(product)) {

                product.setAccepted(true);
                shopInventoryService.updateShopInventory(product);
            }
        }
    }
}
