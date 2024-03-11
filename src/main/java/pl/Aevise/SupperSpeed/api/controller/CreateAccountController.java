package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class CreateAccountController {

    private static final String CREATE_ACCOUNT_PAGE = "/create";

    @GetMapping(CREATE_ACCOUNT_PAGE)
    String getAccountCreationForm(){
        return "create_account_page";
    }
}
