package br.senac.taskmaster;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.HashMap;

public class TelaEditarTarefa extends JFrame {
    private JTextField campoTitulo, campoPrazo;
    private JTextArea campoDescricao;
    private JComboBox<String> campoStatus, campoCategoria;
    private JButton btnSalvar, btnCancelar;

    private Tarefa tarefa;
    private TelaDashboard dashboard;

    // Mapeamento de nome -> id
    private HashMap<String, Integer> categorias = new HashMap<>();

    public TelaEditarTarefa(TelaDashboard dashboard, Tarefa tarefa) {
        this.dashboard = dashboard;
        this.tarefa = tarefa;

        setTitle("Editar Tarefa");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel painel = new JPanel(new GridLayout(7, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        campoTitulo = new JTextField(tarefa.getTitulo());
        campoDescricao = new JTextArea(tarefa.getDescricao(), 3, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);

        campoStatus = new JComboBox<>(new String[]{"Pendente", "Em andamento", "Concluída"});
        campoStatus.setSelectedItem(tarefa.getStatus());

        campoCategoria = new JComboBox<>();
        categorias.put("Trabalho", 1);
        categorias.put("Pessoal", 2);
        categorias.put("Estudos", 3);
        for (String nome : categorias.keySet()) {
            campoCategoria.addItem(nome);
        }

        // Selecionar a categoria atual
        for (String nome : categorias.keySet()) {
            if (categorias.get(nome) == tarefa.getCategoriaId()) {
                campoCategoria.setSelectedItem(nome);
                break;
            }
        }

        campoPrazo = new JTextField(tarefa.getPrazo().toString());

        painel.add(new JLabel("Título:"));
        painel.add(campoTitulo);

        painel.add(new JLabel("Descrição:"));
        painel.add(new JScrollPane(campoDescricao));

        painel.add(new JLabel("Status:"));
        painel.add(campoStatus);

        painel.add(new JLabel("Categoria:"));
        painel.add(campoCategoria);

        painel.add(new JLabel("Prazo (YYYY-MM-DD):"));
        painel.add(campoPrazo);

        painel.add(new JLabel()); // espaço vazio

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações
        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvarAlteracoes() {
        if (campoTitulo.getText().trim().isEmpty() || campoPrazo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e prazo são obrigatórios.");
            return;
        }

        tarefa.setTitulo(campoTitulo.getText().trim());
        tarefa.setDescricao(campoDescricao.getText().trim());
        tarefa.setStatus((String) campoStatus.getSelectedItem());

        try {
            String textoPrazo = campoPrazo.getText().trim();
            Date prazo = Date.valueOf(textoPrazo); // formato YYYY-MM-DD
            tarefa.setPrazo(prazo);

            // Definir o ID da categoria com base na seleção
            String categoriaSelecionada = (String) campoCategoria.getSelectedItem();
            int categoriaId = categorias.get(categoriaSelecionada);
            tarefa.setCategoriaId(categoriaId);

            TarefaDAO.atualizar(tarefa);
            LogDAO logDAO = new LogDAO();
            logDAO.inserir(new Log("Edição da tarefa ID: " + tarefa.getId()));
            dashboard.carregarTarefas();
            JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!");
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato YYYY-MM-DD.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar tarefa: " + e.getMessage());
        }
    }
}
