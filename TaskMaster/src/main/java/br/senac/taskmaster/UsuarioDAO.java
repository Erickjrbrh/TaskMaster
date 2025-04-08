package br.senac.taskmaster;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {

    public static void inserir(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();
            System.out.println("Usuário inserido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
        }
    }

    // ✅ NOVO: usado na TelaCadastro
    public static boolean cadastrar(Usuario usuario) {
        try {
            inserir(usuario);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }

    // ✅ NOVO: usado na TelaLogin
   public static Usuario autenticar(String email, String senha) {
    String sql = "SELECT * FROM Usuario WHERE email = ? AND senha = ?";
    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        stmt.setString(2, senha);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setNome(rs.getString("nome"));
            usuario.setEmail(rs.getString("email"));
            usuario.setSenha(rs.getString("senha"));
            return usuario;
        }

    } catch (SQLException e) {
        System.err.println("Erro ao autenticar usuário: " + e.getMessage());
    }

    return null; // Retorna null se não encontrou o usuário
}


    public static ArrayList<Usuario> listarTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
                u.setId(rs.getInt("id"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }
        return lista;
    }

    public static void atualizar(Usuario usuario) {
        String sql = "UPDATE Usuario SET nome=?, email=?, senha=? WHERE id=?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
            System.out.println("Usuário atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public static void deletar(int id) {
        String sql = "DELETE FROM Usuario WHERE id=?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Usuário deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}
