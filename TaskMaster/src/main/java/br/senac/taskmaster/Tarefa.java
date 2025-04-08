package br.senac.taskmaster;

import java.sql.Date;

public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;
    private String status;
    private Date prazo;
    private int usuarioId;
    private int categoriaId;

    // ✅ Construtor padrão (necessário para TelaNovaTarefa)
    public Tarefa() {
    }

    // Construtor completo
    public Tarefa(int id, String titulo, String descricao, String status, Date prazo, int usuarioId, int categoriaId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prazo = prazo;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getStatus() { return status; }
    public Date getPrazo() { return prazo; }
    public int getUsuarioId() { return usuarioId; }
    public int getCategoriaId() { return categoriaId; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(String status) { this.status = status; }
    public void setPrazo(Date prazo) { this.prazo = prazo; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
}
