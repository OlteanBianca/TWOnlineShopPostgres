package SpringBoot.Security.DBServices;

import SpringBoot.Models.Request;
import SpringBoot.Models.User;
import SpringBoot.Repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PasswordEncoder encoder;



    public void addNewRequest(Request request) {
        request.setPassword(request.getPassword());
        requestRepository.save(request);
    }

    public List<Request> getActiveRequests() {
        return requestRepository.findAllByApprovedFalse();
    }

    public void acceptRequest(Long id) {

        var value = requestRepository.findById(id);
        if (value.isPresent()) {
            Request request = value.get();
            User user = userService.addNewUser(request);
            shopService.addNewShop(request, user);

            request.setApproved(true);
            requestRepository.save(request);
        }
    }
}
