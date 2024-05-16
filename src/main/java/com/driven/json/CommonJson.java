package com.driven.json;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommonJson {
    private final String PATH = "src/main/resources/common.json";

    public JSONObject getJsonObject() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(PATH)));

        JSONObject jsonObject = new JSONObject(content);

        return jsonObject;
    }

    public <T> T getValue(JSONObject jsonObject, String key){
        Object o = jsonObject.get(key);
        try {
            o = (JSONObject) o;
        } catch (Exception e) {
            o = (Object) o;
        }

        return (T) o;
    }
}
