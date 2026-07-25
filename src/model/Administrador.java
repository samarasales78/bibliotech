package model;

import java.util.ArrayList;

// Representa um usuário com permissões para gerenciar o acervo.
public class Administrador extends Usuario {

    // Construtor padrão.
    public Administrador() {
        super();
    }

    /* Cria um administrador com seus dados principais.
    A lista de favoritos é iniciada vazia. */
    public Administrador(
            int id,
            String nome,
            String cpf,
            String generoFavorito
    ) {
        super(
                id,
                nome,
                cpf,
                generoFavorito
        );
    }

    /* Reconstrói um administrador com os dados armazenados.
    Os IDs dos favoritos permitem recuperar os livros correspondentes. */
    public Administrador(
            int id,
            String nome,
            String cpf,
            String generoFavorito,
            int pontos,
            ArrayList<Integer> livrosFavoritos
    ) {
        super(
                id,
                nome,
                cpf,
                generoFavorito,
                pontos,
                livrosFavoritos
        );
    }

    // Retorna o tipo de acesso específico do administrador.
    @Override
    public String getTipo() {
        return "Administrador";
    }
}