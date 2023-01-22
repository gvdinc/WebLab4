package ru.combyte;

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
}
