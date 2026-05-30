# API de Gerenciamento de Produtos

Projeto final do curso de Quarkus. A aplicação disponibiliza uma API RESTful para gerenciamento de produtos, com autenticação JWT, persistência em PostgreSQL, cache e integração com Kafka.

## Tecnologias

- Java 17
- Quarkus 3.x
- Maven
- Hibernate ORM com Panache
- PostgreSQL via Quarkus Dev Services
- SmallRye JWT
- BCrypt
- Quarkus Cache
- SmallRye Reactive Messaging com Kafka
- Docker Desktop

## Funcionalidades

- Cadastro de usuários
- Login com geração de token JWT
- Controle de acesso por roles `ADMIN` e `USER`
- CRUD de produtos
- Persistência em PostgreSQL
- Cache nas consultas de produtos
- Invalidação de cache nas alterações
- Envio de evento Kafka quando `estoque < 5`
- Consumo do evento Kafka e registro de alerta no log

## Pré-requisitos

- JDK 17 ou superior
- Docker Desktop em execução

## Como executar

No Windows:

```powershell
.\mvnw.cmd quarkus:dev
```

No Linux ou macOS:

```bash
./mvnw quarkus:dev
```

O Quarkus Dev Services inicia automaticamente PostgreSQL e Kafka em containers Docker.

A API ficará disponível em:

```text
http://localhost:8080
```

A interface de desenvolvimento do Quarkus estará disponível em:

```text
http://localhost:8080/q/dev-ui
```

## Usuários padrão

| Nome | E-mail | Senha | Role |
|---|---|---|---|
| Admin Sistema | `admin@loja.com` | `admin123` | `ADMIN` |
| User Padrão | `user@loja.com` | `user123` | `USER` |

As senhas são armazenadas no banco de dados com hash BCrypt.

## Autenticação

Faça login:

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "email": "admin@loja.com",
  "senha": "admin123"
}
```

Utilize o token retornado nos endpoints protegidos:

```text
Authorization: Bearer <token>
```

## Endpoints

| Método | Endpoint | Permissão | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | Público | Cadastrar usuário |
| `POST` | `/auth/login` | Público | Autenticar usuário |
| `GET` | `/produtos` | `USER`, `ADMIN` | Listar produtos |
| `GET` | `/produtos/{id}` | `USER`, `ADMIN` | Buscar produto |
| `POST` | `/produtos` | `ADMIN` | Cadastrar produto |
| `PUT` | `/produtos/{id}` | `ADMIN` | Atualizar produto |
| `DELETE` | `/produtos/{id}` | `ADMIN` | Remover produto |

## Alerta Kafka

Ao cadastrar ou atualizar um produto com estoque inferior a cinco unidades, a aplicação publica e consome um evento no tópico:

```text
estoque-baixo
```

Exemplo de log:

```text
[ALERTA DE ESTOQUE] Produto 'Caderno Verde' com estoque baixo (2 unidades). E-mail de alerta enviado para estoque@empresa.com
```

## Testes manuais

O arquivo `api-produtos.http` contém exemplos de requisições para validar os endpoints.

Também é possível importar ou reproduzir essas requisições no Postman.

## Observação

Em modo de desenvolvimento, as tabelas são recriadas ao reiniciar a aplicação para facilitar os testes.