package service;

import exception.BibliotecaException;
import model.Administrador;
import model.Emprestimo;
import model.Leitor;
import model.Livro;
import model.Usuario;
import persistence.Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

// Gerencia os livros, usuários e empréstimos da biblioteca.
public class Biblioteca implements Persistencia {

        private ArrayList<Livro> livros = new ArrayList<>();
        private ArrayList<Usuario> usuarios = new ArrayList<>();
        private ArrayList<Emprestimo> emprestimos = new ArrayList<>();

        // Cadastra um livro gerando automaticamente um ID único.
        public Livro cadastrarLivro(
                        String titulo,
                        String autor,
                        String genero) throws BibliotecaException {

                int novoId = gerarNovoIdLivro();

                Livro livro = new Livro(
                                novoId,
                                titulo,
                                autor,
                                genero);

                livros.add(livro);

                return livro;
        }

        // Gera automaticamente o próximo ID disponível para um livro.
        private int gerarNovoIdLivro() {

                int maiorId = 0;

                for (Livro livro : livros) {
                        if (livro.getId() > maiorId) {
                                maiorId = livro.getId();
                        }
                }

                return maiorId + 1;
        }

        // Cadastra um usuário e impede IDs ou Senhas duplicados.
        public void cadastrarUsuario(Usuario usuario)
                        throws BibliotecaException {

                if (buscarUsuario(usuario.getId()) != null
                                || buscarUsuarioPorSenha(usuario.getSenha()) != null) {

                        throw new BibliotecaException(
                                        "Usuário já cadastrado.");
                }

                usuarios.add(usuario);
        }

        // Busca um livro pelo ID.
        public Livro buscarLivro(int id) {

                for (Livro livro : livros) {
                        if (livro.getId() == id) {
                                return livro;
                        }
                }

                return null;
        }

        // Busca um usuário pelo ID.
        public Usuario buscarUsuario(int id) {

                for (Usuario usuario : usuarios) {
                        if (usuario.getId() == id) {
                                return usuario;
                        }
                }

                return null;
        }

        // Busca um usuário pela Senha.
        public Usuario buscarUsuarioPorSenha(String senha) {

                for (Usuario usuario : usuarios) {
                        if (usuario.getSenha().equals(senha)) {
                                return usuario;
                        }
                }

                return null;
        }

        // Realiza um empréstimo.
        public void realizarEmprestimo(
                        int idLivro,
                        int idUsuario) throws BibliotecaException {

                Livro livro = buscarLivro(idLivro);
                Usuario usuario = buscarUsuario(idUsuario);

                if (livro == null) { // condições
                        throw new BibliotecaException(
                                        "Livro não encontrado.");
                }

                if (usuario == null) {
                        throw new BibliotecaException(
                                        "Usuário não encontrado.");
                }

                if (!livro.isDisponivel()) {
                        throw new BibliotecaException(
                                        "Livro indisponível.");
                }

                LocalDate hoje = LocalDate.now(); // registra data do empréstimo

                emprestimos.add(
                                new Emprestimo( // cria um objeto empréstimo
                                                idLivro,
                                                idUsuario,
                                                hoje,
                                                hoje.plusDays(7))); // devolução definida para 7 dias depois

                livro.registrarEmprestimo();
                usuario.adicionarPontos(10);
        }

        // Registra a devolução de um livro.
        public void devolverLivro(int idLivro)
                        throws BibliotecaException {

                Livro livro = buscarLivro(idLivro);

                if (livro == null) {
                        throw new BibliotecaException(
                                        "Livro não encontrado.");
                }

                for (Emprestimo emprestimo : emprestimos) {

                        if (emprestimo.getIdLivro() == idLivro
                                        && emprestimo.estaAtivo()) {

                                emprestimo.registrarDevolucao(); // torna disponível novamente
                                livro.disponibilizar();

                                Usuario usuario = buscarUsuario(
                                                emprestimo.getIdUsuario());

                                if (usuario != null) {
                                        usuario.adicionarPontos(20); // 20 pontos pela devolução
                                }

                                return;
                        }
                }

                throw new BibliotecaException(
                                "Não existe empréstimo ativo para esse livro.");
        }

        // Adiciona um livro aos favoritos.
        public void adicionarFavorito(
                        int idUsuario,
                        int idLivro) throws BibliotecaException {

                Usuario usuario = buscarUsuario(idUsuario);
                Livro livro = buscarLivro(idLivro);

                if (usuario == null || livro == null) {
                        throw new BibliotecaException(
                                        "Usuário ou livro não encontrado.");
                }

                if (!usuario.possuiFavorito(idLivro)) {
                        usuario.adicionarFavorito(idLivro);
                        usuario.adicionarPontos(5);
                }
        }

        // Recomenda livros com base no gênero favorito.
        public ArrayList<Livro> recomendarLivros(
                        int idUsuario) throws BibliotecaException {

                Usuario usuario = buscarUsuario(idUsuario);

                if (usuario == null) {
                        throw new BibliotecaException(
                                        "Usuário não encontrado.");
                }

                ArrayList<Livro> recomendacoes = new ArrayList<>();

                for (Livro livro : livros) {

                        if (livro.getGenero().equalsIgnoreCase(
                                        usuario.getGeneroFavorito())) {
                                recomendacoes.add(livro);
                        }
                }

                return recomendacoes;
        }

        // Retorna os livros ordenados pela quantidade de empréstimos.
        public ArrayList<Livro> gerarRanking() {

                ArrayList<Livro> ranking = new ArrayList<>(livros);

                ranking.sort(
                                Comparator.comparingInt(
                                                Livro::getQuantidadeEmprestimos).reversed());

                return ranking;
        }

        public ArrayList<Livro> getLivros() {
                return new ArrayList<>(livros);
        }

        public ArrayList<Usuario> getUsuarios() {
                return new ArrayList<>(usuarios);
        }

        public ArrayList<Emprestimo> getEmprestimos() {
                return new ArrayList<>(emprestimos);
        }

        // Salva todos os dados.
        @Override
        public void salvarDados() throws IOException {

                criarPastaDados();

                salvarLivros();
                salvarUsuarios();
                salvarEmprestimos();
        }

        // Cria a pasta de dados caso necessário.
        private void criarPastaDados() {

                File pasta = new File("dados");

                if (!pasta.exists()) {
                        pasta.mkdirs();
                }
        }

        // Salva os livros.
        private void salvarLivros()
                        throws IOException {

                try (PrintWriter arquivo = new PrintWriter(
                                new OutputStreamWriter(
                                                new FileOutputStream(
                                                                "dados/livros.csv"),
                                                StandardCharsets.UTF_8))) {

                        for (Livro livro : livros) {
                                arquivo.println(livro.toCSV());
                        }
                }
        }

        // Salva os usuários e seus tipos.
        private void salvarUsuarios()
                        throws IOException {

                try (PrintWriter arquivo = new PrintWriter(
                                new OutputStreamWriter(
                                                new FileOutputStream(
                                                                "dados/usuarios.csv"),
                                                StandardCharsets.UTF_8))) {

                        for (Usuario usuario : usuarios) {

                                arquivo.println(
                                                usuario.toCSV()
                                                                + ";"
                                                                + usuario.getTipo());
                        }
                }
        }

        // Salva os empréstimos.
        private void salvarEmprestimos()
                        throws IOException {

                try (PrintWriter arquivo = new PrintWriter(
                                new OutputStreamWriter(
                                                new FileOutputStream(
                                                                "dados/emprestimos.csv"),
                                                StandardCharsets.UTF_8))) {

                        for (Emprestimo emprestimo : emprestimos) {

                                arquivo.println(
                                                emprestimo.toCSV());
                        }
                }
        }

        // Carrega todos os dados.
        @Override
        public void carregarDados()
                        throws IOException {

                livros.clear();
                usuarios.clear();
                emprestimos.clear();

                carregarLivros();
                carregarUsuarios();
                carregarEmprestimos();
        }

        // Carrega os livros.
        private void carregarLivros()
                        throws IOException {

                File arquivo = new File("dados/livros.csv");

                if (!arquivo.exists()) {
                        return;
                }

                try (BufferedReader leitor = new BufferedReader(
                                new InputStreamReader(
                                                new FileInputStream(
                                                                arquivo),
                                                StandardCharsets.UTF_8))) {

                        String linha;

                        while ((linha = leitor.readLine()) != null) {

                                if (linha.isBlank()) {
                                        continue;
                                }

                                String[] dados = linha.split(";", -1);

                                if (dados.length < 6) {
                                        continue;
                                }

                                livros.add(
                                                new Livro(
                                                                Integer.parseInt(
                                                                                dados[0]),
                                                                dados[1],
                                                                dados[2],
                                                                dados[3],
                                                                Boolean.parseBoolean(
                                                                                dados[4]),
                                                                Integer.parseInt(
                                                                                dados[5])));
                        }
                }
        }

        // Carrega os usuários.
        private void carregarUsuarios()
                        throws IOException {

                File arquivo = new File("dados/usuarios.csv");

                if (!arquivo.exists()) {
                        return;
                }

                try (BufferedReader leitor = new BufferedReader(
                                new InputStreamReader(
                                                new FileInputStream(
                                                                arquivo),
                                                StandardCharsets.UTF_8))) {

                        String linha;

                        while ((linha = leitor.readLine()) != null) {

                                if (linha.isBlank()) {
                                        continue;
                                }

                                String[] dados = linha.split(";", -1);

                                if (dados.length < 7) {
                                        continue;
                                }

                                ArrayList<Integer> favoritos = new ArrayList<>();

                                if (!dados[5].isBlank()) {

                                        for (String id : dados[5].split(",")) {

                                                favoritos.add(
                                                                Integer.parseInt(id));
                                        }
                                }

                                int id = Integer.parseInt(dados[0]);

                                String nome = dados[1];
                                String senha = dados[2];
                                String genero = dados[3];

                                int pontos = Integer.parseInt(dados[4]);

                                String tipo = dados[6];

                                Usuario usuario;

                                if (tipo.equalsIgnoreCase(
                                                "Administrador")) {

                                        usuario = new Administrador(
                                                        id,
                                                        nome,
                                                        senha,
                                                        genero,
                                                        pontos,
                                                        favoritos);

                                } else {

                                        usuario = new Leitor(
                                                        id,
                                                        nome,
                                                        senha,
                                                        genero,
                                                        pontos,
                                                        favoritos);
                                }

                                usuarios.add(usuario);
                        }
                }
        }

        // Carrega os empréstimos.
        private void carregarEmprestimos()
                        throws IOException {

                File arquivo = new File("dados/emprestimos.csv");

                if (!arquivo.exists()) {
                        return;
                }

                try (BufferedReader leitor = new BufferedReader(
                                new InputStreamReader(
                                                new FileInputStream(
                                                                arquivo),
                                                StandardCharsets.UTF_8))) {

                        String linha;

                        while ((linha = leitor.readLine()) != null) {

                                if (linha.isBlank()) {
                                        continue;
                                }

                                String[] dados = linha.split(";", -1);

                                if (dados.length < 5) {
                                        continue;
                                }

                                emprestimos.add(
                                                new Emprestimo(
                                                                Integer.parseInt(
                                                                                dados[0]),
                                                                Integer.parseInt(
                                                                                dados[1]),
                                                                LocalDate.parse(
                                                                                dados[2]),
                                                                LocalDate.parse(
                                                                                dados[3]),
                                                                Boolean.parseBoolean(
                                                                                dados[4])));
                        }
                }
        }
}