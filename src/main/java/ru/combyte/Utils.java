package ru.combyte;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.ServletResponse;
import lombok.NonNull;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {
    /**
     * @return empty list if all keys is presented as keys in map, list of these keys, if some not presented
     */
    public static<T, U> List<T> getNotPresentedKeysList(Map<T, U> map, List<T> keysToCheck) {
        List<T> notPresentedKeysList = new LinkedList<>();
        for (var keyToCheck : keysToCheck) {
            if (!map.containsKey(keyToCheck)) {
                notPresentedKeysList.add(keyToCheck);
            }
        }
        return notPresentedKeysList;
    }

    public static void appendProps(JSONObject root, JSONObject propsToAdd) {
        propsToAdd.toMap()
                .forEach((key, value) -> root.append(key, value));
    }

    /**
     * @return @JsonProperty value
     * @throws IllegalAccessException if hasn't the field
     * @throws IllegalStateException if field hasn't @JsonProperty
     * @throws NullPointerException if object or fieldName is null
     */
    public static String getJsonProperty(@NonNull Object object, @NonNull String fieldName) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field f : fields) {
            if (f.getName().equals(fieldName)) {
                JsonProperty jsonProperty = f.getDeclaredAnnotation(JsonProperty.class);
                if (jsonProperty != null) {
                    return jsonProperty.value();
                } else {
                    throw new IllegalStateException();
                }
            }
        }
        throw new IllegalAccessException("Object %s hasn't the field %s".formatted(object, fieldName));
    }


    public static String getDateAsISO8601(Date date) {
        return date.toInstant().toString();
    }


    /**
     * @throws NullPointerException null if servletResponse or content is null
     * @throws IOException on write error
     */
    public static void writeJsonContent(@NonNull ServletResponse servletResponse, @NonNull String content) throws IOException {
        servletResponse.setContentType("application/json");
        servletResponse.getWriter().write(content);
        servletResponse.setContentLength(content.length());
    }
}
