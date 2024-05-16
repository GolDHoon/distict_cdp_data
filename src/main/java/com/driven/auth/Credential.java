package com.driven.auth;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class Credential {

    public GoogleCredentials getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        String credentialsFilePath = "src/main/resources/driven-379206-34fddc8c7805.json"; // Replace with path to your service account key file
        GoogleCredentials credentials;

        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsFilePath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        }

        return credentials.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    }
}
