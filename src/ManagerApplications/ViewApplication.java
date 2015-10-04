package ManagerApplications;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ViewApplication extends Exceptions {
    public JFrame frame;
    private JPanel panel;
    public JTable table;
    private JTextField txtAbsolutePath;
    private JScrollPane scrollPane;
    public Statement statement;
    private MyDBConnection myDBConnection;
    private JSpinner jSpinner1;
    public ResultSet resultSet;
    public JFileChooser fileChooser;
    TimerUtil timer = new TimerUtil();
    JButton buttonStart;
    JButton buttonAdd;


    public ViewApplication() throws Exception {
        frame = new JFrame();
        myDBConnection = new MyDBConnection();
        myDBConnection.init();
        Connection conn = myDBConnection.getMyConnection();
        statement = conn.createStatement();
        initComponents();
    }

    public void initComponents() throws Exception {
        panel = new JPanel();
        buttonAdd = new JButton();
        buttonStart = new JButton();
        JButton btnCancel = new JButton();
        JButton btnDelete = new JButton();
        txtAbsolutePath = new JTextField();
        JButton btnView = new JButton();
        table = new JTable();
        scrollPane = new JScrollPane();
        JLabel lbltime = new JLabel();

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing();
            }
        });
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new GridLayout(4, 2));

        btnView.setText("Выбрать файл ...");
        panel.add(btnView);

        panel.add(txtAbsolutePath);
        txtAbsolutePath.setEditable(false);

        lbltime.setText("Выберите время запуска:");
        panel.add(lbltime);

        addSpinnerDateToForm();

        buttonAdd.setText("Добавить данные");
        panel.add(buttonAdd);

        buttonStart.setText("Запустить");
        panel.add(buttonStart);

        btnCancel.setText("Отменить");
        panel.add(btnCancel);

        btnDelete.setText("Удалить");
        panel.add(btnDelete);

        addTableToForm();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((screenSize.width - 400) / 2, (screenSize.height - 400) / 2, 400, 400);

        /** Реализация кнопок **/
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                implementationBtnAdd();
            }
        });

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                implementationBtnStart();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.timer.cancel();
                JOptionPane.showMessageDialog(null, "Задача успешно отменена!");
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    implementationBtnDelete();
                } catch (Exception e1) {
                    showMessageDialogErrorDelete(e1);
                }

            }
        });

        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                implementationBtnView();
            }
        });
    }

    private void implementationBtnAdd() {
        String runs = txtAbsolutePath.getText();
        String date_time = new SimpleDateFormat("HH:mm:ss").format(jSpinner1.getValue());
        try {
            int done = statement.executeUpdate(myDBConnection.getQuery("INSERT INTO managers (date_time, command) VALUES("
                    + quotate(date_time) + ","
                    + quotate(runs)
                    + ")"));
            frame.getContentPane().removeAll();
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionsBtnAdd(e);
        }
    }

    private void implementationBtnStart() {
        try {
            ArrayList<String> dateString = new ArrayList<>();
            resultSet = statement.executeQuery(myDBConnection.getQuery("SELECT date_time FROM managers"));
            while (resultSet.next()) {
                dateString.add(resultSet.getString(1));
            }
            for (String s : dateString) {
                timer.parseDate(s);
                timer.SetTimer();
            }
        } catch (SQLException | ParseException  e) {
            exceptionsBtnStart(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*try {
            timer.SetTimer();
        } catch (Exception e) {
            exceptionsRunTimer(e);
        }*/
    }

    private void implementationBtnDelete() {
        int selectedRow = table.getSelectedRow();
        int selectedCol = table.getSelectedColumn();
        int idd = Integer.parseInt(table.getModel().getValueAt(selectedRow, selectedCol).toString());
        try {
            PreparedStatement pstmt = myDBConnection.getMyConnection().prepareStatement(
                    myDBConnection.getQuery("delete from managers where id = ?"));
            pstmt.setInt(1, idd);
            pstmt.executeUpdate();
            pstmt.close();
            frame.getContentPane().removeAll();
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            initComponents();
        } catch (Exception e) {
            exceptionsBtnDelete(e);
        }
    }

    private void implementationBtnView() {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int ret1 = fileChooser.showDialog(null, "Выбрать файл");
        if (ret1 == JFileChooser.APPROVE_OPTION) {
            File file1 = fileChooser.getSelectedFile();
            if (!file1.isFile()) {
                showMessageDialogNotIsFile();
                implementationBtnView();
            } else {
                txtAbsolutePath.setText(file1.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"));
            }
        }
    }

    private ResultSet addTableToForm() {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(myDBConnection.getQuery("SELECT * FROM managers"));
            table.setModel(new ApplicationTableModel(resultSet));
            myDBConnection.close(resultSet);
            scrollPane.setViewportView(table);
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException e) {
            exceptionsAddTableToForm(e);
        }
        return resultSet;

    }

    private void addSpinnerDateToForm() {
        Date date = new java.util.Date();
        SpinnerDateModel sm =
                new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        jSpinner1 = new javax.swing.JSpinner(sm);
        JSpinner.DateEditor de = new JSpinner.DateEditor(jSpinner1, "HH:mm:ss");
        String format = new SimpleDateFormat("HH:mm:ss").format(jSpinner1.getValue());
        jSpinner1.setEditor(de);
        panel.add(jSpinner1);
    }

    private String quotate(String content) {

        return "'" + content + "'";
    }

    private void formWindowClosing() {
        myDBConnection.close(statement);
        myDBConnection.destroy();
    }

}

