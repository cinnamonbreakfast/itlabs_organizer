package com.organizer.web.controller;

import com.organizer.core.model.*;
import com.organizer.core.service.*;
import com.organizer.core.service.file.FileService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.*;
import com.organizer.web.utils.Emailer;
import com.organizer.web.utils.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompanyController {


    private final CompanyService companyService;
    private final AuthStore authStore;
    private final UserService userService;
    private final FileService fileService;
    private final CategoryService animeService;
    private final CityListService cityListService;
    private final CountryListService countryListService;
    private final InvitationService invitationService;
    private final ServiceService serviceService;
    private final SpecialistService specialistService;
    private final Emailer emailer;
    private final Regex regex;
    @Autowired
    public CompanyController(CompanyService companyService, AuthStore authStore, UserService userService, FileService fileService, CategoryService animeService, CityListService cityListService, CountryListService countryListService, InvitationService invitationService, ServiceService serviceService,
                             Regex regex,
                             SpecialistService specialistService,
                             Emailer emailer) {
        this.companyService = companyService;
        this.authStore = authStore;
        this.userService = userService;
        this.fileService=fileService;
        this.regex=  regex;
        this.animeService=animeService;
        this.cityListService=cityListService;
        this.countryListService=countryListService;
        this.invitationService = invitationService;
        this.serviceService=  serviceService;
        this.emailer=emailer;
        this.specialistService=specialistService;
    }

    @RequestMapping(value = "c/create",method = RequestMethod.POST,consumes = {"multipart/form-data"})
        //public ResponseEntity<String> addNewCompany(@RequestParam("file") MultipartFile file, @RequestBody CompanyDTO newCompany, @RequestHeader(name = "token") String token){
    public ResponseEntity<String> addNewCompany(@RequestParam(name="file",required = false) MultipartFile file,
CompanyDTO newCompany,@RequestHeader String token ){
        System.out.println(newCompany);
        Long id =  JWToken.checkToken(token);
        //valid token
        if(id== null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

        String username = newCompany.getName()+"-"+newCompany.getCity();
        Company company = companyService.findByUsernameAll(username);
        // validate company_name
        if(company!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Company name already taken");

        //todo:validate image

        //validate category
        String category = newCompany.getCategory();
        if(animeService.getCount(category)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a category from database");
        }
        //validate country & city
        String country = newCompany.getCountry();
        String city = newCompany.getCity();
        if(cityListService.getCount(country,city)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a country and city from database");
        }
        //get category, country & city from db
        System.out.println(newCompany);
        System.out.println(city);
        category = animeService.findByList(category).getList();
        country= countryListService.findByCountry(country).getAbbreviation();
        city = cityListService.findByCity(city).getCity();
        String cui;
        try {
            cui = newCompany.getCui();
        }
        catch (Exception e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please send the cui");

        }

        User user = userService.findById(id);
        company = Company.builder().address(newCompany.getAddress())
                .category(category)
                .city(city)
                .country(country)
                .owner(user)
                .name(newCompany.getName())
                .username(username)
                .validated(false)
                .cui(cui)
                .build();
        if(!file.isEmpty()) {
            try {
                String[] list = file.getOriginalFilename().split("[.]");
                if (list.length == 1) {
                    fileService.uploadDir(file, username + "." + list[0], username);
                    company.setImage_url(username);
                } else if (list.length > 1) {
                    fileService.uploadDir(file, username + "." + list[list.length - 1], username);
                    company.setImage_url(username);
                } else {
                    company.setImage_url("default_company");
                }
            } catch (Exception e) {
                company.setImage_url("default_company");
            }
        }
        else{
            company.setImage_url("default_company");
        }
        companyService.save(company);
        return ResponseEntity.ok("Company has been created, wait for an admin validation");
    }


    @RequestMapping(value = "c/findByOwner",method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> listByCompanyOwner(@RequestHeader(value = "token") String token){
       Long id  = JWToken.checkToken(token);
        if(id==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        User user = userService.findById(id);

        List<Company> companies = companyService.findByOwner(user);

        List<CompanyDTO> companyDTOList = new ArrayList<>(companies.size());
        for(Company company : companies){

            UserDTO userDTO = UserDTO.builder()
                    .phone(user. getPhone())
                    .email(user.getEmail())
                    .build();
            userDTO.setId(user.getId());

            CompanyDTO companyDTO =CompanyDTO.builder()
                    .country(company.getCountry())
                    .owner(userDTO)
                    .name(company.getName())
                    .category(company.getCategory())
                    .address(company.getAddress())
                    .city(company.getCity())
                    .image_url(company.getImage_url())
                    .username(company.getUsername())
                    .build();
            companyDTO.setId(company.getId());
            companyDTOList.add(companyDTO);
        }

        return ResponseEntity.ok(companyDTOList);
    }

    @RequestMapping(value = "c/getId/{id}",method = RequestMethod.GET)
    public ResponseEntity<Company> findCompanyId(@PathVariable Long id ){
        Company company = companyService.findById(id);
        return ResponseEntity.ok(company);
    }

    @RequestMapping(value = "c/{username:.+}",method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> findUsername(@PathVariable String username ){

        Company company = companyService.findByUsername(username);
        if(company==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        List<Service> specialistServices = company.getServices();

        List<Specialist> specialistsStaf=null;

        try {
            specialistsStaf = specialistService.findStaffInCompany(username);

        }
        catch (Exception e ){

        }
        List<SpecialistDTO> specialiststaffDTOS = new ArrayList<>();
        if(specialistsStaf!=null){
            for(Specialist sp :specialistsStaf){
                boolean check = true;
                for(SpecialistDTO spd : specialiststaffDTOS){
                    if(spd.getUser().getId()==sp.getUser().getId()){
                        check = false;
                    }
                }
                if(check==true) {
                    System.out.println(sp.getUser().getId());
                    User u = sp.getUser();
                    UserDTO userDTO = UserDTO.builder()
                            .name(u.getName())
                            .email(u.getEmail())
                            .phone(u.getPhone())
                            .city(u.getCity())
                            .imageURL(u.getImageURL())
                            .country(u.getCountry())
                            .build();
                    userDTO.setId(u.getId());
                    Service service= sp.getService();
                    ServiceDTO serviceDTO = ServiceDTO.builder()
                            .price(service.getPrice())
                            .duration(service.getDuration())
                            .name(service.getServiceName()).build();
                    serviceDTO.setId(service.getId());

                    SpecialistDTO specialistDTO = SpecialistDTO.builder()
                            .user(userDTO)
                            .servicesDTO(serviceDTO)
                            .build();
                    specialistDTO.setId(sp.getId());

                    specialiststaffDTOS.add(specialistDTO);
                }
            }
        }



        List<ServiceDTO> serviceDTOS = new ArrayList<>(specialistServices.size());
        for (Service specialistService : specialistServices) {
            List<SpecialistDTO>specialistDTOS = new ArrayList<>();
            for(Specialist sp : specialistService.getSpecialists()){
                User user = sp.getUser();
                UserDTO userDTO = UserDTO.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .city(user.getCity())
                        .country(user.getCountry())
                        .imageURL(user.getImageURL())
                        .build();
                userDTO.setId(user.getId());
                SpecialistDTO specialistDTO = SpecialistDTO.builder()
                        .user(userDTO)

                        .build();
                specialistDTO.setId(sp.getId());
                specialistDTOS.add(specialistDTO);
            }

            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .price(specialistService.getPrice())
                    .specialistDTOList( specialistDTOS)
                    .duration(specialistService.getDuration())
                    .name(specialistService.getServiceName()).build();
            serviceDTO.setId(specialistService.getId());
            serviceDTOS.add(serviceDTO);
        }
        User ow = company.getOwner();
        UserDTO owner = UserDTO.builder()
                .imageURL(ow.getImageURL())
                .country(ow.getCountry())
                .city(ow.getCity())
                .phone(ow.getPhone())
                .email(ow.getEmail())
                .name(ow.getName())
                .build();
        owner.setId(ow.getId());
        CompanyDTO companyDTO = CompanyDTO.builder()
                .name(company.getName())
                .owner(owner)
                .address(company.getAddress())
                .city(company.getCity())
                .category(company.getCategory())
                .country(company.getCountry())
                .username(company.getUsername())
                .staffMembers(specialiststaffDTOS)
                .image_url(company.getImage_url())
                .services(serviceDTOS).build();

        companyDTO.setId(company.getId());
        return ResponseEntity.ok(companyDTO);
    }
    @RequestMapping(value = "c/changeDetails", method=RequestMethod.PUT,consumes ={"multipart/form-data"})
    public ResponseEntity<String> updateDetails(@RequestParam(name="file",required = false) MultipartFile file,
                                                    CompanyDTO updateCompany,@RequestHeader String token )
    {
            Long id = JWToken.checkToken(token);
            if(id==null){
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
            }
            User user = userService.findById(id);
            String username = updateCompany.getUsername();
            Company company = companyService.findByUsername(username);
            if(company==null){
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known company");
            }
            if(company.getOwner().getEmail().equals(user.getEmail())==false){
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not the owner of the company");
            }
        // validate company_name
        if(company!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Company name already taken");

        //todo:validate image

        //validate category
        String category = updateCompany.getCategory();
        if(animeService.getCount(category)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a category from database");
        }
        //validate country & city
        String country = updateCompany.getCountry();
        String city = updateCompany.getCity();
        if(cityListService.getCount(country,city)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a country and city from database");
        }
        //get category, country & city from db
        category = animeService.findByList(category).getList();
        country= countryListService.findByCountry(country).getAbbreviation();
        city = cityListService.findByCity(city).getCity();
        String name = updateCompany.getName();
        String address = updateCompany.getAddress();

        company.setAddress(address);
        company.setName(name);
        company.setCity(city);
        company.setCountry(country);
        company.setCategory(category);
        try {
                String[] list = file.getOriginalFilename().split("[.]");
                if (list.length == 1) {
                    fileService.uploadDir(file, username+ "." + list[0],username);
                    company.setImage_url(username);
                } else if (list.length > 1) {
                    fileService.uploadDir(file, username + "." + list[list.length - 1],username);
                    company.setImage_url(username);
                } else {
                    company.setImage_url("default_company");
                }
            }
            catch (Exception e )
            {
                company.setImage_url("default_company");
            }
        companyService.save(company);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Updated company");
    }

    @RequestMapping(value="c/invite/specialist", method = RequestMethod.POST)
    public ResponseEntity<String > inviteSpecialist(@RequestParam String  userPhone,@RequestParam String companyUsername,@RequestParam String serviceName, @RequestHeader String token){
        Long id = JWToken.checkToken(token);
        System.out.println(userPhone);
        System.out.println(companyUsername);
        System.out.println(serviceName);
        int m = regex.phoneMatcher("40",userPhone);
        if(m==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a valid phonen number");
        }
        String tel = "+40"+userPhone.substring(m,userPhone.length());
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bad token");
        }
        User user = userService.findById(id);
        if(user==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known entry user");
        }
        Company company = companyService.findByUsername(companyUsername);
        if(company.getOwner().getId()!=user.getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not the owner of this company");
        }
        User userSelected = userService.findByPhone(tel);
        if(userSelected==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known selected user");
        }
        Service service = serviceService.findByServiceAndCompany(serviceName,companyUsername);
        if(service==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Service not yet created");
        }
        System.out.println(service.getServiceName());
        Invitation invitation = invitationService.findByUserAndService(userSelected,service);
        if(invitation!=null){
          //has received

            return ResponseEntity.status(HttpStatus.OK).body("Invitation has already been send again");
        }
        {
            invitation = Invitation.builder()
                    .company(company)
                    .user(userSelected)
                    .service(service)
                    .accepted(false)
                    .build();

            try {
                invitationService.save(invitation);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
            }

            emailer.sendSimpleMessage(userSelected.getEmail(),
                    "Specialist invitation in "+company.getName()
                    ,"You have received an invitation in "+company.getName()+"please enter" +
                            "your account for more information!");

            return ResponseEntity.status(HttpStatus.OK).body("Invitation has been send");
        }

    }


}
