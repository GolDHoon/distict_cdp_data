package com.driven.googlesheet;

import com.driven.auth.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleSheet {
    private final String APPLICATION_NAME = "CDP data reprocessing"; // 애플리케이션 이름 지정
    private Credential credential = new Credential();

    public List<List<Object>> getListData(String SPREADSHEET_ID, String range) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credential.getCredentials(HTTP_TRANSPORT)))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
        List<List<Object>> values = response.getValues();

        return values;
    }

    public UpdateValuesResponse updateSheetData(String spreadsheetId,
                                                    String range,
                                                    List<List<Object>> values)
            throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Create the sheets API client
        Sheets service = new Sheets.Builder(new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credential.getCredentials(HTTP_TRANSPORT)))
                .setApplicationName(APPLICATION_NAME)
                .build();

        UpdateValuesResponse result = null;
        try {
            // Updates the values in the specified range.
            ValueRange body = new ValueRange()
                    .setValues(values);
            result = service.spreadsheets().values().update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
            } else {
                throw e;
            }
        }
        return result;
    }

    public void setFormatColumn(String spreadsheetId, int gid, int columnIndex, String type, String Pattern) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Create the sheets API client
        Sheets service = new Sheets.Builder(new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credential.getCredentials(HTTP_TRANSPORT)))
                .setApplicationName(APPLICATION_NAME)
                .build();

        NumberFormat numberFormat = null;
        if(type.equals("DATE")||type.equals("NUMBER")){
            numberFormat = new NumberFormat().setType(type).setPattern(Pattern);
        }

        GridRange gridRange = new GridRange()
                .setSheetId(gid)
                .setStartColumnIndex(columnIndex-1)
                .setEndColumnIndex(columnIndex);

        CellData cellData = new CellData()
                .setUserEnteredFormat(new CellFormat()
                        .setNumberFormat(numberFormat));

        RepeatCellRequest repeatCellRequest = new RepeatCellRequest()
                .setRange(gridRange)
                .setCell(cellData)
                .setFields("userEnteredFormat(numberFormat)");

        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setRepeatCell(repeatCellRequest));

        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        BatchUpdateSpreadsheetResponse response = service.spreadsheets()
                .batchUpdate(spreadsheetId, batchUpdateRequest)
                .execute();
    }
}
