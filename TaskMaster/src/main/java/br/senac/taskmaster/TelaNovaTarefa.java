package br.senac.taskmaster;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;

public class TelaNovaTarefa extends JFrame {

    private TelaDashboard dashboard;
    private Usuario usuario;

    public TelaNovaTarefa(TelaDashboard dashboard, Usuario usuario) {
        this.dashboard = dashboard;
        this.usuario = usuario;

        setTitle("Nova Tarefa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color azul = new Color(0, 123, 255);
        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 14);
        Font fonteCampo = new Font("Segoe UI", Font.PLAIN, 13);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Campo título
        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setFont(fonteTitulo);
        JTextField txtTitulo = new JTextField(20);
        txtTitulo.setFont(fonteCampo);
        txtTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Campo descrição
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(fonteTitulo);
        JTextArea txtDescricao = new JTextArea(5, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setFont(fonteCampo);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Campo status
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(fonteTitulo);
        String[] statusOptions = {"Pendente", "Em andamento", "Concluída"};
        JComboBox<String> cbStatus = new JComboBox<>(statusOptions);
        cbStatus.setFont(fonteCampo);
        cbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Campo prazo
        JLabel lblPrazo = new JLabel("Prazo:");
        lblPrazo.setFont(fonteTitulo);
        JPanel prazoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        prazoPanel.setBackground(Color.WHITE);

        String[] dias = new String[31];
        String[] meses = new String[12];
        String[] anos = {"2025", "2026", "2027"};

        for (int i = 0; i < 31; i++) dias[i] = String.format("%02d", i + 1);
        for (int i = 0; i < 12; i++) meses[i] = String.format("%02d", i + 1);

        JComboBox<String> cbDia = new JComboBox<>(dias);
        JComboBox<String> cbMes = new JComboBox<>(meses);
        JComboBox<String> cbAno = new JComboBox<>(anos);

        cbDia.setFont(fonteCampo);
        cbMes.setFont(fonteCampo);
        cbAno.setFont(fonteCampo);

        prazoPanel.add(new JLabel("Dia:"));
        prazoPanel.add(cbDia);
        prazoPanel.add(new JLabel("Mês:"));
        prazoPanel.add(cbMes);
        prazoPanel.add(new JLabel("Ano:"));
        prazoPanel.add(cbAno);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(azul);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setFont(fonteTitulo);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.LIGHT_GRAY);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(fonteTitulo);

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        btnCancelar.addActionListener(e -> dispose());

        // Adiciona componentes ao painel
        panel.add(lblTitulo);
        panel.add(txtTitulo);
        panel.add(Box.createVerticalStrut(15));

        panel.add(lblDescricao);
        panel.add(scrollDescricao);
        panel.add(Box.createVerticalStrut(15));

        panel.add(lblStatus);
        panel.add(cbStatus);
        panel.add(Box.createVerticalStrut(15));

        panel.add(lblPrazo);
        panel.add(prazoPanel);
        panel.add(Box.createVerticalStrut(25));

        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);

        // Ação do botão Salvar
        btnSalvar.addActionListener(e -> {
            String titulo = txtTitulo.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String status = (String) cbStatus.getSelectedItem();

            if (titulo.isEmpty() || descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            try {
                int dia = Integer.parseInt((String) cbDia.getSelectedItem());
                int mes = Integer.parseInt((String) cbMes.getSelectedItem());
                int ano = Integer.parseInt((String) cbAno.getSelectedItem());

                LocalDate localDate = LocalDate.of(ano, mes, dia);
                Date prazo = Date.valueOf(localDate);

                Tarefa tarefa = new Tarefa();
                tarefa.setTitulo(titulo);
                tarefa.setDescricao(descricao);
                tarefa.setStatus(status);
                tarefa.setPrazo(prazo);
                tarefa.setUsuarioId(usuario.getId());

                if (TarefaDAO.inserir(tarefa)) {
                    Log log = new Log("Nova tarefa criada pelo usuário ID " + usuario.getId());
                    LogDAO logDAO = new LogDAO();
                    logDAO.inserir(log);

                    JOptionPane.showMessageDialog(this, "Tarefa salva com sucesso!");
                    dashboard.carregarTarefas();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar tarefa.");
                }

            } catch (DateTimeException dte) {
                JOptionPane.showMessageDialog(this, "A data selecionada é inválida.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao processar os dados.");
            }
        });

        setVisible(true);
    }
}
