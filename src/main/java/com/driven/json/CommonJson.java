package com.driven.json;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommonJson {
    public JSONObject getJsonObject() throws IOException {
        // 클래스 로더를 사용하여 클래스패스에서 리소스를 읽기
        try (InputStream inputStream = CommonJson.class.getResourceAsStream("/common.json")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: /common.json");
            }
            // 스트림에서 모든 바이트를 읽고 문자열로 변환
            byte[] bytes = inputStream.readAllBytes();
            String jsonString = new String(bytes, StandardCharsets.UTF_8);
            return new JSONObject(jsonString);
        }
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
