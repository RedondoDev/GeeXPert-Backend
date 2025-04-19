package com.redondodev.GeeXPert_back.protectedContent;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protectedContent")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ExploreController {

    @PostMapping("/explore")
    public String explore() {
        return "Exploring from protected endpoint";
    }

}
