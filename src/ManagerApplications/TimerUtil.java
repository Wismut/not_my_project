package ManagerApplications;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class TimerUtil extends Exceptions {
    private int hour, minute, second;
    MyDBConnection myDBConnection;
    Statement statement;
    //ResultSet resultSet;
    Timer timer;
    ArrayList<String> time_start = new ArrayList<>();
    int count_start = 0;


    public TimerUtil() throws SQLException {
        myDBConnection = new MyDBConnection();
        myDBConnection.init();
        Connection conn = myDBConnection.getMyConnection();
        statement = conn.createStatement();
    }

    public void SetTimer() throws Exception {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    String[] command;
                    String s = "\"";
                    PreparedStatement pstmt = myDBConnection.getMyConnection().prepareStatement(
                            myDBConnection.getQuery("SELECT command FROM managers where date_time = ?")); //
                    Collections.sort(time_start); //
                    pstmt.setTime(1, Time.valueOf(time_start.get(count_start))); //
                    count_start++; //
                    ResultSet res_prep = pstmt.executeQuery(); //
                    while (res_prep.next()) {
                        String path = res_prep.getString("command");
                        Runtime.getRuntime().exec(path); //
                    }
                    /*resultSet = statement.executeQuery(
                            myDBConnection.getQuery("SELECT command FROM managers"));
                    while (resultSet.next()) {
                        command = new String[]{s + resultSet.getString(1) + s};
                        System.out.println(command[0]);
                        //command = new String[]{"cmd.exe", "/C", s + resultSet.getString(1) + s};
                        Process process = Runtime.getRuntime().exec(command);
                    }*/
                    //Statement st = myDBConnection.myConnection.createStatement();
                    //st.executeUpdate("DELETE FROM managers");
                } catch (Exception e) {
                    exceptionsBtnStart(e);
                }
            }
        };
        Date time = getTime();
        timer = new Timer();
        timer.schedule(timerTask, time);
    }

    private Date getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Date time = calendar.getTime();
        return time;
    }

    public void parseDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = sdf.parse(dateString);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        time_start.add(hour + ":" + minute + ":" + second);
    }
}
