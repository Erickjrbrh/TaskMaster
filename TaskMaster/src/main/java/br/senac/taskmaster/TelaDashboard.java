package br.senac.taskmaster;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TelaDashboard extends JFrame {
    private JTable tabelaTarefas;
    private DefaultTableModel modelo;
    private JButton btnNova, btnEditar, btnExcluir, btnLogs;
    private Usuario usuario;

    public TelaDashboard(Usuario usuario) {
        this.usuario = usuario;

        setTitle("TaskMaster - Dashboard");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Cores e fontes
        Color corFundo = new Color(248, 249, 250);
        Color corPrincipal = new Color(0, 123, 255);
        Color corTexto = new Color(33, 37, 41);
        Font fonteGeral = new Font("Segoe UI", Font.PLAIN, 14);
        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 20);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(corFundo);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(painelPrincipal);

        // Título
        JLabel titulo = new JLabel("Minhas Tarefas");
        titulo.setFont(fonteTitulo);
        titulo.setForeground(corTexto);
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Tabela
        modelo = new DefaultTableModel(new String[]{"ID", "Título", "Status", "Responsável", "Prazo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaTarefas = new JTable(modelo);
        tabelaTarefas.setRowHeight(28);
        tabelaTarefas.setFont(fonteGeral);
        tabelaTarefas.getTableHeader().setFont(fonteGeral);
        JScrollPane scroll = new JScrollPane(tabelaTarefas);
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painelBotoes.setBackground(corFundo);

        btnNova = new JButton("Nova Tarefa");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnLogs = new JButton("Ver Logs");

        estilizarBotao(btnNova, corPrincipal);
        estilizarBotao(btnEditar, corPrincipal.darker());
        estilizarBotao(btnExcluir, new Color(220, 53, 69)); // vermelho claro
        estilizarBotao(btnLogs, new Color(40, 167, 69)); // verde

        painelBotoes.add(btnNova);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLogs);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        // Carregar dados
        carregarTarefas();

        // Ações
        btnNova.addActionListener(e -> new TelaNovaTarefa(this, usuario).setVisible(true));

        btnEditar.addActionListener(e -> {
            int linha = tabelaTarefas.getSelectedRow();
            if (linha != -1) {
                int id = (int) modelo.getValueAt(linha, 0);
                Tarefa tarefa = TarefaDAO.buscarPorId(id);
                if (tarefa != null) {
                    new TelaEditarTarefa(this, tarefa).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Tarefa não encontrada no banco de dados.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para editar.");
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabelaTarefas.getSelectedRow();
            if (linha != -1) {
                int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja excluir a tarefa?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    int id = (int) modelo.getValueAt(linha, 0);
                    TarefaDAO.deletar(id);
                    new LogDAO().inserir(new Log("Exclusão da tarefa ID " + id + " pelo usuário ID " + usuario.getId()));
                    carregarTarefas();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para excluir.");
            }
        });

        btnLogs.addActionListener(e -> new TelaLogs().setVisible(true));
    }

    private void estilizarBotao(JButton botao, Color corFundo) {
        botao.setFocusPainted(false);
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(140, 35));
    }

    public void carregarTarefas() {
        modelo.setRowCount(0);
        ArrayList<Tarefa> tarefas = TarefaDAO.listarPorUsuario(usuario.getId());
        for (Tarefa t : tarefas) {
            modelo.addRow(new Object[]{
                t.getId(),
                t.getTitulo(),
                t.getStatus(),
                usuario.getNome(),
                t.getPrazo()
            });
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
