package ManagerApplications;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ApplicationTableModel extends AbstractTableModel {
    private static final int COL_NUM = 3;

    private String[] colNames = {
            "ID", "DateTime", "Command"
    };
    private ArrayList<String[]> ResultSets;

    public ApplicationTableModel(ResultSet rs) {
        ResultSets = new ArrayList<>();
        try {
            while (rs.next()) {
                String[] row = {
                        String.valueOf(rs.getInt("id")), String.valueOf(rs.getTime("date_time")), rs.getString("command")
                };
                ResultSets.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка подключения к БД: " +
                    e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    public Object getValueAt(int rowindex, int columnindex) {

        String[] row = ResultSets.get(rowindex);
        return row[columnindex];

    }

    public int getRowCount() {
        return ResultSets.size();
    }

    public int getColumnCount() {
        return COL_NUM;
    }

    public String getColumnName(int param) {

        return colNames[param];
    }
}
