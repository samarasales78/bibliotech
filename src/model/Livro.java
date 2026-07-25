package model;

// Representa um livro disponível no acervo da biblioteca.
public class Livro {

    /* O ID identifica individualmente o livro e permite
    relacioná-lo aos empréstimos e favoritos dos usuários. */
    private int id;

    // Armazena o título da obra.
    private String titulo;

    // Armazena o nome do autor da obra.
    private String autor;

    /* Armazena o gênero literário do livro.
    Essa informação é utilizada nas recomendações. */
    private String genero;

    // Indica se o livro está disponível para empréstimo.
    private boolean disponivel;

    // Registra quantas vezes o livro já foi emprestado.
    private int quantidadeEmprestimos;

    // Construtor padrão.
    public Livro() {
        this.disponivel = true;
    }

    /* Cria um livro com seus dados principais.
    O livro começa disponível e sem empréstimos registrados. */
    public Livro(
            int id,
            String titulo,
            String autor,
            String genero
    ) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.disponivel = true;
        this.quantidadeEmprestimos = 0;
    }

    /* Reconstrói um livro com os dados armazenados.
    Permite recuperar seu estado de disponibilidade e histórico. */
    public Livro(
            int id,
            String titulo,
            String autor,
            String genero,
            boolean disponivel,
            int quantidadeEmprestimos
    ) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.disponivel = disponivel;
        this.quantidadeEmprestimos =
                quantidadeEmprestimos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public int getQuantidadeEmprestimos() {
        return quantidadeEmprestimos;
    }

    public void setQuantidadeEmprestimos(
            int quantidadeEmprestimos
    ) {
        this.quantidadeEmprestimos =
                quantidadeEmprestimos;
    }

    /*
     * Marca o livro como emprestado e registra
     * mais uma utilização no histórico.
     */
    public void registrarEmprestimo() {
        this.disponivel = false;
        this.quantidadeEmprestimos++;
    }

    // Torna o livro novamente disponível para empréstimo.
    public void disponibilizar() {
        this.disponivel = true;
    }

    /* Converte os dados do livro para uma linha CSV.
    A ordem dos campos deve ser mantida durante a leitura. */
    public String toCSV() {
        return id + ";"
                + titulo + ";"
                + autor + ";"
                + genero + ";"
                + disponivel + ";"
                + quantidadeEmprestimos;
    }

    // Retorna os principais dados do livro em formato legível.
    @Override
    public String toString() {
        return "ID: " + id
                + " | Título: " + titulo
                + " | Autor: " + autor
                + " | Gênero: " + genero
                + " | Status: "
                + (disponivel
                ? "Disponível"
                : "Emprestado")
                + " | Empréstimos: "
                + quantidadeEmprestimos;
    }
}