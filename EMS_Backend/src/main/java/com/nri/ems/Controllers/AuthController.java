package com.nri.ems.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nri.ems.DTO.AuthRequest;
import com.nri.ems.DTO.AuthResponse;
import com.nri.ems.DTO.EmployeeDTO;
import com.nri.ems.DTO.RefreshTokenRequest;
import com.nri.ems.DTO.RefreshTokenResponse;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Services.EmployeeService;
import com.nri.ems.Services.RefreshTokenServiceImpl;
import com.nri.ems.Utils.Logging_Service.LoggerServiceImpl;

import jakarta.mail.MessagingException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Autowired
    private LoggerServiceImpl loggerServiceImpl;

    // api called for login authentication
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        AuthResponse response = employeeService.authenticate(authRequest);
        if (response == null) {
            loggerServiceImpl.error("Invalid Credentials");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            loggerServiceImpl.info("User verified!!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // api for adding the first hr
    @PostMapping("/add-hr")
    public ResponseEntity<Employee> addHr(@RequestBody EmployeeDTO employeeDTO) throws MessagingException {
        Employee savedEmp = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmp, HttpStatus.OK);
    }

    // api called when jwt expires , requesting for a new token
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest req) {
        String jwt = refreshTokenServiceImpl.generateTokenIfExpired(req);
        Employee existEmp = employeeService.getByUsername(req.getUsername());
        if (jwt == null || existEmp == null) {
            RefreshTokenResponse respn = RefreshTokenResponse.builder()
                    .accesstoken(null)
                    .refreshtoken(null)
                    .build();
            return new ResponseEntity<>(respn, HttpStatus.NOT_FOUND);
        } else {
            if (!existEmp.getStatus().name().equals("ACTIVE")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                RefreshTokenResponse response = RefreshTokenResponse.builder()
                        .accesstoken(jwt)
                        .refreshtoken(req.getRefreshToken())
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        }
    }
}
