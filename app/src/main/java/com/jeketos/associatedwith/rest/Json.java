package com.jeketos.associatedwith.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Utility class for converting to and from json any object */
public class Json {

    public static final Json DEFAULT = new Json();

    private final ObjectMapper objectMapper = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Set all class field serializable (public, private, protected)
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // Set don't throw exception if found unknown properties in json
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Set wrap a single value in a list or array where it expected
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//        objectMapper.setDateFormat(new ChainDateFormat()
//                .add(new ChainDateFormat())
//                .add("dd.MM.yyyy")
//                .add("dd.MM.yyyy HH:mm")
//        );
        return objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public CollectionType collectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {

        return objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /** Parse and extract a plain object by the key */
    public <T> T toObject(String data, String key, Class<T> clazz) {
        if(data == null) return null;

        if(clazz.isAssignableFrom(Collection.class)){
            throw new IllegalArgumentException("Unsupported Collection subclasses for that you must to use 'T toObject(String data, String key, JavaType typeToken)'");
        }
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            if(jsonNode.get(key) == null) return null;

            JsonNode keyJsonNode = jsonNode.get(key);
            return objectMapper.readValue(keyJsonNode.asText(), clazz);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Parse and extract a generic collection by the key */
    public <T extends Collection> T toCollection(String data, String key, JavaType javaType) {
        if(data == null) return null;
        try {
            JsonNode jsonNode = objectMapper.readTree(data);

            if(jsonNode.get(key) == null || jsonNode.get(key).isNull()) {
                return getEmptyCollection(javaType);
            }

            JsonNode keyJsonNode = jsonNode.get(key);
            return objectMapper.readValue(keyJsonNode.toString(), javaType);
        }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    /** Parse and extract a generic collection by the key */
    public <T extends Collection> T toCollection(String data, JavaType javaType) {
        if(data == null) return null;
        try {
            return objectMapper.readValue(data, javaType);
        }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public <T> T toObject(String data, Class<T> clazz) {
        if(data == null) return null;
        try {
            return objectMapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T toObject(JsonNode jsonNode, Class<T> clazz){
        if(jsonNode == null) return null;
        try {
            return objectMapper.treeToValue(jsonNode, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T toObject(InputStream is, Class<T> clazz) {
        if(is == null) return null;
        try {
            return objectMapper.readValue(is, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(Object obj) {
        if(obj == null) return null;

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode toTree(String data) {
        if(data == null) return null;
        try {
            return objectMapper.readTree(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Collection> T getEmptyCollection(JavaType typeToken) {
        if(typeToken.getRawClass().isAssignableFrom(List.class)){
            return (T) Collections.EMPTY_LIST;
        }
        else if(typeToken.getRawClass().isAssignableFrom(Set.class)){
            return (T) Collections.EMPTY_SET;
        }
        else if(typeToken.getRawClass().isAssignableFrom(Collection.class)){
            return (T) Collections.EMPTY_LIST;
        }
        else if(typeToken.getRawClass().isAssignableFrom(Map.class)){
            return (T) Collections.EMPTY_MAP;
        }
        throw new RuntimeException("Unsupported collection type: " + typeToken.getRawClass());
    }

//    /** {@link DateFormat} which can recognize many date formats(chain of patterns) */
//    private static class ChainDateFormat extends SimpleDateFormat {
//        private final Collection<DateFormat> mDateFormats = new ArrayList<>();
//
//        public ChainDateFormat add(String pattern){
//            add(new SimpleDateFormat(pattern, Locale.getDefault()));
//            return this;
//        }
//
//        public ChainDateFormat add(DateFormat df){
//            mDateFormats.add(df);
//            return this;
//        }
//
//        @Override
//        public StringBuffer format(Date date, StringBuffer buffer, FieldPosition field) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public Date parse(String string, ParsePosition position) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public Date parse(String data) throws ParseException {
//            for(DateFormat df : mDateFormats){
//                try {
//                    return df.parse(data);
//                } catch (ParseException e) { /* look over */ }
//            }
//            throw new ParseException("Unknown date format: " + data, -1);
//        }
//    }
}
