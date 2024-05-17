package com.driven.auth;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class Credential {

    public GoogleCredentials getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // 클래스패스를 통해 서비스 계정 키 파일을 읽음
        try (InputStream serviceAccountStream = getClass().getResourceAsStream("/driven-379206-34fddc8c7805.json")) {
            if (serviceAccountStream == null) {
                throw new IOException("Resource not found: /driven-379206-34fddc8c7805.json");
            }
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
            return credentials.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        }
    }
}
