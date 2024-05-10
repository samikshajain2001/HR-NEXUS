package com.nri.ems.Services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nri.ems.Config.JWT.JWTService;
import com.nri.ems.DTO.RefreshTokenRequest;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.RefreshToken;
import com.nri.ems.Repositories.EmployeeRepository;
import com.nri.ems.Repositories.RefreshTokenRepository;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    JWTService jwtService;

    private long expiryTime = 5 * 60 * 60 * 1000;

    @Override
    public RefreshToken createRefreshToken(String username) {
        Employee existEmployee = employeeRepository.findByUsername(username);
        if (existEmployee != null) {
            RefreshToken tokenFound = existEmployee.getRefreshToken();
            if (tokenFound == null) {
                tokenFound = RefreshToken.builder()
                        .token(UUID.randomUUID().toString())
                        .expiry(Instant.now().plusMillis(expiryTime)).employee(existEmployee).build();
            } else {
                tokenFound.setExpiry(Instant.now().plusMillis(expiryTime));
            }
            existEmployee.setRefreshToken(tokenFound);
            if (tokenFound != null) {
                refreshTokenRepository.save(tokenFound);
            } else {
            }
            return tokenFound;
        } else {
            return null;
        }
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken token1 = refreshTokenRepository.findByToken(token).orElse(null);
        if (token1 == null) {
            return null;
        } else {
            if (token1.getExpiry().compareTo(Instant.now()) < 0) {
                // token is expired
                token1.getEmployee().setRefreshToken(null);
                refreshTokenRepository.delete(token1);
                return null;
            } else {

                return token1;
            }
        }
    }

    @Override
    public String generateTokenIfExpired(RefreshTokenRequest req) {
        RefreshToken refToken = this.verifyRefreshToken(req.getRefreshToken());
        if (refToken != null) {
            Employee foundEmp = refToken.getEmployee();
            String jwt = jwtService.generateToken(foundEmp);
            return jwt;
        } else {
            return null;
        }
    }

}
