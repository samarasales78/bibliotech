package model;

import java.util.ArrayList;

// Representa a base abstrata dos usuários cadastrados no sistema.
public abstract class Usuario {

    /* O ID identifica individualmente o usuário e permite
    relacioná-lo aos empréstimos registrados no sistema. */
    private int id;

    // Armazena o nome completo do usuário.
    private String nome;

    /* O CPF identifica o usuário e pode ser utilizado
    para evitar cadastros duplicados. */
    private String cpf;

    /* Armazena o gênero literário de preferência do usuário.
    Essa informação é utilizada para gerar recomendações. */
    private String generoFavorito;

    // Armazena a pontuação acumulada pelo usuário.
    private int pontos;

    /* Armazena os IDs dos livros adicionados aos favoritos.
    Os IDs permitem localizar os livros no acervo. */
    private ArrayList<Integer> livrosFavoritos;

    // Construtor padrão.
    public Usuario() {
        livrosFavoritos = new ArrayList<>();
    }

    /* Cria um usuário com seus dados principais.
    A lista de favoritos é iniciada vazia. */
    public Usuario(
            int id,
            String nome,
            String cpf,
            String generoFavorito
    ) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.generoFavorito = generoFavorito;
        this.pontos = 0;
        this.livrosFavoritos = new ArrayList<>();
    }

    /* Reconstrói um usuário com todos os dados armazenados.
    Os IDs dos favoritos permitem recuperar os livros correspondentes. */
    public Usuario(
            int id,
            String nome,
            String cpf,
            String generoFavorito,
            int pontos,
            ArrayList<Integer> livrosFavoritos
    ) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.generoFavorito = generoFavorito;
        this.pontos = pontos;
        this.livrosFavoritos = livrosFavoritos;
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getGeneroFavorito() {
        return generoFavorito;
    }

    public void setGeneroFavorito(String generoFavorito) {
        this.generoFavorito = generoFavorito;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public ArrayList<Integer> getLivrosFavoritos() {
        return livrosFavoritos;
    }

    public void setLivrosFavoritos(
            ArrayList<Integer> livrosFavoritos
    ) {
        this.livrosFavoritos = livrosFavoritos;
    }

    // Adiciona pontos à pontuação atual do usuário.
    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }

    // Adiciona um livro aos favoritos caso ele ainda não esteja na lista.
    public void adicionarFavorito(int idLivro) {
        if (!livrosFavoritos.contains(idLivro)) {
            livrosFavoritos.add(idLivro);
        }
    }

    // Remove um livro da lista de favoritos.
    public void removerFavorito(int idLivro) {
        livrosFavoritos.remove(Integer.valueOf(idLivro));
    }

    // Verifica se um determinado livro está entre os favoritos.
    public boolean possuiFavorito(int idLivro) {
        return livrosFavoritos.contains(idLivro);
    }

    /* Define um comportamento obrigatório para as subclasses.
    Cada tipo de usuário informa seu próprio tipo de acesso. */
    public abstract String getTipo();

    /* Converte os dados do usuário para uma linha CSV.
    Os IDs dos livros favoritos são separados por vírgulas. */
    public String toCSV() {
        String favoritos = "";

        for (int i = 0; i < livrosFavoritos.size(); i++) {
            favoritos += livrosFavoritos.get(i);

            if (i < livrosFavoritos.size() - 1) {
                favoritos += ",";
            }
        }

        return id + ";"
                + nome + ";"
                + cpf + ";"
                + generoFavorito + ";"
                + pontos + ";"
                + favoritos;
    }

    // Retorna os principais dados do usuário em formato legível.
    @Override
    public String toString() {
        return "ID: " + id
                + " | Nome: " + nome
                + " | CPF: " + cpf
                + " | Gênero favorito: " + generoFavorito
                + " | Pontos: " + pontos
                + " | Favoritos: " + livrosFavoritos.size()
                + " | Tipo: " + getTipo();
    }
}