package com.driven.distirct_logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataConversion {
    public List<List<Object>> toLookerSheetDataForm(List<List<Object>> visitor, List<List<Object>> ticketSold, String reportType){
        int dateIndexVistor = 0;
        int dateIndexTicketSold = 0;
        int onlineIndexVisitor = 0;
        int onlineIndexTicketSold = 0;
        int onsiteIndexVisitor = 0;
        int onsiteIndexTicketSold = 0;
        visitor.remove(visitor.size()-1);
        ticketSold.remove(ticketSold.size()-1);
        List<List<Object>> result = new ArrayList<>();
        int loopIndex = 0;
        switch (reportType){
            case "lv":
                for(int i = 0; i < visitor.get(0).size(); i++){
                    String Header = (String) visitor.get(0).get(i);

                    if(Header.equals("date")){
                        dateIndexVistor = i;
                    }else if (Header.equals("Online")){
                        onlineIndexVisitor = i;
                    }
                }

                for(int i = 0; i < ticketSold.get(0).size(); i++){
                    String Header = (String) ticketSold.get(0).get(i);

                    if(Header.equals("date")){
                        dateIndexTicketSold = i;
                    }else if (Header.equals("Online")){
                        onlineIndexTicketSold = i;
                    }
                }

                while (true){
                    if (visitor.size() == 1 || ticketSold.size() == 1){
                        break;
                    }
                    List<Object> resultRows = new ArrayList<>();
                    if(loopIndex == 0){
                        resultRows.add("date");
                        resultRows.add("visitor");
                        resultRows.add("ticket sold");
                        result.add(resultRows);
                        loopIndex++;

                        continue;
                    }

                    LocalDate dateVistor = LocalDate.parse((String) visitor.get(loopIndex).get(dateIndexVistor));
                    boolean beforeCheker = false;

                    int loopIndex2 = 1;
                    while (true){
                        if(ticketSold.size() == 1){
                            beforeCheker = true;
                            break;
                        }
                        LocalDate dateTicketSold = LocalDate.parse((String) ticketSold.get(loopIndex2).get(dateIndexTicketSold));

                        if(dateVistor.isEqual(dateTicketSold)){
                            resultRows.add(visitor.get(1).get(dateIndexVistor));
                            resultRows.add(Integer.parseInt((String) visitor.get(1).get(onlineIndexVisitor)));
                            resultRows.add(Integer.parseInt((String) ticketSold.get(loopIndex2).get(onlineIndexTicketSold)));

                            visitor.remove(1);
                            ticketSold.remove(1);
                            result.add(resultRows);
                            break;
                        }else if(dateVistor.isAfter(dateTicketSold)){
                            resultRows.add(ticketSold.get(loopIndex2).get(dateIndexTicketSold));
                            resultRows.add(0);
                            resultRows.add(Integer.parseInt((String) ticketSold.get(loopIndex2).get(onlineIndexTicketSold)));

                            ticketSold.remove(loopIndex2);
                            result.add(resultRows);
                            break;
                        }
                        loopIndex2++;
                    }

                    if(beforeCheker){
                        resultRows.add(visitor.get(1).get(dateIndexVistor));
                        resultRows.add(Integer.parseInt((String) visitor.get(1).get(onlineIndexVisitor)));
                        resultRows.add(0);
                        visitor.remove(1);
                    }

                    if(visitor.size() == 1 && ticketSold.size() > 1){
                        resultRows.add(ticketSold.get(1).get(dateIndexTicketSold));
                        resultRows.add(0);
                        resultRows.add(Integer.parseInt((String) ticketSold.get(1).get(onlineIndexTicketSold)));

                        ticketSold.remove(1);
                        result.add(resultRows);
                    }
                }

                break;
            case "db":
                for(int i = 0; i < visitor.get(0).size(); i++){
                    String Header = (String) visitor.get(0).get(i);

                    if(Header.equals("date")){
                        dateIndexVistor = i;
                    }else if (Header.equals("Online")){
                        onlineIndexVisitor = i;
                    }else if (Header.equals("Onsite")){
                        onsiteIndexVisitor = i;
                    }
                }

                for(int i = 0; i < ticketSold.get(0).size(); i++){
                    String Header = (String) ticketSold.get(0).get(i);

                    if(Header.equals("date")){
                        dateIndexTicketSold = i;
                    }else if (Header.equals("Online")){
                        onlineIndexTicketSold = i;
                    }else if (Header.equals("Onsite")){
                        onsiteIndexTicketSold = i;
                    }
                }

                while (true){
                    if (visitor.size() == 1 || ticketSold.size() == 1){
                        break;
                    }
                    List<Object> resultRows = new ArrayList<>();
                    if(loopIndex == 0){
                        resultRows.add("date");
                        resultRows.add("visitor");
                        resultRows.add("online");
                        resultRows.add("onsite");
                        result.add(resultRows);
                        loopIndex++;

                        continue;
                    }

                    LocalDate dateVistor = LocalDate.parse((String) visitor.get(loopIndex).get(dateIndexVistor));
                    boolean beforeCheker = false;

                    int loopIndex2 = 1;
                    while (true){
                        if(ticketSold.size() == 1){
                            beforeCheker = true;
                            break;
                        }
                        LocalDate dateTicketSold = LocalDate.parse((String) ticketSold.get(loopIndex2).get(dateIndexTicketSold));

                        if(dateVistor.isEqual(dateTicketSold)){
                            resultRows.add(visitor.get(1).get(dateIndexVistor));
                            resultRows.add((Integer.parseInt((String) (visitor.get(1).get(onlineIndexVisitor)))) + (Integer.parseInt((String) (visitor.get(1).get(onsiteIndexVisitor)))));
                            resultRows.add(Integer.parseInt((String) ticketSold.get(loopIndex2).get(onlineIndexTicketSold)));
                            resultRows.add(Integer.parseInt((String) ticketSold.get(loopIndex2).get(onsiteIndexTicketSold)));

                            visitor.remove(1);
                            ticketSold.remove(1);
                            result.add(resultRows);
                            break;
                        }else if(dateVistor.isAfter(dateTicketSold)){
                            resultRows.add(ticketSold.get(loopIndex2).get(dateIndexTicketSold));
                            resultRows.add(0);
                            resultRows.add(Integer.parseInt((String) ticketSold.get(loopIndex2).get(onlineIndexTicketSold)));
                            resultRows.add(Integer.parseInt((String) ticketSold.get(loopIndex2).get(onsiteIndexTicketSold)));

                            ticketSold.remove(loopIndex2);
                            result.add(resultRows);
                            break;
                        }
                        loopIndex2++;
                    }

                    if(beforeCheker){
                        resultRows.add(visitor.get(1).get(dateIndexVistor));
                        resultRows.add((Integer.parseInt((String) (visitor.get(1).get(onlineIndexVisitor)))) + (Integer.parseInt((String) (visitor.get(1).get(onsiteIndexVisitor)))));
                        resultRows.add(0);
                        resultRows.add(0);
                        visitor.remove(1);
                    }

                    if(visitor.size() == 1 && ticketSold.size() > 1){
                        resultRows.add(ticketSold.get(1).get(dateIndexTicketSold));
                        resultRows.add(0);
                        resultRows.add(Integer.parseInt((String) ticketSold.get(1).get(onlineIndexTicketSold)));
                        resultRows.add(Integer.parseInt((String) ticketSold.get(1).get(onsiteIndexTicketSold)));

                        ticketSold.remove(1);
                        result.add(resultRows);
                    }
                }

                break;
            default:
                break;
        }
        return result;
    }
}
