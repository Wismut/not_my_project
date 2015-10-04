package ManagerApplications;

import javax.swing.*;
import java.awt.*;

public abstract class Exceptions {

    public void exceptionsBtnStart(Exception e) {
        showMessage(null, "Неудалось выполнить запуск: " + e.getMessage(), "Ошибка запуска таймера",
                JOptionPane.ERROR_MESSAGE);
    }

    public void exceptionsBtnAdd(Exception e) {
        showMessage(null, "Неудалось добавить: " + e.getMessage(), "Ошибка добавления",
                JOptionPane.ERROR_MESSAGE);
    }

    public void exceptionsBtnDelete(Exception e) {
        showMessage(null, "Неудалось удалить: " + e.getMessage(), "Ошибка удаления",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showMessageDialogNotIsFile() {
        showMessage(null, "Требуется выбрать файл, а не папку.", "Ошибка выбора файла",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showMessageDialogErrorDelete(Exception e) {
        showMessage(null, "Для удаления задачи, выберете ID: " + e.getMessage(), "Не выбран ID",
                JOptionPane.ERROR_MESSAGE);
    }

    public void exceptionsAddTableToForm(Exception e) {
        showMessage(null, "Ошибка добавления таблицы на форму. " + e.getMessage(), "Ошибка добавления таблицы",
                JOptionPane.ERROR_MESSAGE);
    }

    public void exceptionsRunTimer(Exception e) {
        showMessage(null, "Невозможно запустить таймер: " + e.getMessage(), "Ошибка запуска таймера",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(Component parentComponent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }
}
