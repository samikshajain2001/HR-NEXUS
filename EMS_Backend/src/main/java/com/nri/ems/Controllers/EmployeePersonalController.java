package com.nri.ems.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nri.ems.DTO.ChangePassDTO;
import com.nri.ems.DTO.RequestDTO;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.UpdateRequest;
import com.nri.ems.Services.EmployeeService;
import com.nri.ems.Services.EmployeeServiceImpl;
import com.nri.ems.Services.UpdateRequestService;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/emp")
public class EmployeePersonalController {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    EmployeeServiceImpl employeeServiceImpl;
    @Autowired
    private UpdateRequestService updateRequestService;
     @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager  authenticationManager;
     @Autowired
     EmployeeService employeeService;
    @GetMapping("/get-curr-user")
    public Employee getCurrUser(Principal principal) {
        return (Employee) userDetailsService.loadUserByUsername(principal.getName());
    }

    @PutMapping("/create-request")
    public ResponseEntity<UpdateRequest> add(@RequestBody RequestDTO reqBody) {
        UpdateRequest req = updateRequestService.createRequest(reqBody);
        return new ResponseEntity<>(req, HttpStatus.OK);
    }

    @GetMapping("/check-request")
    public ResponseEntity<UpdateRequest> checkRequest(Principal principal) {
        UpdateRequest req = updateRequestService.checkUpdateRequest(principal.getName());
        return new ResponseEntity<>(req, HttpStatus.OK);
    }
      @PostMapping("/newpassword")
    public ResponseEntity<String> changepassword(@RequestBody ChangePassDTO  changePassword  , Principal principal){
        Employee requestEmp = (Employee) userDetailsService.loadUserByUsername(principal.getName());
       try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestEmp.getUsername(), changePassword.getOldpassword()));
        } catch (Exception e) {
          
            return new ResponseEntity<>("Invalid Credentials",HttpStatus.CONFLICT);
        }

        
        String encodedpass= passwordEncoder.encode(changePassword.getPassword());
        int a=employeeService.updatepass(requestEmp.getUsername(),encodedpass);
       if(a==1) return new ResponseEntity <>("Password changed successfully",HttpStatus.OK);
       else{
           return new ResponseEntity <>( "Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
