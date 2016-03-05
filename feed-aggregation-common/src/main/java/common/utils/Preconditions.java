package common.utils;

import common.exception.PreconditionException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Utils for checking preconditions and validity
 * Created by Yvonne on 2016-03-05.
 */
public class Preconditions {
    private static final String DEFAULT_MESSAGE = "Received an invalid parameter";

    private static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[0-9]+");


    /**
     * Checks that an object is not null.
     *
     * @param object   any object
     * @throws PreconditionException if the object is null
     */
    public static void checkNotNull(Object object) {
        checkNotNull(object, null);
    }


    /**
     * Checks that an object is not null.
     *
     * @param object   any object
     * @param errorMsg error message
     * @throws PreconditionException if the object is null
     */
    public static void checkNotNull(Object object, String errorMsg) {
        check(object != null, errorMsg);
    }

    /**
     * Checks that at least one of object1 or object2 is not null
     *
     * @param object1 any object
     * @param object2 any object
     * @throws PreconditionException if both object1 and object2 are null
     */
    public static void checkBothNotNull(Object object1, Object object2) {
        checkBothNotNull(object1, object2, null);
    }

    /**
     * Checks that at least one of object1 or object2 is not null
     *
     * @param object1 any object
     * @param object2 any object
     * @param errorMsg error message
     * @throws PreconditionException if both object1 and object2 are null
     */
    public static void checkBothNotNull(Object object1, Object object2, String errorMsg) {
        check(!(object1 == null && object2 == null), errorMsg);
    }

    /**
     * Checks that a string is not null or empty
     * @param string   any string
     * @throws PreconditionException if the string is null or empty
     */
    public static void checkEmptyString(String string) {
        checkEmptyString(string, null);
    }

    /**
     * Checks that a string is not null or empty
     * @param string   any string
     * @param errorMsg error message
     * @throws PreconditionException if the string is null or empty
     */
    public static void checkEmptyString(String string, String errorMsg) {
        check(StringUtils.isNotBlank(string), errorMsg);
    }

    /**
     * Checks that a URL is valid
     *  @param url      any string
     *
     */
    public static void checkValidUrl(String url) {
        checkValidUrl(url, null);
    }

    /**
     * Checks that a URL is valid
     *
     * @param url      any string
     * @param errorMsg error message
     */
    public static void checkValidUrl(String url, String errorMsg) {
        checkEmptyString(url, errorMsg);
        check(isUrl(url), errorMsg);
    }

    private static boolean isUrl(String url) {
        return URL_PATTERN.matcher(url).matches();
    }

    private static boolean isNumeric(String numericString) {
        return NUMERIC_PATTERN.matcher(numericString).matches();
    }

    private static void check(boolean requirements, String error) {
        String message = StringUtils.isBlank(error) ? DEFAULT_MESSAGE : error;

        if (!requirements) {
            throw new PreconditionException(message);
        }
    }
}
