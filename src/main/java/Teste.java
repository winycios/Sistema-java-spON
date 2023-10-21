import DAO.UserDAO;
import entities.Interface;
import entities.ItensDecoracao;
import entities.Usuario;

import java.util.Scanner;

import static java.lang.System.exit;

public class Teste {

    public static void main(String[] args) {
        Interface inteface = new Interface();
        UserDAO procurar = new UserDAO();

        Scanner ler = new Scanner(System.in);

        System.out.println("ÁREA DE LOGIN");
        ItensDecoracao.barra();
        System.out.print("Digite seu cpf : ");
        String cpf = ler.nextLine();

        ItensDecoracao.barra();
        System.out.print("Digite sua senha : ");
        String senha = ler.nextLine();
        ItensDecoracao.barra();

        Usuario user = new Usuario(cpf, senha);
        Boolean op = procurar.selectUser(user);

        if (op) {

            // mostra as opções
            inteface.Opcoes();
        } else {
            System.out.println("Usuário não encontrado, fechando sistema");
            exit(0);
        }
    }
}
