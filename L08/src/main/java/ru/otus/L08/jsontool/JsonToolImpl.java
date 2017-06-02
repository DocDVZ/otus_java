package ru.otus.L08.jsontool;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by DocDVZ on 01.06.2017.
 */
public class JsonToolImpl implements JsonTool {

    private final Set<Class<?>> primitives;

    public JsonToolImpl() {
        primitives = new HashSet<>(Arrays.asList(Boolean.class, Long.class, Integer.class, Character.class, Double.class, Float.class, Byte.class, Short.class));
    }

    @Override
    public String toJson(Object object) {
        if (object == null) {
            return null;
        }
        StringBuilder resultSB = new StringBuilder();

        Class<?> clazz = object.getClass();
        if (isSimpleElement(clazz)){
            if (isArray(clazz)){
                resultSB.append("[");
                String arrPrfx = "";
                for (Object item : (Object[]) object){
                    resultSB.append(arrPrfx);
                    arrPrfx = ",";
                    resultSB.append(toJson(item));
                }
                resultSB.append("]");
            } else if (isCollection(clazz)){
                resultSB.append("[");
                String arrPrfx = "";
                for (Object item : (Collection) object){
                    resultSB.append(arrPrfx);
                    arrPrfx = ",";
                    resultSB.append(toJson(item));
                }
                resultSB.append("]");
            } else if (clazz.equals(Object.class)){
                return "{},";
            } else if (isString(clazz)){
                return "\"" + object.toString() + "\"";
            }
            return object.toString();
        }

        resultSB.append("{");
        Field[] fields = object.getClass().getDeclaredFields();
        String prefix = "";
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.get(object) == null) {
                    continue;
                }
                resultSB.append(prefix);
                prefix = ",";
                resultSB.append("\"" + field.getName() + "\":");
                if (isSimple(field.getType())) {
                    resultSB.append(field.get(object));
                } else if (isString(field.getType())) {
                    resultSB.append("\"" + field.get(object) + "\"");
                } else if (isDate(field.getType())) {
                    resultSB.append("\"" + ISO8601DateParser.toString((Date) field.get(object)) + "\"");
                } else if (isCollection(field.getType())) {
                    Collection collection = (Collection) field.get(object);
                    resultSB.append("[");
                    String arrPrfx = "";
                    for (Object item : collection) {
                        resultSB.append(arrPrfx);
                        arrPrfx = ",";
                        resultSB.append(toJson(item));
                    }
                    resultSB.append("]");
                } else if (isArray(field.getType())) {
                    resultSB.append("[");
                    Object[] arr = (Object[]) field.get(object);
                    String arrPrfx = "";
                    for (Object item : arr) {
                        resultSB.append(arrPrfx);
                        arrPrfx = ",";
                        resultSB.append(toJson(item));
                    }
                    resultSB.append("]");
                } else {
                    resultSB.append(toJson(field.get(object)));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(false);
            }
        }
        resultSB.append("}");
        return resultSB.toString();
    }


    private Boolean isSimple(Class<?> clazz) {
        for (Class<?> pr : primitives){
            if (clazz.isAssignableFrom(pr)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private Boolean isString(Class<?> clazz) {
        if (String.class.isAssignableFrom(clazz)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean isCollection(Class<?> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean isArray(Class<?> clazz) {
        if (clazz.isArray()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean isDate(Class<?> clazz) {
        if (clazz.isAssignableFrom(Date.class)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean isSimpleElement(Class<?> clazz){
        if (isArray(clazz) || isCollection(clazz) || isDate(clazz) || isString(clazz) || isSimple(clazz)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
