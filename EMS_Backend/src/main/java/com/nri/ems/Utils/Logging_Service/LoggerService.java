package com.nri.ems.Utils.Logging_Service;

public interface LoggerService {
    // logging levels
    void info(String message);

    void debug(String message);

    void error(String message);
}
