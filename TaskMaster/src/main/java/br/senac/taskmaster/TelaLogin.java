package br.senac.taskmaster;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton btnLogin, btnCadastro;

    public TelaLogin() {
        setTitle("TaskMaster - Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Cores e fontes
        Color corFundo = new Color(248, 249, 250); // cinza claro
        Color corPrincipal = new Color(0, 123, 255); // azul
        Color corTexto = new Color(33, 37, 41); // cinza escuro
        Font fonteGeral = new Font("Segoe UI", Font.PLAIN, 14);
        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 20);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(corFundo);
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titulo = new JLabel("Bem-vindo ao TaskMaster");
        titulo.setFont(fonteTitulo);
        titulo.setForeground(corTexto);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(titulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));

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

        btnCadastro = new JButton("Cadastrar");
        btnLogin = new JButton("Entrar");

        estilizarBotao(btnCadastro, corPrincipal, Color.WHITE);
        estilizarBotao(btnLogin, corPrincipal.darker(), Color.WHITE);

        painelBotoes.add(btnCadastro);
        painelBotoes.add(btnLogin);

        painelPrincipal.add(painelBotoes);
        add(painelPrincipal);

        // Ações
        btnLogin.addActionListener(e -> realizarLogin());
        btnCadastro.addActionListener(e -> {
            new TelaCadastro().setVisible(true);
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

    private void realizarLogin() {
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        Usuario usuario = UsuarioDAO.autenticar(email, senha);
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            new TelaDashboard(usuario).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou senha incorretos.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
