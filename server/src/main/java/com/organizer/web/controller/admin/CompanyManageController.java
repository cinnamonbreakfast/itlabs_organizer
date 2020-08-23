package com.organizer.web.controller.admin;

import com.organizer.core.model.Company;
import com.organizer.core.model.User;
import com.organizer.core.service.AdminService;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class CompanyManageController {
    //TODO:validate companies: LIST ALL COMPANIES THAT ARE NOT VALIDATED
    //TODO: VALIDATE : validated a company

    UserService userService;
    AdminService adminService;
    CompanyService companyService;
    @Autowired
    public CompanyManageController(AdminService adminService, UserService userService,CompanyService companyService){
        this.adminService=adminService;
        this.userService =userService;
        this.companyService= companyService;
    }
    @RequestMapping(value = "admin/get/companies")
    public ResponseEntity<List<Company> > listValidatingCompanies(@RequestParam int page, @RequestHeader String token){
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        System.out.println(id);
        User user=null;
        try {
            user = userService.findById(id);
        }
        catch (Exception e )
        {
            System.out.println(e);
            e.printStackTrace();
        }
        if(user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if(user.getAdmin()==false){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<Company> companyList = adminService.getCompanies(page);
        return ResponseEntity.ok(companyList);
    }

    @RequestMapping(value ="admin/validate/company/{username}")
    public ResponseEntity<String > validateCompany(@RequestHeader String token , @PathVariable String username ){

        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
        }
        User user= userService.findById(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }

        if(user.getAdmin()==false) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not an admin user");
        }
        try {
            Company company = companyService.findByUsername(username);
            if(company==null){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("In valid company username");
            }
            company.setValidated(true);
            companyService.save(company);
        }
        catch (Exception e ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Some server error exception");
        }
        return ResponseEntity.ok("Company has been validated");
    }

}
