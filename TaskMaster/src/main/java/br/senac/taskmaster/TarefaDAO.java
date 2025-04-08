package br.senac.taskmaster;

import java.sql.*;
import java.util.ArrayList;

public class TarefaDAO {

    public static boolean inserir(Tarefa tarefa) {
    String sql = "INSERT INTO Tarefa (titulo, descricao, status, prazo, usuario_id, categoria_id) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = Conexao.obterConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        // Diagnóstico (LOG)
        System.out.println("[DEBUG] Título: " + tarefa.getTitulo());
        System.out.println("[DEBUG] Descrição: " + tarefa.getDescricao());
        System.out.println("[DEBUG] Status: " + tarefa.getStatus());
        System.out.println("[DEBUG] Prazo: " + tarefa.getPrazo());
        System.out.println("[DEBUG] Usuário ID: " + tarefa.getUsuarioId());
        System.out.println("[DEBUG] Categoria ID: " + tarefa.getCategoriaId());

        stmt.setString(1, tarefa.getTitulo());
        stmt.setString(2, tarefa.getDescricao());
        stmt.setString(3, tarefa.getStatus());
        stmt.setDate(4, tarefa.getPrazo());
        stmt.setInt(5, tarefa.getUsuarioId());

        // ⚠️ Se categoria_id for 0, seta como NULL no banco
        if (tarefa.getCategoriaId() == 0) {
            stmt.setNull(6, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(6, tarefa.getCategoriaId());
        }

        stmt.executeUpdate();
        return true;

    } catch (Exception e) {
        System.err.println("Erro ao inserir tarefa: " + e.getMessage());
        return false;
    }
}



    public static ArrayList<Tarefa> listarTodas() {
        ArrayList<Tarefa> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tarefa";
        try (Connection conn = Conexao.obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tarefa t = new Tarefa(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("status"),
                    rs.getDate("prazo"),
                    rs.getInt("usuario_id"),
                    rs.getInt("categoria_id")
                );
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tarefas: " + e.getMessage());
        }
        return lista;
    }

    public static ArrayList<Tarefa> listarPorUsuario(int usuarioId) {
        ArrayList<Tarefa> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tarefa WHERE usuario_id = ?";
        try (Connection conn = Conexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tarefa t = new Tarefa(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("status"),
                    rs.getDate("prazo"),
                    rs.getInt("usuario_id"),
                    rs.getInt("categoria_id")
                );
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tarefas por usuário: " + e.getMessage());
        }
        return lista;
    }

    public static Tarefa buscarPorId(int id) {
        String sql = "SELECT * FROM Tarefa WHERE id = ?";
        try (Connection conn = Conexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tarefa(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("status"),
                    rs.getDate("prazo"),
                    rs.getInt("usuario_id"),
                    rs.getInt("categoria_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefa por ID: " + e.getMessage());
        }
        return null;
    }

    public static void atualizar(Tarefa tarefa) {
        String sql = "UPDATE Tarefa SET titulo=?, descricao=?, status=?, prazo=?, usuario_id=?, categoria_id=? WHERE id=?";
        try (Connection conn = Conexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getStatus());
            stmt.setDate(4, tarefa.getPrazo());
            stmt.setInt(5, tarefa.getUsuarioId());
            stmt.setInt(6, tarefa.getCategoriaId());
            stmt.setInt(7, tarefa.getId());

            stmt.executeUpdate();
            System.out.println("Tarefa atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    public static void deletar(int id) {
        String sql = "DELETE FROM Tarefa WHERE id=?";
        try (Connection conn = Conexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Tarefa deletada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar tarefa: " + e.getMessage());
        }
    }
}
