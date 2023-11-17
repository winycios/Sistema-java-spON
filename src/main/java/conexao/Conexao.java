package conexao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Conexao {

    public abstract Connection criarConexao() throws ClassNotFoundException, SQLException;

}
