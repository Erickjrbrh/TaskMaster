package br.senac.taskmaster;

import br.senac.taskmaster.Log;
import br.senac.taskmaster.LogDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TelaLogs extends JFrame {

    private JTable tabelaLogs;
    private DefaultTableModel modeloTabela;

    public TelaLogs() {
        setTitle("TaskMaster - Histórico de Logs");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Data/Hora", "Ação"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição direta na tabela
            }
        };

        tabelaLogs = new JTable(modeloTabela);
        tabelaLogs.setRowHeight(24);
        tabelaLogs.getTableHeader().setReorderingAllowed(false); // bloqueia arrastar colunas

        JScrollPane scroll = new JScrollPane(tabelaLogs);
        scroll.setBorder(BorderFactory.createTitledBorder("Logs do sistema"));

        add(scroll, BorderLayout.CENTER);

        carregarLogs();
    }

    private void carregarLogs() {
        modeloTabela.setRowCount(0); // limpa a tabela antes de carregar

        LogDAO logDAO = new LogDAO();
        ArrayList<Log> logs = logDAO.listar();

        for (Log log : logs) {
            modeloTabela.addRow(new Object[]{
                log.getId(),
                log.getDataHora(),
                log.getAcao()
            });
        }
    }
}
