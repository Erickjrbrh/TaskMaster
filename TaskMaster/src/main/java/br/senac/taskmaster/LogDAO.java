package br.senac.taskmaster;

import java.sql.*;
import java.util.ArrayList;

public class LogDAO {

    // Insere um novo log no banco de dados
    public boolean inserir(Log log) {
        String sql = "INSERT INTO logs (dataHora, acao) VALUES (?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, log.getDataHora());
            stmt.setString(2, log.getAcao());
            stmt.executeUpdate(); // executeUpdate é ideal para INSERT

            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir log: " + e.getMessage());
            return false;
        }
    }

    // Lista todos os logs em ordem decrescente por data e hora
    public ArrayList<Log> listar() {
        ArrayList<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs ORDER BY dataHora DESC";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Log log = new Log(
                    rs.getInt("id"),
                    rs.getString("dataHora"),
                    rs.getString("acao")
                );
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar logs: " + e.getMessage());
        }

        return logs;
    }
}
