package br.senac.taskmaster;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private int id;
    private String dataHora;
    private String acao;

    // Construtor padrão
    public Log() {}

    // Construtor com todos os campos
    public Log(int id, String dataHora, String acao) {
        this.id = id;
        this.dataHora = dataHora;
        this.acao = acao;
    }

    // Construtor para inserção (com data e ação)
    public Log(String dataHora, String acao) {
        this.dataHora = dataHora;
        this.acao = acao;
    }

    // ✅ Construtor automático com data/hora atual formatada
    public Log(String acao) {
        this.dataHora = gerarDataHoraAtual();
        this.acao = acao;
    }

    // Método utilitário para gerar data e hora no formato correto
    private String gerarDataHoraAtual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", dataHora='" + dataHora + '\'' +
                ", acao='" + acao + '\'' +
                '}';
    }
}
