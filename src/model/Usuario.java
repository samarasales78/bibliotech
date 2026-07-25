package model;

import java.util.ArrayList;

// Representa um usuário cadastrado no sistema da biblioteca.
public class Usuario {

    /* O identificador permite localizar individualmente o usuário e relacioná-lo aos empréstimos registrados no sistema. */
    private int id;

    // Armazena o nome completo do usuário.
    private String nome;

    /* O CPF identifica o usuário e pode ser utilizado para evitar cadastros duplicados. */
    private String cpf;

    /* Armazena o gênero literário de preferência do usuário.
    Essa informação é utilizada para gerar recomendações de livros. */
    private String generoFavorito;

    /* A pontuação representa a participação do usuário nas atividades realizadas na biblioteca. */
    private int pontos;

    /* Armazena os IDs dos livros adicionados aos favoritos.
    Os IDs permitem localizar os livros correspondentes no acervo. */
    private ArrayList<Integer> livrosFavoritos;

    // Construtor padrão.
    public Usuario() {
        livrosFavoritos = new ArrayList<>();
    }

    /* Cria um usuário com seus dados principais. A lista de favoritos é iniciada vazia. */
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

    /* Cria um usuário com todos os seus dados definidos.
    Esse construtor permite reconstruir corretamente registros armazenados. */
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

    public void setLivrosFavoritos(ArrayList<Integer> livrosFavoritos) {
        this.livrosFavoritos = livrosFavoritos;
    }

    /* Adiciona pontos à pontuação atual do usuário.*/
    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }

    /* Adiciona um livro aos favoritos caso ele ainda não esteja registrado. */
    public void adicionarFavorito(int idLivro) {
        if (!livrosFavoritos.contains(idLivro)) {
            livrosFavoritos.add(idLivro);
        }
    }

    /* Remove um livro da lista de favoritos. */
    public void removerFavorito(int idLivro) {
        livrosFavoritos.remove(Integer.valueOf(idLivro));
    }

    /* Verifica se um determinado livro está na lista de favoritos. */
    public boolean possuiFavorito(int idLivro) {
        return livrosFavoritos.contains(idLivro);
    }

    /* Converte os dados do objeto para uma linha separada por ponto e vírgula.
    Os IDs dos livros favoritos são armazenados separados por vírgulas. */
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
                + " | Favoritos: " + livrosFavoritos.size();
    }
}