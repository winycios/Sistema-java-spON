package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexaoServer extends Conexao {

    private static final String url = "jdbc:sqlserver://34.197.21.13;databaseName=SuperVisiON;trustServerCertificate=true;";
    private static final String usuario = "sa";
    private static final String senha = "MAdalena13";

    /* conexao com o banco de dados SQL SERVER*/

    @Override
    public Connection criarConexao() throws ClassNotFoundException, SQLException {

        Connection conexao = DriverManager.getConnection(url, usuario, senha);

        return conexao;
    }

    public static void main(String[] args) throws Exception {
        Conexao conexao = new ConexaoServer();

        //Recuperar uma conexão com o banco de dados
        Connection con = conexao.criarConexao();

        //Testar se a conexão é nula
        if (con != null) {
            System.out.println("Conexão obtida com sucesso!");
            con.close();
        }
    }
}
