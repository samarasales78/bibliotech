package model;

import java.time.LocalDate;

// Representa um empréstimo realizado por um usuário para um determinado livro.
public class Emprestimo {

    /*O empréstimo armazena apenas os IDs do livro e do usuário.
    Esses IDs permitem localizar posteriormente os objetos correspondentes nas listas gerenciadas pela classe Biblioteca. */
    private int idLivro;
    private int idUsuario;

    // Datas utilizadas para controlar o período do empréstimo.
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    /* Indica se o livro já foi devolvido.
    false significa que o empréstimo ainda está ativo. */
    private boolean devolvido;

    // Construtor padrão.
    public Emprestimo() {
    }

    /* Cria um novo empréstimo ainda não finalizado.
    Por padrão, o livro é considerado não devolvido. */
    public Emprestimo(
            int idLivro,
            int idUsuario,
            LocalDate dataEmprestimo,
            LocalDate dataDevolucao
    ) {
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.devolvido = false;
    }

    /* Cria um empréstimo com seu estado de devolução já definido.
    Isso permite reconstruir corretamente registros armazenados.*/
    public Emprestimo(
            int idLivro,
            int idUsuario,
            LocalDate dataEmprestimo,
            LocalDate dataDevolucao,
            boolean devolvido
    ) {
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.devolvido = devolvido;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    /*
     * Altera o estado do empréstimo para devolvido.
     */
    public void registrarDevolucao() {
        this.devolvido = true;
    }

    // Verifica se o empréstimo ainda está ativo.
    public boolean estaAtivo() {
        return !devolvido;
    }

    /* Converte os dados do objeto para uma linha separada por ponto e vírgula.
    Esse formato será utilizado para armazenar o empréstimo em arquivo CSV. */
    public String toCSV() {
        return idLivro + ";"
                + idUsuario + ";"
                + dataEmprestimo + ";"
                + dataDevolucao + ";"
                + devolvido;
    }

    // Retorna os principais dados do empréstimo em formato legível. 
    @Override
    public String toString() {
        return "Livro ID: " + idLivro
                + " | Usuário ID: " + idUsuario
                + " | Empréstimo: " + dataEmprestimo
                + " | Devolução: " + dataDevolucao
                + " | Devolvido: " + (devolvido ? "Sim" : "Não");
    }
}