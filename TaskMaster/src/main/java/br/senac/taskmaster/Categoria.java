package br.senac.taskmaster;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Categoria {
    private int id;
    private String nome;

    private static final Logger LOGGER = Logger.getLogger(Categoria.class.getName());

    // Construtores
    public Categoria() {}

    public Categoria(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Verifica se a categoria já existe no banco de dados pelo nome.
     */
    public static boolean categoriaExiste(final Connection conn, final String nome) {
        final String sql = "SELECT COUNT(*) FROM Categoria WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar categoria: ", e);
        }
        return false;
    }

    /**
     * Insere uma nova categoria no banco de dados se ela não existir.
     */
    public static void inserirCategoria(final Connection conn, final Categoria categoria) {
        if (categoriaExiste(conn, categoria.getNome())) {
            LOGGER.info("Categoria '" + categoria.getNome() + "' já existe.");
            return;
        }

        final String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();
            LOGGER.info("Categoria '" + categoria.getNome() + "' inserida com sucesso!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir categoria: ", e);
        }
    }

    /**
     * Lista todas as categorias cadastradas no banco de dados.
     */
    public static void listarCategorias(final Connection conn) {
        final String sql = "SELECT * FROM Categoria";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Categorias cadastradas:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nome"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar categorias: ", e);
        }
    }

    /**
     * Busca o ID de uma categoria a partir do nome.
     */
    public static int buscarCategoriaPorNome(final Connection conn, final String nome) {
        final String sql = "SELECT id FROM Categoria WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar categoria: ", e);
        }
        return -1; // Retorna -1 caso a categoria não exista
    }

    /**
     * Exclui uma categoria com base no ID informado.
     */
    public static void excluirCategoria(final Connection conn, final int id) {
        final String sql = "DELETE FROM Categoria WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Categoria removida com sucesso!");
            } else {
                LOGGER.warning("Nenhuma categoria encontrada com o ID informado.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir categoria: ", e);
        }
    }
}
