# Sistema-java-spON
Sistema básico em java que conecta-se com o projeto principal da organização info-guard
https://github.com/InfoGuard-Solution

## Funções

 - Cadastra chamado no banco de dados, caso o usuário forneça o apelido de algum computador
 - "Em caso de qualquer problema com a máquina que estiver operando, poderá ser feito um chamado
 - Pega informações da API Looca e insere no banco de dados do sistema, caso o usuário fornceça o apelido de algum computador
 
OBS : Para acessar as funções mencionadas, é indispensável possuir um perfil registrado em alguma organização no sistema.


# Observação
- O projeto está enviando dados para o banco de dados EC2 do projeto SuperVisiON. Caso você queira trocar o banco de dados, Ir para a classe conexão.
  Ao optar pelo uso do MySQL, acesse o pacote "Registros" e proceda descomentando ou apagando as linhas que contenham 'conn = Conexao.createConnectionToMySQL();', ao mesmo tempo em que comenta as linhas relacionadas ao SQL Server


## Adicional
Para executar o arquivo JAR, é essencial acessá-lo por meio do prompt de comando, onde se torna possível gerenciar este projeto. <br><br>
- 1. Vá até a pasta onde o arquivo JAR está armazenado e digite `java -jar individual_sp2.jar`.
- 2. Após inserir esse comando, pressione "Enter" para executar o arquivo JAR e iniciar o programa.
