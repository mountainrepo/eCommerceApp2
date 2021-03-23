package com.udacity.course4.common;

import org.slf4j.Logger;

public class Logging {

    public static void logMethodEntry(Logger logger, String path, String method, String position, String inputArgument, String value, String otherArguments) {
        logger.info("request_path={}, api_method={}, position={}, key_input_argument={}, key_value={}, other_arguments={}",
                path, method, position, inputArgument, value, otherArguments);
    }

    public static void logMethodExit(Logger logger, String path, String method, String position, int httpStatusCode, String httpStatusMessage) {
        logger.info("request_path={}, api_method={}, position={}, http_status_code={}, http_status_message={}",
                path, method, position, httpStatusCode, httpStatusMessage);
    }

    public static void logValidationOrProcessingError(Logger logger, String path, String method, String position, int errorCode, String errorMessage, int httpStatusCode, String httpStatusMessage) {
        logger.error("request_path={}, api_method={}, position={}, error_code={}, error_message={}, http_status_code={}, http_status_message={}",
                path, method, position, errorCode, errorMessage, httpStatusCode, httpStatusMessage);
    }

    public static void logDatabaseError(Logger logger, String path, String method, String position, int errorCode, String errorMessage, String repository, String repositoryMethod, int httpStatusCode, String httpStatusMessage) {
        logger.error("request_path={}, api_method={}, position={}, error_code={}, error_message={}, repopsitory={}, repository_method={}, http_status_code={}, http_status_message={}",
                path, method, position, errorCode, errorMessage, repository, repositoryMethod, httpStatusCode, httpStatusMessage);
    }

    public static void logSuccessMessage(Logger logger, String path, String method, String position, int messageCode, String message) {
        logger.info("request_path={}, api_method={}, position={}, message_code={}, message={}",
                path, method, position, messageCode, message);
    }
}
