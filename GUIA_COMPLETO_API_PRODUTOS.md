Guia Completo: API de Produtos com Quarkus
1. Objetivo
   Criar uma API RESTful para gerenciar produtos, contendo:

PostgreSQL
Cadastro e login
JWT
Roles ADMIN e USER
CRUD de produtos
Cache
Kafka
README
Testes manuais
Publicação no GitHub
API: sistema que recebe requisições e devolve respostas.
CRUD: criar, consultar, atualizar e remover dados.
Endpoint: endereço de uma funcionalidade, como /produtos.

2. Instalar Ferramentas
   Instale:

JDK 17 ou superior
IntelliJ IDEA
Docker Desktop
Postman
Git
Links:

Docker Desktop
Postman
Git para Windows
No Windows, se o Docker pedir atualização do WSL, abra PowerShell como administrador:

wsl.exe --update
3. Criar Projeto No IntelliJ
   No IntelliJ:

New Project > Quarkus
Configure:

Name: api-produtos
Group: br.com.loja
Artifact: api-produtos
Build Tool: Maven
JDK: 17
Adicione extensões:

REST Jackson
Hibernate ORM with Panache
JDBC Driver - PostgreSQL
SmallRye JWT
SmallRye JWT Build
Cache
Messaging - Kafka Connector
Hibernate Validator
Após criar o projeto, abra o terminal e adicione BCrypt:

.\mvnw.cmd quarkus:add-extension -Dextensions="elytron-security-common"
4. Criar Pacotes
   Dentro de:

src/main/java/br/com/loja
Crie:

dto
entity
resource
service
Entity: representa tabela do banco.
DTO: transporta dados entre cliente e API.
Resource: contém endpoints.
Service: contém regras de negócio.

5. Criar Arquivos Java
   Crie os arquivos seguindo a estrutura:

entity
Produto.java
Usuario.java

dto
ErroResponse.java
LoginRequest.java
LoginResponse.java
ProdutoRequest.java
RegisterRequest.java
UsuarioResponse.java

resource
AuthResource.java
ProdutoResource.java
SecurityResponseFilter.java

service
AuthService.java
StartupService.java
EstoqueBaixoProducer.java
EstoqueBaixoConsumer.java
O código completo de cada arquivo está disponível em:

Entidades
DTOs
Resources
Services
Cada colega deve criar os arquivos manualmente e entender o papel de cada classe.

6. Configurar Aplicação
   Abra:

src/main/resources/application.properties
Cole:

quarkus.datasource.db-kind=postgresql

quarkus.hibernate-orm.schema-management.strategy=drop-and-create
quarkus.hibernate-orm.log.sql=true

%dev.quarkus.datasource.devservices.enabled=true
%dev.quarkus.kafka.devservices.enabled=true

mp.jwt.verify.issuer=api-produtos

mp.messaging.outgoing.estoque-baixo-out.connector=smallrye-kafka
mp.messaging.outgoing.estoque-baixo-out.topic=estoque-baixo
mp.messaging.outgoing.estoque-baixo-out.value.serializer=org.apache.kafka.common.serialization.StringSerializer

mp.messaging.incoming.estoque-baixo-in.connector=smallrye-kafka
mp.messaging.incoming.estoque-baixo-in.topic=estoque-baixo
mp.messaging.incoming.estoque-baixo-in.value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
Dev Services: recurso do Quarkus que sobe PostgreSQL e Kafka automaticamente em containers Docker.

7. Executar
   Abra Docker Desktop.

No terminal:

.\mvnw.cmd quarkus:dev
Aguarde:

Listening on: http://localhost:8080
Dev UI:

http://localhost:8080/q/dev-ui
É normal o Docker não mostrar containers antes de executar a API.

8. Usuários Padrão
   Ao iniciar, StartupService cria:

admin@loja.com / admin123 / ADMIN
user@loja.com  / user123  / USER
As senhas são armazenadas com BCrypt.

Hash: transformação de segurança que impede salvar senha pura no banco.

9. Testar No Postman
   Login ADMIN
   POST http://localhost:8080/auth/login
   {
   "email": "admin@loja.com",
   "senha": "admin123"
   }
   Copie o token retornado.

Criar Produto
POST http://localhost:8080/produtos
Authorization > Bearer Token > cole o token ADMIN
{
"nome": "Caderno Verde",
"descricao": "Caderno universitario",
"preco": 12.90,
"estoque": 2
}
Esperado:

201 Created
No terminal:

[ALERTA DE ESTOQUE] Produto 'Caderno Verde' com estoque baixo (2 unidades). E-mail de alerta enviado para estoque@empresa.com
Testar CRUD
GET    http://localhost:8080/produtos
GET    http://localhost:8080/produtos/1
PUT    http://localhost:8080/produtos/1
DELETE http://localhost:8080/produtos/1
Use o ID realmente retornado no cadastro.

Testar Segurança
Sem token:

GET http://localhost:8080/produtos
Esperado:

401 Unauthorized
{"erro":"Não autenticado"}
Com token USER, tente criar produto. Esperado:

403 Forbidden
{"erro":"Acesso negado"}
10. Entregáveis
    Crie na raiz:

README.md
api-produtos.http
Use como referência:

README
Arquivo HTTP
11. Publicar No GitHub
    Crie conta em:

GitHub

Configure Git:

git config --global user.name "SEU NOME"
git config --global user.email "SEU EMAIL"
Na raiz do projeto:

git init -b main
git status --short
git add .
git commit -m "Implementa API de gerenciamento de produtos"
No GitHub:

New repository
Repository name: api-produtos
Visibility: Public
Não marque:

Add README
Add .gitignore
Choose a license
Copie a URL do repositório e execute:

git remote add origin https://github.com/SEU-USUARIO/api-produtos.git
git remote -v
git push -u origin main
Nunca publique tokens, senhas pessoais ou chaves privadas.

12. Tabela De Pontuação
    Critério	Evidência	Pontos
    CRUD com persistência	POST, GET, PUT e DELETE	2,0
    Login e cadastro	JWT, BCrypt e e-mail único	1,5
    JWT e roles	ADMIN, USER, 401 e 403	2,0
    Cache	@CacheResult e @CacheInvalidateAll	2,0
    Kafka	Producer, consumer e log	1,5
    Organização	Pacotes, DTOs, README e .http	1,0
    Total		10,0
13. Erros Comuns
    Docker isn't working
    Abra Docker Desktop e atualize WSL.

Port 8080 already in use
Pare a instância anterior com Ctrl + C.

401 Unauthorized
Faça login novamente e use token completo.

403 Forbidden
Use token ADMIN para alterar produtos.

Cannot find symbol MyMessagingApplication
Apague testes automáticos antigos do template em src/test.

Fontes Oficiais
Quarkus com Maven
Quarkus Dev Services
PostgreSQL com Dev Services
Hibernate ORM e Panache
JWT no Quarkus
Publicar projeto local no GitHub