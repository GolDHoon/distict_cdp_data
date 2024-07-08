package com.driven;

import com.driven.distirct_logic.DataConversion;
import com.driven.googlesheet.GoogleSheet;
import com.driven.json.CommonJson;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        LocalDateTime now1 = LocalDateTime.now();
        System.out.println("start time : " + now1);
        GoogleSheet googleSheet = new GoogleSheet();
        CommonJson commonJson = new CommonJson();
        DataConversion dataConversion = new DataConversion();

        JSONObject jsonObject = commonJson.getJsonObject();
        JSONObject sheetId = commonJson.getValue(jsonObject, "sheet_id");
        JSONObject gid = commonJson.getValue(jsonObject, "gid");

        List<List<Object>> visitorData = null;
        List<List<Object>> ticketSoldData = null;
        try {
            String lvSheetId = (String) commonJson.getValue(sheetId, "lv");

            System.out.println("라스베가스 시작 : " + LocalDateTime.now());
            visitorData = googleSheet.getListData(lvSheetId, "pivot!A3:D");
            ticketSoldData = googleSheet.getListData(lvSheetId, "pivot!E3:H");
            List<List<Object>> convertLvDataFrom = dataConversion.toLookerSheetDataForm(visitorData,ticketSoldData, "lv");

            googleSheet.updateSheetData(lvSheetId, "looker!A2:C", convertLvDataFrom);


            int lvGid = (int) commonJson.getValue(gid, "lv");

            googleSheet.setFormatColumn(lvSheetId, lvGid, 1, "DATE", "yyyy-mm-dd");
            googleSheet.setFormatColumn(lvSheetId, lvGid, 2, "NUMBER", "0");
            googleSheet.setFormatColumn(lvSheetId, lvGid, 3, "NUMBER", "0");
            System.out.println("라스베가스 완료 : " + LocalDateTime.now());
        } catch (GeneralSecurityException e) {
            try {
                dataConversion.sendNotification("sch@driven.co.kr", e);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } catch (IOException e) {
            try {
                dataConversion.sendNotification("sch@driven.co.kr", e);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        try {
            String dbSheetId = (String) commonJson.getValue(sheetId, "db");

            System.out.println("두바이 시작 : " + LocalDateTime.now());
            visitorData = googleSheet.getListData(dbSheetId, "pivot!A3:E");
            ticketSoldData = googleSheet.getListData(dbSheetId, "pivot!F3:J");
            List<List<Object>> convertDbDataFrom = dataConversion.toLookerSheetDataForm(visitorData,ticketSoldData, "db");

            int dbGid = (int) commonJson.getValue(gid, "db");

            convertDbDataFrom.remove(0);
            googleSheet.updateSheetData(dbSheetId, "looker!A2:D", convertDbDataFrom);

            googleSheet.setFormatColumn(dbSheetId, dbGid, 1, "DATE", "yyyy-mm-dd");
            googleSheet.setFormatColumn(dbSheetId, dbGid, 2, "NUMBER", "0");
            googleSheet.setFormatColumn(dbSheetId, dbGid, 3, "NUMBER", "0");
            googleSheet.setFormatColumn(dbSheetId, dbGid, 4, "NUMBER", "0");
            System.out.println("두바이 완료 : " + LocalDateTime.now());
        } catch (GeneralSecurityException e) {
            try {
                dataConversion.sendNotification("sch@driven.co.kr", e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } catch (IOException e) {
            try {
                dataConversion.sendNotification("sch@driven.co.kr", e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        LocalDateTime now2 = LocalDateTime.now();
        System.out.println("end time : " + now2);
        Duration duration = Duration.between(now1, now2);
        long seconds = duration.getSeconds();
        System.out.println("총 소요 시간 : " + seconds + "s");
    }
}