package exception;

// Representa erros específicos que podem ocorrer durante as operações realizadas no sistema da biblioteca.
public class BibliotecaException extends Exception {

    // Cria uma exceção com uma mensagem que descreve o problema ocorrido durante a operação.
    public BibliotecaException(String mensagem) {
        super(mensagem);
    }
}