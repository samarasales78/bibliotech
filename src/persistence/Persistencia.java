package persistence;

import java.io.IOException;

    // Define operações básicas de persistência dos dados do sistema.
public interface Persistencia {
    void carregarDados() throws IOException;

    void salvarDados() throws IOException;
    }
