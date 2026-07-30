package model;

import java.util.ArrayList;

// Representa um usuário comum que utiliza os serviços da biblioteca.
public class Leitor extends Usuario {

    // Construtor padrão.
    public Leitor() {
        super();
    }

    /*
     * Cria um leitor com seus dados principais.
     * A lista de favoritos é iniciada vazia.
     */
    public Leitor(
            int id,
            String nome,
            String senha,
            String generoFavorito) {
        super(
                id,
                nome,
                senha,
                generoFavorito);
    }

    /*
     * Reconstrói um leitor com os dados armazenados.
     * Os IDs dos favoritos permitem recuperar os livros correspondentes.
     */
    public Leitor(
            int id,
            String nome,
            String senha,
            String generoFavorito,
            int pontos,
            ArrayList<Integer> livrosFavoritos) {
        super(
                id,
                nome,
                senha,
                generoFavorito,
                pontos,
                livrosFavoritos);
    }

    // Retorna o tipo de acesso específico do leitor.
    @Override
    public String getTipo() {
        return "Leitor";
    }
}