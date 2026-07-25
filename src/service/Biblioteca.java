package service;

import exception.BibliotecaException;
import model.Administrador;
import model.Emprestimo;
import model.Leitor;
import model.Livro;
import model.Usuario;
import persistence.Persistencia;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

// Gerencia os livros, usuários e empréstimos da biblioteca.
public class Biblioteca implements Persistencia {

    private ArrayList<Livro> livros = new ArrayList<>();
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Emprestimo> emprestimos = new ArrayList<>();

    // Cadastra um livro e impede IDs duplicados.
    public void cadastrarLivro(Livro livro)
            throws BibliotecaException {

        if (buscarLivro(livro.getId()) != null) {
            throw new BibliotecaException(
                    "Livro já cadastrado."
            );
        }

        livros.add(livro);
    }

    // Cadastra um usuário e impede IDs ou CPFs duplicados.
    public void cadastrarUsuario(Usuario usuario)
            throws BibliotecaException {

        if (buscarUsuario(usuario.getId()) != null
                || buscarUsuarioPorCpf(usuario.getCpf()) != null) {

            throw new BibliotecaException(
                    "Usuário já cadastrado."
            );
        }

        usuarios.add(usuario);
    }

    // Busca um livro pelo seu ID.
    public Livro buscarLivro(int id) {

        for (Livro livro : livros) {
            if (livro.getId() == id) {
                return livro;
            }
        }

        return null;
    }

    // Busca um usuário pelo seu ID.
    public Usuario buscarUsuario(int id) {

        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }

        return null;
    }

    // Busca um usuário pelo seu CPF.
    public Usuario buscarUsuarioPorCpf(String cpf) {

        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                return usuario;
            }
        }

        return null;
    }

    /*
     * Cria um empréstimo, atualiza a disponibilidade do livro
     * e adiciona pontos ao usuário.
     */
    public void realizarEmprestimo(
            int idLivro,
            int idUsuario
    ) throws BibliotecaException {

        Livro livro = buscarLivro(idLivro);
        Usuario usuario = buscarUsuario(idUsuario);

        if (livro == null) {
            throw new BibliotecaException(
                    "Livro não encontrado."
            );
        }

        if (usuario == null) {
            throw new BibliotecaException(
                    "Usuário não encontrado."
            );
        }

        if (!livro.isDisponivel()) {
            throw new BibliotecaException(
                    "Livro indisponível."
            );
        }

        LocalDate hoje = LocalDate.now();

        emprestimos.add(new Emprestimo(
                idLivro,
                idUsuario,
                hoje,
                hoje.plusDays(7)
        ));

        livro.registrarEmprestimo();
        usuario.adicionarPontos(10);
    }

    /*
     * Finaliza o empréstimo ativo, disponibiliza o livro
     * e adiciona pontos ao usuário pela devolução.
     */
    public void devolverLivro(int idLivro)
            throws BibliotecaException {

        Livro livro = buscarLivro(idLivro);

        if (livro == null) {
            throw new BibliotecaException(
                    "Livro não encontrado."
            );
        }

        for (Emprestimo emprestimo : emprestimos) {

            if (emprestimo.getIdLivro() == idLivro
                    && emprestimo.estaAtivo()) {

                emprestimo.registrarDevolucao();
                livro.disponibilizar();

                Usuario usuario = buscarUsuario(
                        emprestimo.getIdUsuario()
                );

                if (usuario != null) {
                    usuario.adicionarPontos(20);
                }

                return;
            }
        }

        throw new BibliotecaException(
                "Não existe empréstimo ativo para esse livro."
        );
    }

    // Adiciona um livro aos favoritos do usuário.
    public void adicionarFavorito(
            int idUsuario,
            int idLivro
    ) throws BibliotecaException {

        Usuario usuario = buscarUsuario(idUsuario);
        Livro livro = buscarLivro(idLivro);

        if (usuario == null || livro == null) {
            throw new BibliotecaException(
                    "Usuário ou livro não encontrado."
            );
        }

        if (!usuario.possuiFavorito(idLivro)) {
            usuario.adicionarFavorito(idLivro);
            usuario.adicionarPontos(5);
        }
    }

    // Recomenda livros com base no gênero favorito do usuário.
    public ArrayList<Livro> recomendarLivros(
            int idUsuario
    ) throws BibliotecaException {

        Usuario usuario = buscarUsuario(idUsuario);

        if (usuario == null) {
            throw new BibliotecaException(
                    "Usuário não encontrado."
            );
        }

        ArrayList<Livro> recomendacoes = new ArrayList<>();

        for (Livro livro : livros) {

            if (livro.getGenero().equalsIgnoreCase(
                    usuario.getGeneroFavorito()
            )) {
                recomendacoes.add(livro);
            }
        }

        return recomendacoes;
    }

    // Retorna os livros ordenados pela quantidade de empréstimos.
    public ArrayList<Livro> gerarRanking() {

        ArrayList<Livro> ranking =
                new ArrayList<>(livros);

        ranking.sort(
                Comparator.comparingInt(
                        Livro::getQuantidadeEmprestimos
                ).reversed()
        );

        return ranking;
    }

    // Retorna uma cópia dos livros cadastrados.
    public ArrayList<Livro> getLivros() {
        return new ArrayList<>(livros);
    }

    // Retorna uma cópia dos usuários cadastrados.
    public ArrayList<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    // Retorna uma cópia dos empréstimos registrados.
    public ArrayList<Emprestimo> getEmprestimos() {
        return new ArrayList<>(emprestimos);
    }

    /*
     * Salva livros, usuários e empréstimos em arquivos CSV.
     */
    @Override
    public void salvarDados() throws IOException {

        salvarLivros();
        salvarUsuarios();
        salvarEmprestimos();
    }

    // Salva os livros no arquivo CSV.
    private void salvarLivros() throws IOException {

        try (PrintWriter arquivo = new PrintWriter(
                "dados/livros.csv"
        )) {

            for (Livro livro : livros) {
                arquivo.println(livro.toCSV());
            }
        }
    }

    // Salva os usuários, incluindo seu tipo de acesso.
    private void salvarUsuarios() throws IOException {

        try (PrintWriter arquivo = new PrintWriter(
                "dados/usuarios.csv"
        )) {

            for (Usuario usuario : usuarios) {
                arquivo.println(
                        usuario.toCSV()
                                + ";"
                                + usuario.getTipo()
                );
            }
        }
    }

    // Salva os empréstimos no arquivo CSV.
    private void salvarEmprestimos() throws IOException {

        try (PrintWriter arquivo = new PrintWriter(
                "dados/emprestimos.csv"
        )) {

            for (Emprestimo emprestimo : emprestimos) {
                arquivo.println(emprestimo.toCSV());
            }
        }
    }

    /*
     * Carrega os dados armazenados nos arquivos CSV.
     */
    @Override
    public void carregarDados() throws IOException {

        carregarLivros();
        carregarUsuarios();
        carregarEmprestimos();
    }

    // Lê e reconstrói os livros armazenados.
    private void carregarLivros() throws IOException {

        File arquivo = new File(
                "dados/livros.csv"
        );

        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader leitor =
                     new BufferedReader(
                             new FileReader(arquivo)
                     )) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                if (linha.isBlank()) {
                    continue;
                }

                String[] dados = linha.split(";");

                livros.add(new Livro(
                        Integer.parseInt(dados[0]),
                        dados[1],
                        dados[2],
                        dados[3],
                        Boolean.parseBoolean(dados[4]),
                        Integer.parseInt(dados[5])
                ));
            }
        }
    }

    /*
     * Lê e reconstrói os usuários armazenados.
     * O último campo define a subclasse do usuário.
     */
    private void carregarUsuarios() throws IOException {

        File arquivo = new File(
                "dados/usuarios.csv"
        );

        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader leitor =
                     new BufferedReader(
                             new FileReader(arquivo)
                     )) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                if (linha.isBlank()) {
                    continue;
                }

                String[] dados = linha.split(";");
                ArrayList<Integer> favoritos =
                        new ArrayList<>();

                if (dados.length > 5
                        && !dados[5].isBlank()) {

                    for (String id : dados[5].split(",")) {
                        favoritos.add(
                                Integer.parseInt(id)
                        );
                    }
                }

                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                String cpf = dados[2];
                String genero = dados[3];
                int pontos = Integer.parseInt(dados[4]);

                String tipo = dados.length > 6
                        ? dados[6]
                        : "Leitor";

                Usuario usuario;

                if (tipo.equalsIgnoreCase(
                        "Administrador"
                )) {
                    usuario = new Administrador(
                            id,
                            nome,
                            cpf,
                            genero,
                            pontos,
                            favoritos
                    );
                } else {
                    usuario = new Leitor(
                            id,
                            nome,
                            cpf,
                            genero,
                            pontos,
                            favoritos
                    );
                }

                usuarios.add(usuario);
            }
        }
    }

    // Lê e reconstrói os empréstimos armazenados.
    private void carregarEmprestimos()
            throws IOException {

        File arquivo = new File(
                "dados/emprestimos.csv"
        );

        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader leitor =
                     new BufferedReader(
                             new FileReader(arquivo)
                     )) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                if (linha.isBlank()) {
                    continue;
                }

                String[] dados = linha.split(";");

                emprestimos.add(new Emprestimo(
                        Integer.parseInt(dados[0]),
                        Integer.parseInt(dados[1]),
                        LocalDate.parse(dados[2]),
                        LocalDate.parse(dados[3]),
                        Boolean.parseBoolean(dados[4])
                ));
            }
        }
    }
}