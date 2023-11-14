package test.safecast;

import java.util.ArrayList;
import java.util.List;

public class ListCast {
    public static <T> List<T> cast(Object o, Class<T> clazz) {
        if (!(o instanceof List)) {
            return null;
        }
        List<T> ret = new ArrayList<>();
        var list = (List<?>) o;
        for (var elem : list) {
            try {
                T t = clazz.cast(elem);
                ret.add(t);
            } catch (ClassCastException e) {
            }
        }
        return ret;
    }
}
