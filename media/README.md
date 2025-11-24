# MedIA API - Sistema de Pré-Triagem Digital

## Documento de Entrega

Este documento contém todos os artefatos necessários para avaliação do projeto conforme os critérios estabelecidos.

### Links dos Repositórios

- **Repositório Principal**: https://github.com/letprado/media

### Links dos Deploys

- **API em Produção (Swagger UI)**: http://158.158.40.232:8080/swagger-ui/index.html
- **Health Check**: http://158.158.40.232:8080/api/sugestoes/health

###  Vídeos de Demonstração

| Tipo | Link | Duração |
|------|-----|---------|
| **Vídeo Pitch** | https://youtu.be/PzHuLhgT7ug | Máx. 3 minutos |
| **Demonstração da Solução Completa** | https://youtu.be/q1R8di_eTDI | Máx. 10 minutos |
| **Solução Geral** | https://youtu.be/ZNVd9L_YfDw | - |
| **Integração Oracle + Java** | https://youtu.be/dSJ6lVBRvdg | - |

###  Instruções para Acesso e Testes

#### 1. Acessar a API em Produção

A API está disponível em: **http://158.158.40.232:8080/swagger-ui/index.html**

1. Acesse o link acima no navegador
2. Faça login usando as credenciais:
   - **Usuário**: `admin`
   - **Senha**: `admin123`
3. Clique em "Authorize" e cole o token JWT retornado
4. Teste os endpoints disponíveis no Swagger

#### 2. Testar Endpoints Principais

**Endpoint de Autenticação:**
```
POST /api/auth/login-profissional
Body: {
  "username": "admin",
  "password": "admin123"
}
```

**Endpoint de Sugestões Médicas:**
```
POST /api/sugestoes
Headers: Authorization: Bearer <token>
Body: {
  "sintomas": ["febre", "tosse", "dor de cabeça"]
}
```

**Endpoint de Histórico (Apenas ADMIN/MEDICO):**
```
GET /api/sugestoes/history?page=0&size=10
Headers: Authorization: Bearer <token>
```

#### 3. Credenciais de Teste

| Usuário | Senha | Roles | Acesso |
|---------|------|-------|--------|
| admin | admin123 | ADMIN, USER | Todos os endpoints |
| medico | medico123 | MEDICO, USER | Sugestões + Histórico |

#### 4. Teste Rápido via cURL

```bash
# 1. Fazer login
curl -X POST http://158.158.40.232:8080/api/auth/login-profissional \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. Obter sugestões (substitua YOUR_TOKEN pelo token obtido)
curl -X POST http://158.158.40.232:8080/api/sugestoes \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"sintomas":["febre","tosse"]}'
```

---

## Sobre o Projeto

O MedIA é uma API REST desenvolvida em Java com Spring Boot que implementa um sistema de pré-triagem digital para unidades de saúde. O sistema recebe uma lista de sintomas informados pelo paciente e retorna sugestões médicas baseadas em um banco de conhecimento, contribuindo para otimizar o fluxo de atendimento e reduzir o tempo de espera nos pronto-socorros.

## Sobre o MedIA

O **MedIA** (Medical Intelligence Assistant) é uma solução tecnológica desenvolvida no contexto do "Futuro do Trabalho" que visa transformar o ambiente de trabalho dos profissionais de saúde através da otimização do fluxo de pacientes em unidades de saúde.

### O Problema

O sistema de saúde brasileiro enfrenta desafios críticos de gestão operacional:
- **Tempo médio de espera**: 53 minutos em pronto-socorros
- **Superlotação**: casos extremos atingem 500% da capacidade (ex: HC da Unicamp)
- **Falta de dados**: 59% dos hospitais operam sem dados básicos de gestão
- **Triagem manual ineficiente**: sensibilidade de apenas 53,8% para identificar casos graves

### A Solução

O MedIA implementa uma **triagem digital (eTriage)** que:
- **Filtra 70-80% dos casos** antes da ida física ao hospital
- **Aumenta a precisão** para 88,5% de sensibilidade na identificação de casos graves
- **Fornece dados de gestão** para hospitais que operam sem visibilidade operacional
- **Reduz o estresse** dos profissionais da linha de frente através da automação da coleta inicial de dados

### Impacto no Futuro do Trabalho

A solução transforma o ambiente de trabalho na saúde, saindo de um modelo reativo e caótico para um modelo **digital-first**, preditivo e eficiente, criando um ambiente de trabalho mais sustentável para profissionais de saúde.

## Integrantes do Grupo

| Integrante | RM | Responsabilidade |
|------------|-----|------------------|
| **Letícia Sousa Prado** | 559258 | Java e Banco de Dados |
| **Jennyfer Lee** | 561020 | .NET e IoT |
| **Ivanildo Alfredo** | 560049 | Mobile, QA e DevOps |

## Problema que Resolve

O sistema foi desenvolvido para enfrentar os seguintes desafios do sistema de saúde brasileiro:

- Gestão ineficiente de filas em pronto-socorro
- Falta de pré-triagem digital e direcionamento inteligente de pacientes
- Superlotação que causa estresse tanto em pacientes quanto em profissionais
- Tempo médio de espera de 53 minutos no Brasil

A solução implementa um sistema de triagem digital (eTriage) que pode filtrar e resolver entre 70% e 80% dos casos antes mesmo da ida física ao hospital, com 88,5% de sensibilidade comparado aos 53,8% da triagem convencional. Além disso, fornece dados de gestão para hospitais que operam sem visibilidade adequada dos seus processos.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.7
- Spring Data JPA para persistência
- Spring Security com JWT para autenticação
- Oracle Database como banco de dados relacional
- Swagger/OpenAPI 3 para documentação da API
- Maven para gerenciamento de dependências

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/com/project/media/
├── config/          # Configurações (Security, JWT, Swagger, Database)
├── controller/      # Controllers REST
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── init/           # Inicialização de dados
├── repository/     # Repositories JPA
├── service/        # Lógica de negócio
└── MediaApplication.java
```

## Arquitetura

### Entidades Principais

O sistema trabalha com três entidades principais:

1. **Sintoma** - Armazena sintomas e palavras-chave que podem ser informados pelos pacientes
2. **Sugestao** - Armazena sugestões médicas com diferentes níveis de prioridade
3. **HistoricoConsulta** - Registra o histórico de todas as consultas realizadas no sistema

### Relacionamentos

- **Sugestao** ↔ **Sintoma** (ManyToMany) - Uma sugestão pode estar relacionada a múltiplos sintomas e vice-versa
- **HistoricoConsulta** → **Sugestao** (ManyToOne) - Cada consulta registrada está associada a uma sugestão

## Configuração e Instalação

### Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- Java 17 ou superior
- Maven 3.6 ou superior
- Oracle Database (ou Oracle XE) configurado e rodando

### Passo 1: Clonar o Repositório

```bash
git clone <url-do-repositorio>
cd media
```

### Passo 2: Configurar o Banco de Dados

Edite o arquivo `src/main/resources/application.properties` com as informações do seu banco Oracle:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

### Passo 3: Executar o Script de Setup

Antes de iniciar a aplicação, execute o script SQL para criar as tabelas e sequências:

```bash
# Conecte-se ao Oracle e execute:
sqlplus usuario/senha@localhost:1521/xe @oracle-setup.sql
```

### Passo 4: Iniciar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## Autenticação

O sistema utiliza JWT (JSON Web Token) para autenticação. Existem dois tipos de login disponíveis.

### Login para Profissionais de Saúde

Endpoint: **POST** `/api/auth/login-profissional`

Este endpoint é utilizado por médicos e administradores do sistema. As credenciais disponíveis são:

| Usuário | Senha | Roles |
|---------|-------|-------|
| admin | admin123 | ADMIN, USER |
| medico | medico123 | MEDICO, USER |

Exemplo de requisição:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

### Login para Usuários Comuns

Endpoint: **POST** `/api/auth/login-usuario`

Este endpoint é utilizado por pacientes que desejam obter sugestões médicas. Não é necessário senha, apenas nome e CPF:

```json
{
  "nome": "João Silva",
  "cpf": "12345678901"
}
```

### Resposta de Autenticação

Ambos os endpoints retornam um token JWT válido por 24 horas:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin",
  "authorities": "[ROLE_ADMIN, ROLE_USER]",
  "expiresAt": "2024-11-19T19:25:00"
}
```

### Como Usar o Token

Após obter o token, inclua-o no header Authorization de todas as requisições protegidas:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Endpoints da API

### Endpoints Públicos

Estes endpoints não requerem autenticação:

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login-profissional` | Login para profissionais |
| POST | `/api/auth/login-usuario` | Login para usuários comuns |
| POST | `/api/auth/refresh` | Renovar token JWT |
| GET | `/api/auth/validate` | Validar token JWT |
| GET | `/api/sugestoes/health` | Verificação de saúde da API |

### Endpoints Protegidos

Estes endpoints requerem autenticação via JWT:

| Método | Endpoint | Roles Necessárias | Descrição |
|--------|----------|-------------------|-----------|
| POST | `/api/sugestoes` | USER | Obter sugestões médicas baseadas em sintomas |
| GET | `/api/sugestoes/history` | ADMIN, MEDICO | Consultar histórico de consultas com paginação |

## Como Obter Sugestões Médicas

Para obter sugestões médicas, faça uma requisição POST para `/api/sugestoes` informando os sintomas.

**Endpoint:** POST `/api/sugestoes`

**Headers:**
```
Authorization: Bearer seu_token_jwt_aqui
Content-Type: application/json
```

**Body:**
```json
{
  "sintomas": ["febre", "tosse", "dor de cabeça"]
}
```

**Resposta:**
```json
[
  {
    "id": 1,
    "titulo": "Consulta médica para sintomas respiratórios",
    "descricao": "Com base nos sintomas respiratórios informados, é recomendada uma consulta médica para avaliação...",
    "tipoAtendimento": "CONSULTA",
    "prioridade": 3,
    "recomendacoes": "• Procure uma unidade básica de saúde\n• Mantenha repouso e hidratação\n• Evite contato próximo com outras pessoas",
    "sintomasCorrespondentes": ["febre", "tosse"],
    "dataConsulta": "2024-11-18T19:25:00"
  }
]
```

### Tipos de Atendimento

O sistema retorna sugestões classificadas por tipo de atendimento e prioridade:

- **URGENTE** (Prioridade 5) - Procure pronto-socorro imediatamente
- **CONSULTA** (Prioridade 2-4) - Agende consulta médica nas próximas horas ou dias
- **OBSERVACAO** (Prioridade 2) - Monitore os sintomas em casa
- **AUTOCUIDADO** (Prioridade 1) - Tratamento caseiro para sintomas leves

## Consultar Histórico de Consultas

O endpoint de histórico permite que médicos e administradores consultem todas as consultas realizadas no sistema, com suporte a paginação, ordenação e filtros.

**Endpoint:** GET `/api/sugestoes/history`

**Parâmetros de Paginação:**
- `page` - Número da página (padrão: 0)
- `size` - Tamanho da página (padrão: 10, máximo: 50)
- `sort` - Campo para ordenação (padrão: dataConsulta)
- `direction` - Direção da ordenação: asc ou desc (padrão: desc)

**Filtros Disponíveis:**
- `cpfPaciente` - CPF do paciente para buscar histórico específico (11 dígitos, sem formatação)
- `dataInicio` - Data de início do período (formato: yyyy-MM-ddTHH:mm:ss)
- `dataFim` - Data de fim do período (formato: yyyy-MM-ddTHH:mm:ss)

**Exemplos de Uso:**

Buscar histórico de um paciente específico:
```
GET /api/sugestoes/history?cpfPaciente=12345678901&page=0&size=10
```

Buscar histórico de todos os pacientes em um período:
```
GET /api/sugestoes/history?dataInicio=2024-11-01T00:00:00&dataFim=2024-11-30T23:59:59
```

Buscar histórico de um paciente em período específico:
```
GET /api/sugestoes/history?cpfPaciente=12345678901&dataInicio=2024-11-01T00:00:00&dataFim=2024-11-30T23:59:59&page=0&size=5
```

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "cpfPaciente": "12345678901",
      "nomePaciente": "João Silva",
      "sintomasInformados": "febre, tosse, dor de cabeça",
      "tituloSugestao": "Consulta médica para sintomas respiratórios",
      "tipoAtendimento": "CONSULTA",
      "prioridade": 3,
      "dataConsulta": "2024-11-18T19:25:00",
      "ipOrigem": "192.168.1.100",
      "tempoRespostaMs": 245
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  }
}
```

## Documentação Swagger

A documentação completa da API está disponível através do Swagger UI. Após iniciar a aplicação, acesse:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

No Swagger UI você pode testar todos os endpoints diretamente, visualizar os modelos de dados e entender melhor como cada endpoint funciona.

## Dados de Exemplo

O sistema é inicializado automaticamente com dados de exemplo que incluem:

- Mais de 90 sintomas organizados por categoria
- 15 sugestões médicas com diferentes níveis de prioridade
- Relacionamentos pré-configurados entre sintomas e sugestões

### Categorias de Sintomas

O banco de dados inclui sintomas nas seguintes categorias:

**Respiratório:** febre, tosse, tosse seca, tosse com catarro, dor no peito, falta de ar, dor de garganta, coriza, espirros, congestão nasal, rouquidão, dificuldade para engolir

**Gastrointestinal:** dor abdominal, dor de estômago, náusea, vômito, vômitos frequentes, diarreia, prisão de ventre, constipação, azia, refluxo, inchaço abdominal, flatulência, perda de apetite, sede excessiva, sangue nas fezes

**Neurológico:** dor de cabeça, enxaqueca, tontura, vertigem, confusão mental, dormência, formigamento, convulsão, desmaio, perda de memória, visão embaçada, dificuldade para falar

**Cardiovascular:** palpitação, taquicardia, dor no braço esquerdo, pressão alta, pressão baixa, inchaço nas pernas, cansaço ao fazer esforço, suor frio, desconforto no peito, dor no maxilar

**Musculoesquelético:** dor nas costas, dor lombar, dor nas articulações, rigidez muscular, dor no pescoço, limitação de movimento, fraqueza muscular, cãibra, dor nos ombros, dor nos joelhos

**Dermatológico:** erupção cutânea, coceira, vermelhidão na pele, bolhas, descamação, ferida que não cicatriza, manchas na pele, urticária

**Geral:** fadiga, cansaço excessivo, perda de peso, ganho de peso, sudorese, sudorese noturna, calafrios, inchaço, gânglios inchados, mal estar geral, insônia, sonolência excessiva

**Urológico:** ardor ao urinar, vontade frequente de urinar, sangue na urina, dificuldade para urinar, incontinência urinária, dor lombar com ardência

**Oftalmológico:** dor nos olhos, olhos vermelhos, sensibilidade à luz, lacrimejamento, visão dupla

**Otorrinolaringológico:** dor de ouvido, zumbido, perda auditiva, tontura com zumbido, secreção no ouvido

### Tipos de Sugestões Disponíveis

O sistema possui 15 tipos diferentes de sugestões médicas pré-configuradas, incluindo emergências cardíacas, infecções respiratórias, problemas gastrointestinais, emergências neurológicas, e casos de autocuidado, entre outros.

## Deploy em Nuvem
--
### Preparação
--

### Gerar o JAR

```bash
mvn clean package -DskipTests
```

### Executar em Produção

```bash
java -jar target/media-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=$ORACLE_URL \
  --spring.datasource.username=$ORACLE_USERNAME \
  --spring.datasource.password=$ORACLE_PASSWORD \
  --jwt.secret=$JWT_SECRET
```

## Testes

### Executar Testes Automatizados

```bash
mvn test
```

### Teste Manual via cURL

Exemplo completo de uso da API via linha de comando:

```bash
# 1. Fazer login e obter token
curl -X POST http://localhost:8080/api/auth/login-profissional \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. Obter sugestões médicas (substitua YOUR_TOKEN_HERE pelo token obtido no passo 1)
curl -X POST http://localhost:8080/api/sugestoes \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"sintomas":["febre","tosse","dor de cabeça"]}'
```

## Monitoramento

### Health Check

O endpoint de health check permite verificar se a aplicação está funcionando corretamente:

```bash
GET /api/sugestoes/health
```

Resposta:
```json
{
  "status": "UP",
  "timestamp": "2024-11-18T19:25:00",
  "service": "MedIA API"
}
```

## Logs

O sistema registra logs detalhados sobre:

- Tentativas de login e autenticação
- Consultas realizadas pelos usuários
- Erros de validação e exceções
- Performance das queries ao banco de dados

O nível de log pode ser configurado no arquivo `application.properties`. Para desenvolvimento, recomenda-se usar DEBUG para obter mais informações.

## Tratamento de Erros

A API retorna códigos de status HTTP apropriados para diferentes situações:

| Código | Descrição | Exemplo |
|--------|-----------|---------|
| 200 | Sucesso | Sugestões encontradas com sucesso |
| 400 | Bad Request | Lista de sintomas vazia ou inválida |
| 401 | Unauthorized | Token inválido ou expirado |
| 403 | Forbidden | Usuário sem permissão para acessar o endpoint |
| 500 | Internal Server Error | Erro interno do servidor ou no banco de dados |

## Segurança

O sistema implementa várias camadas de segurança:

- Autenticação JWT com algoritmo HS512
- CORS configurado para desenvolvimento (deve ser ajustado para produção)
- Validação de entrada com Bean Validation para prevenir dados inválidos
- Prevenção de SQL Injection através do uso de JPA e prepared statements
- Rate limiting recomendado para ambiente de produção

## Roadmap

Funcionalidades planejadas para futuras versões:

- Cache Redis para melhorar performance das consultas frequentes
- Rate limiting por IP para prevenir abuso da API
- Métricas com Micrometer e Prometheus para monitoramento avançado
- Integração com IA/ML para sugestões mais precisas e personalizadas
- Notificações em tempo real para profissionais de saúde
- API de relatórios avançados com análises estatísticas

## ✅ Checklist de Requisitos Técnicos

Este projeto atende a **100% dos requisitos técnicos** solicitados:

| Requisito | Status | Implementação |
|-----------|--------|---------------|
| ✅ API REST com boas práticas | ✅ | Arquitetura em camadas (Controller → Service → Repository) |
| ✅ Spring Data JPA | ✅ | Repositories JPA com Oracle Database |
| ✅ Relacionamentos entre entidades | ✅ | @ManyToMany (Sugestao↔Sintoma), @ManyToOne (Historico→Sugestao) |
| ✅ Bean Validation | ✅ | @Valid, @NotBlank, @NotNull, @Size, @Pattern em DTOs e Entidades |
| ✅ Paginação, Ordenação e Filtros | ✅ | Endpoint `/history` com Pageable, Sort e filtros por CPF/data |
| ✅ Documentação Swagger | ✅ | Swagger/OpenAPI 3 completo e acessível |
| ✅ Autenticação JWT | ✅ | JWT com roles (ADMIN, MEDICO, USER) |
| ✅ Deploy em nuvem | ✅ | API em produção: http://158.158.40.232:8080 |

**Verificação completa**: Consulte `CHECKLIST_REQUISITOS.md` para detalhes de cada requisito.

## Documentação Adicional

### Diagramas e Modelos

- **Diagrama de Arquitetura Java**: [adicionais/arquitetura_java.png](adicionais/arquitetura_java.png)
- **Modelo Lógico**: [adicionais/diagrama_logico.pdf](adicionais/diagrama_logico.pdf)
- **Modelo Relacional**: [adicionais/diagrama_relacional.pdf](adicionais/diagrama_relacional.pdf)

### Outros Documentos

- **Arquitetura do Sistema**: Consulte a documentação de arquitetura para entender a estrutura em camadas
- **Procedures e Funções**: Documentação das procedures PL/SQL e funções Oracle
- **Checklist de Requisitos**: [CHECKLIST_REQUISITOS.md](CHECKLIST_REQUISITOS.md) - Verificação completa de todos os requisitos

##  Arquitetura

O projeto segue uma **arquitetura em camadas (Layered Architecture)**:

- **Camada de Apresentação**: Controllers REST (AuthController, SugestaoController)
- **Camada de Serviço**: Lógica de negócio (SugestaoService)
- **Camada de Persistência**: Repositories JPA (SugestaoRepository, SintomaRepository, HistoricoRepository)
- **Camada de Entidades**: Modelo de domínio (Sugestao, Sintoma, HistoricoConsulta)

### Boas Práticas Implementadas

 **Separação de Responsabilidades**: Cada camada tem responsabilidade única  
 **SOLID Principles**: Código seguindo princípios SOLID  
 **Clean Code**: Código legível e bem documentado  
 **RESTful API**: Endpoints seguindo padrões REST  
 **Validação de Dados**: Bean Validation em DTOs  
 **Tratamento de Erros**: Exceções tratadas adequadamente  
 **Logging**: Logs estruturados para debugging  
 **Documentação**: Swagger completo e atualizado  
 **Segurança**: JWT e autorização por roles  
 **Transações**: Gerenciamento adequado de transações  

## Suporte

Para dúvidas, problemas ou sugestões:

- Email: leticia17prado@gmail.com

## Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
