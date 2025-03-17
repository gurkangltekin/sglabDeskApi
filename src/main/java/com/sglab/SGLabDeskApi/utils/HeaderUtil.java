package com.sglab.SGLabDeskApi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public final class HeaderUtil {
    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "sglabDeskApp";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-sglabDeskApp-alert", message);
        headers.add("X-sglabDeskApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityGetAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".sent", param);
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity creation failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-sglabDeskApp-error", "error." + errorKey);
        headers.add("X-sglabDeskApp-params", entityName);
        return headers;
    }

    public static HttpHeaders createFailureHeader( String errorKey, String defaultMessage) {
        log.error("Entity creation failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-sglabDeskApp-error", "error." + errorKey);
        return headers;
    }
}
