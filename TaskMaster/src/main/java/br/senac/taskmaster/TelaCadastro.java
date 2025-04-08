package br.senac.taskmaster;

import javax.swing.*;
import java.awt.*;

public class TelaCadastro extends JFrame {
    private JTextField campoNome, campoEmail;
    private JPasswordField campoSenha;
    private JButton btnSalvar, btnVoltar;

    public TelaCadastro() {
        setTitle("TaskMaster - Cadastro");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Cores e fontes
        Color corFundo = new Color(248, 249, 250);
        Color corPrincipal = new Color(0, 123, 255);
        Color corTexto = new Color(33, 37, 41);
        Font fonteGeral = new Font("Segoe UI", Font.PLAIN, 14);
        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 20);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(corFundo);
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titulo = new JLabel("Crie sua conta");
        titulo.setFont(fonteTitulo);
        titulo.setForeground(corTexto);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(titulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel labelNome = new JLabel("Nome:");
        labelNome.setFont(fonteGeral);
        labelNome.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(labelNome);

        campoNome = new JTextField();
        campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelPrincipal.add(campoNome);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelEmail = new JLabel("E-mail:");
        labelEmail.setFont(fonteGeral);
        labelEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelPrincipal.add(campoEmail);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(fonteGeral);
        labelSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelPrincipal.add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelPrincipal.add(campoSenha);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        painelBotoes.setBackground(corFundo);

        btnSalvar = new JButton("Cadastrar");
        btnVoltar = new JButton("Voltar");

        estilizarBotao(btnSalvar, corPrincipal, Color.WHITE);
        estilizarBotao(btnVoltar, corPrincipal.darker(), Color.WHITE);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);

        painelPrincipal.add(painelBotoes);
        add(painelPrincipal);

        // Ações
        btnSalvar.addActionListener(e -> cadastrarUsuario());
        btnVoltar.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });
    }

    private void estilizarBotao(JButton botao, Color fundo, Color texto) {
        botao.setFocusPainted(false);
        botao.setBackground(fundo);
        botao.setForeground(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(120, 35));
    }

    private void cadastrarUsuario() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        if (UsuarioDAO.cadastrar(usuario)) {
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
            new TelaLogin().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário.");
        }
    }
}
