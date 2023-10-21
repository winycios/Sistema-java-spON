package entities;

import DAO.ChamadosDAO;
import DAO.LoocaDAO;
import DAO.UserDAO;

import javax.swing.*;

import java.time.LocalDateTime;
import java.util.Scanner;

import static java.lang.System.exit;

public class Interface {
    Scanner ler = new Scanner(System.in);

    public void Opcoes() {
        UserDAO user = new UserDAO();

        ItensDecoracao.barra();
        System.out.println(String.format("""
                Olá %s, escolha uma das opções abaixo:
                1 - Enviar dados do hardware
                2 - Fazer um chamado
                3 - Fechar sistema""", user.getNome()));
        ItensDecoracao.barra();
        Integer escolha = ler.nextInt();
        Escolha(escolha);
    }

    public void Escolha(Integer escolha) {

        switch (escolha) {
            case 1: {
                EnviarDadosLooca();
                break;
            }
            case 2: {
                FazerChamado();
                break;
            }
            case 3: {

                exit(0);
                break;
            }
            default: {
                System.out.println("Sistema finalizado");
                exit(0);
            }
        }
    }

    public void EnviarDadosLooca() {
        DadosLooca dados = new DadosLooca();
        LoocaDAO looca = new LoocaDAO();

        // Mostra os dados vindo da looca
        ItensDecoracao.barra();
        System.out.println("\n" + dados);

        looca.InsertDados(dados);
        //Envia para a tela de login
        Opcoes();
    }

    public void FazerChamado() {
        Scanner lerTexto = new Scanner(System.in);
        ChamadosDAO chamado = new ChamadosDAO();
        // Pega a data atual
        LocalDateTime hora = LocalDateTime.now();

        // pega o problema
        ItensDecoracao.barra();
        System.out.print("Digite o problema que está acontecendo: ");
        String problem = lerTexto.nextLine();
        ItensDecoracao.barra();

        // validação para caso o campo venha vazio
        if (problem.isBlank()) {
            System.out.println("O campo está vazio, isso não é brincadeira");
        }else{
            // criação de um objeto chamado
            Chamado dados = new Chamado(problem, String.valueOf(hora));

            //Faz a inserção do chamado no banco de dados
            chamado.InsertChamado(dados);

        }
        //Envia para a tela de login
        Opcoes();
    }
}
