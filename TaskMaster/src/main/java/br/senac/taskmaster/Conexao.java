package br.senac.taskmaster;

import java.sql.*;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/TaskMaster";
    private static final String USER = "gestor";
    private static final String PASSWORD = "senhaSegura123";

    // Conexão principal usada pelo sistema
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Método compatível com DAO
    public static Connection obterConexao() throws SQLException {
        return getConexao();
    }

    // Testes de conexão e inserção de dados (executado ao rodar essa classe)
    public static void main(String[] args) {
        System.out.println("Conectando ao banco de dados...");
        try (Connection conn = getConexao()) {
            System.out.println("Conexão bem-sucedida!");

            // Testes com dados simulados
            inserirUsuario(conn, "Carlos Almeida", "carlos@email.com", "senha456");
            listarUsuarios(conn);

            inserirCategoria(conn, "Lazer");
            listarCategorias(conn);

            inserirTarefa(conn, "Ir ao cinema", "Assistir a um filme", "pendente", "2025-04-05", 1, 1);
            listarTarefasPendentes(conn);

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public static void inserirUsuario(Connection conn, String nome, String email, String senha) {
        String sql = "INSERT INTO Usuario (nome, email, senha) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.executeUpdate();
            System.out.println("Usuário '" + nome + "' inserido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
        }
    }

    public static void listarUsuarios(Connection conn) {
        String sql = "SELECT * FROM Usuario";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Usuários cadastrados:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nome") + " - " + rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }
    }

    public static void inserirCategoria(Connection conn, String nome) {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
            System.out.println("Categoria '" + nome + "' inserida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir categoria: " + e.getMessage());
        }
    }

    public static void listarCategorias(Connection conn) {
        String sql = "SELECT * FROM Categoria";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Categorias cadastradas:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
    }

    public static void inserirTarefa(Connection conn, String titulo, String descricao, String status, String prazo, int usuarioId, int categoriaId) {
        String sql = "INSERT INTO Tarefa (titulo, descricao, status, prazo, usuario_id, categoria_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            stmt.setString(2, descricao);
            stmt.setString(3, status);
            stmt.setString(4, prazo);
            stmt.setInt(5, usuarioId);
            stmt.setInt(6, categoriaId);
            stmt.executeUpdate();
            System.out.println("Tarefa '" + titulo + "' adicionada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir tarefa: " + e.getMessage());
        }
    }

    public static void listarTarefasPendentes(Connection conn) {
        String sql = "SELECT * FROM View_TarefasPendentes";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Tarefas pendentes:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " - " +
                    rs.getString("titulo") + " - " +
                    rs.getString("descricao") + " - Prazo: " +
                    rs.getString("prazo") + " - Usuário: " +
                    rs.getString("usuario")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tarefas pendentes: " + e.getMessage());
        }
    }
}
