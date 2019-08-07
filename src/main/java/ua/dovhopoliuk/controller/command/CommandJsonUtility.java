package ua.dovhopoliuk.controller.command;

import com.google.gson.Gson;

public class CommandJsonUtility<T> {

    private final Class<T> typeParameterClass;
    private final Gson gson;

    public CommandJsonUtility(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
        this.gson = new Gson();
    }

    public String toJson(T entity) {
       return gson.toJson(entity);
    }

    public T fromJson(String json) {
        return gson.fromJson(json, typeParameterClass);
    }
}
