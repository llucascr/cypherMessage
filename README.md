# ⚙️ Variáveis de Ambiente — CypherMessage

Antes de iniciar o projeto, configure as variáveis de ambiente conforme abaixo:

## 🗄️ Banco de Dados MongoDB

| Variável | Descrição | Valor |
|-----------|------------|-------|
| **`MONGO_URI`** | String de conexão com o banco de dados MongoDB. | ```mongodb+srv://mongodb:mongodb@clustercyphermessage.64kwdu2.mongodb.net/?retryWrites=true&w=majority&appName=ClusterCypherMessage``` |
| **`DATABASE_NAME`** | Nome do banco de dados a ser utilizado. | `cypherMessage` |

---

## 🔐 Criptografia

| Variável | Descrição | Valor |
|-----------|------------|-------|
| **`INIT_VECTOR`** | Vetor de inicialização usado no processo de criptografia. | `abcdef9876543210` |
| **`SECRET_KEY`** | Chave secreta utilizada para criptografar e descriptografar mensagens. | `1234567890123456` |
