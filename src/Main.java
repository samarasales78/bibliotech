import exception.BibliotecaException;
import model.Emprestimo;
import model.Leitor;
import model.Livro;
import model.Usuario;
import service.Biblioteca;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Classe principal responsável pela interação com o usuário.
public class Main {

        private static final Scanner scanner = new Scanner(System.in);
        private static final Biblioteca biblioteca = new Biblioteca();

        public static void main(String[] args) {

                try {
                        biblioteca.carregarDados();
                } catch (IOException e) {
                        System.out.println("Erro ao carregar os dados.");
                }

                int opcao;

                do {
                        titulo("BIBLIOTECH");
                        System.out.println("1 - Entrar");
                        System.out.println("2 - Criar conta");
                        System.out.println("0 - Sair");

                        opcao = lerInteiro("\nEscolha: ");

                        try {
                                switch (opcao) {
                                        case 1 -> entrar();
                                        case 2 -> criarConta();
                                        case 0 -> System.out.println(
                                                        "\nEncerrando o sistema...");
                                        default -> System.out.println(
                                                        "\nOpção inválida.");
                                }
                        } catch (BibliotecaException e) {
                                System.out.println("\nErro: " + e.getMessage());
                        }

                } while (opcao != 0);

                try {
                        biblioteca.salvarDados();
                        System.out.println("Dados salvos com sucesso.");
                } catch (IOException e) {
                        System.out.println("Erro ao salvar os dados.");
                }

                scanner.close();
        }

        /*
         * Localiza o usuário pela Senha e direciona para o
         * menu correspondente ao seu tipo de acesso.
         */
        private static void entrar()
                        throws BibliotecaException {

                String senha = lerTexto("\nSENHA: ");
                Usuario usuario = biblioteca.buscarUsuarioPorSenha(senha);

                if (usuario == null) {
                        throw new BibliotecaException(
                                        "Usuário não encontrado.");
                }

                if (usuario.getTipo().equals("Administrador")) {
                        menuAdministrador();
                } else {
                        menuUsuario(usuario);
                }
        }

        /*
         * Permite que uma pessoa crie sua própria conta.
         * Novos cadastros são leitores comuns.
         */
        private static void criarConta()
                        throws BibliotecaException {

                titulo("CRIAR CONTA");

                String nome = lerTexto("Nome: ");
                String senha = lerTexto("SENHA: ");
                String genero = lerTexto(
                                "Gênero literário favorito: ");

                Leitor leitor = new Leitor(
                                gerarNovoIdUsuario(),
                                nome,
                                senha,
                                genero);

                biblioteca.cadastrarUsuario(leitor);

                System.out.println("\nConta criada com sucesso!");
                System.out.println("Seu ID é: " + leitor.getId());
        }

        // Gera um novo ID baseado no maior ID existente.
        private static int gerarNovoIdUsuario() {

                int maiorId = 0;

                for (Usuario usuario : biblioteca.getUsuarios()) {
                        if (usuario.getId() > maiorId) {
                                maiorId = usuario.getId();
                        }
                }

                return maiorId + 1;
        }

        // Exibe as operações exclusivas do administrador.
        private static void menuAdministrador()
                        throws BibliotecaException {

                int opcao;

                do {
                        titulo("MENU ADMINISTRADOR");

                        exibirOpcoes(
                                        "Cadastrar livro",
                                        "Listar livros",
                                        "Listar usuários",
                                        "Ver empréstimos",
                                        "Ranking de livros");

                        opcao = lerInteiro("\nEscolha: ");

                        switch (opcao) {
                                case 1 -> cadastrarLivro();
                                case 2 -> listarLivros();
                                case 3 -> listarUsuarios();
                                case 4 -> listarEmprestimos();
                                case 5 -> exibirRanking();
                                case 0 -> System.out.println(
                                                "\nSaindo da conta...");
                                default -> System.out.println(
                                                "\nOpção inválida.");
                        }

                } while (opcao != 0);
        }

        // Exibe as operações disponíveis para o leitor.
        private static void menuUsuario(Usuario usuario)
                        throws BibliotecaException {

                int opcao;

                do {
                        titulo("MENU DO USUÁRIO");
                        System.out.println("Olá, " + usuario.getNome() + "!\n");

                        exibirOpcoes(
                                        "Listar livros disponíveis",
                                        "Realizar empréstimo",
                                        "Devolver livro",
                                        "Adicionar favorito",
                                        "Ver recomendações",
                                        "Ver minha pontuação");

                        opcao = lerInteiro("\nEscolha: ");

                        switch (opcao) {
                                case 1 -> listarLivrosDisponiveis();
                                case 2 -> realizarEmprestimo(usuario);
                                case 3 -> devolverLivro();
                                case 4 -> adicionarFavorito(usuario);
                                case 5 -> exibirRecomendacoes(usuario);
                                case 6 -> exibirPontuacao(usuario);
                                case 0 -> System.out.println(
                                                "\nSaindo da conta...");
                                default -> System.out.println(
                                                "\nOpção inválida.");
                        }

                } while (opcao != 0);
        }

        // Exibe as opções de um menu numerado.
        private static void exibirOpcoes(String... opcoes) {

                for (int i = 0; i < opcoes.length; i++) {
                        System.out.println(
                                        (i + 1) + " - " + opcoes[i]);
                }

                System.out.println("0 - Sair");
        }

        // Cadastra um livro no acervo.
        private static void cadastrarLivro()
                        throws BibliotecaException {

                titulo("CADASTRAR LIVRO");

                String titulo = lerTexto("Título: ");
                String autor = lerTexto("Autor: ");
                String genero = escolherGenero();

                Livro livro = biblioteca.cadastrarLivro(
                                titulo,
                                autor,
                                genero);

                System.out.println(
                                "\nLivro cadastrado com sucesso.");
                System.out.println(
                                "ID gerado automaticamente: "
                                                + livro.getId());
        }

        // Permite escolher um gênero literário padronizado.
        private static String escolherGenero() {

                String[] generos = {
                                "Romance",
                                "Fantasia",
                                "Ficção Científica",
                                "Terror",
                                "Suspense",
                                "Mistério",
                                "Drama",
                                "Aventura",
                                "Biografia",
                                "História",
                                "Filosofia",
                                "Tecnologia",
                                "Programação",
                                "Educação",
                                "Autoajuda"
                };

                System.out.println("\nGêneros disponíveis:");

                for (int i = 0; i < generos.length; i++) {
                        System.out.println((i + 1) + " - " + generos[i]);
                }

                while (true) {
                        int opcao = lerInteiro("\nEscolha o gênero: ");

                        if (opcao >= 1 && opcao <= generos.length) {
                                return generos[opcao - 1];
                        }

                        System.out.println("Opção inválida. Tente novamente.");
                }
        }

        // Exibe todos os livros cadastrados.
        private static void listarLivros() {

                titulo("LIVROS");

                ArrayList<Livro> livros = biblioteca.getLivros();

                if (livros.isEmpty()) {
                        System.out.println("Nenhum livro cadastrado.");
                        return;
                }

                for (Livro livro : livros) {
                        exibirLivro(livro);
                }
        }

        // Exibe somente os livros disponíveis.
        private static void listarLivrosDisponiveis() {

                titulo("LIVROS DISPONÍVEIS");

                boolean encontrou = false;

                for (Livro livro : biblioteca.getLivros()) {
                        if (livro.isDisponivel()) {
                                exibirLivro(livro);
                                encontrou = true;
                        }
                }

                if (!encontrou) {
                        System.out.println(
                                        "Nenhum livro disponível no momento.");
                }
        }

        // Exibe os dados principais de um livro.
        private static void exibirLivro(Livro livro) {

                System.out.println(
                                "\n┌──────────────────────────────────────┐");
                System.out.println("│ ID: " + livro.getId());
                System.out.println("│ Título: " + livro.getTitulo());
                System.out.println("│ Autor: " + livro.getAutor());
                System.out.println("│ Gênero: " + livro.getGenero());
                System.out.println(
                                "│ Status: "
                                                + (livro.isDisponivel()
                                                                ? "Disponível"
                                                                : "Emprestado"));
                System.out.println(
                                "│ Empréstimos: "
                                                + livro.getQuantidadeEmprestimos());
                System.out.println(
                                "└──────────────────────────────────────┘");
        }

        // Exibe todos os usuários cadastrados.
        private static void listarUsuarios() {

                titulo("USUÁRIOS");

                ArrayList<Usuario> usuarios = biblioteca.getUsuarios();

                if (usuarios.isEmpty()) {
                        System.out.println("Nenhum usuário cadastrado.");
                        return;
                }

                for (Usuario usuario : usuarios) {

                        System.out.println(
                                        "\n┌──────────────────────────────────────┐");
                        System.out.println("│ ID: " + usuario.getId());
                        System.out.println("│ Nome: " + usuario.getNome());
                        System.out.println("│ SENHA: " + usuario.getSenha());
                        System.out.println(
                                        "│ Gênero favorito: "
                                                        + usuario.getGeneroFavorito());
                        System.out.println("│ Pontos: " + usuario.getPontos());
                        System.out.println(
                                        "│ Tipo: " + usuario.getTipo());
                        System.out.println(
                                        "└──────────────────────────────────────┘");
                }
        }

        // Exibe todos os empréstimos registrados.
        private static void listarEmprestimos() {

                titulo("EMPRÉSTIMOS");

                ArrayList<Emprestimo> emprestimos = biblioteca.getEmprestimos();

                if (emprestimos.isEmpty()) {
                        System.out.println(
                                        "Nenhum empréstimo registrado.");
                        return;
                }

                for (Emprestimo emprestimo : emprestimos) {

                        System.out.println(
                                        "\n┌──────────────────────────────────────┐");
                        System.out.println(
                                        "│ Livro ID: "
                                                        + emprestimo.getIdLivro());
                        System.out.println(
                                        "│ Usuário ID: "
                                                        + emprestimo.getIdUsuario());
                        System.out.println(
                                        "│ Empréstimo: "
                                                        + emprestimo.getDataEmprestimo());
                        System.out.println(
                                        "│ Devolução: "
                                                        + emprestimo.getDataDevolucao());
                        System.out.println(
                                        "│ Status: "
                                                        + (emprestimo.isDevolvido()
                                                                        ? "Devolvido"
                                                                        : "Ativo"));
                        System.out.println(
                                        "└──────────────────────────────────────┘");
                }
        }

        // Exibe os livros mais emprestados.
        private static void exibirRanking() {

                titulo("RANKING DE LIVROS");

                int posicao = 1;

                for (Livro livro : biblioteca.gerarRanking()) {

                        System.out.println(
                                        posicao + "º lugar — "
                                                        + livro.getTitulo());
                        System.out.println(
                                        "Autor: " + livro.getAutor());
                        System.out.println(
                                        "Empréstimos: "
                                                        + livro.getQuantidadeEmprestimos());
                        System.out.println();

                        posicao++;
                }
        }

        // Exibe a pontuação atual do usuário.
        private static void exibirPontuacao(
                        Usuario usuario) {

                titulo("MINHA PONTUAÇÃO");

                System.out.println(
                                "Usuário: " + usuario.getNome());
                System.out.println(
                                "Pontos acumulados: "
                                                + usuario.getPontos());
        }

        // Realiza um empréstimo para o usuário conectado.
        private static void realizarEmprestimo(
                        Usuario usuario) throws BibliotecaException {

                int idLivro = lerInteiro("\nID do livro: ");

                biblioteca.realizarEmprestimo(
                                idLivro,
                                usuario.getId());

                System.out.println(
                                "\nEmpréstimo realizado com sucesso.");
        }

        // Registra a devolução de um livro.
        private static void devolverLivro()
                        throws BibliotecaException {

                int idLivro = lerInteiro("\nID do livro: ");

                biblioteca.devolverLivro(idLivro);

                System.out.println(
                                "\nLivro devolvido com sucesso.");
        }

        // Adiciona um livro aos favoritos do usuário.
        private static void adicionarFavorito(
                        Usuario usuario) throws BibliotecaException {

                int idLivro = lerInteiro("\nID do livro: ");

                biblioteca.adicionarFavorito(
                                usuario.getId(),
                                idLivro);

                System.out.println(
                                "\nLivro adicionado aos favoritos.");
        }

        // Exibe recomendações baseadas no gênero favorito.
        private static void exibirRecomendacoes(
                        Usuario usuario) throws BibliotecaException {

                titulo("RECOMENDAÇÕES PARA VOCÊ");

                System.out.println(
                                "Gênero favorito: "
                                                + usuario.getGeneroFavorito());

                ArrayList<Livro> recomendacoes = biblioteca.recomendarLivros(
                                usuario.getId());

                if (recomendacoes.isEmpty()) {
                        System.out.println(
                                        "\nNenhuma recomendação encontrada.");
                        return;
                }

                int numero = 1;

                for (Livro livro : recomendacoes) {

                        System.out.println(
                                        "\n" + numero + ". "
                                                        + livro.getTitulo());
                        System.out.println(
                                        "   Autor: " + livro.getAutor());
                        System.out.println(
                                        "   Gênero: " + livro.getGenero());
                        System.out.println(
                                        "   Status: "
                                                        + (livro.isDisponivel()
                                                                        ? "Disponível"
                                                                        : "Emprestado"));

                        numero++;
                }
        }

        // Exibe um título dentro de uma borda padronizada.
        private static void titulo(String texto) {

                System.out.println(
                                "\n┌──────────────────────────────────────┐");
                System.out.printf(
                                "│ %-36s │%n",
                                texto);
                System.out.println(
                                "└──────────────────────────────────────┘");
        }

        // Lê um número inteiro e trata entradas inválidas.
        private static int lerInteiro(String mensagem) {

                while (true) {

                        try {
                                System.out.print(mensagem);
                                return Integer.parseInt(
                                                scanner.nextLine());

                        } catch (NumberFormatException e) {
                                System.out.println(
                                                "Digite um número inteiro válido.");
                        }
                }
        }

        // Lê um texto e impede campos obrigatórios vazios.
        private static String lerTexto(String mensagem) {

                while (true) {

                        System.out.print(mensagem);

                        String texto = scanner.nextLine().trim();

                        if (!texto.isEmpty()) {
                                return texto;
                        }

                        System.out.println(
                                        "O campo não pode ficar vazio.");
                }
        }
}