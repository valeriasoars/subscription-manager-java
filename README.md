# Subscription Manager

O **Subscription Manager** é uma API desenvolvida em Spring Boot para gerenciar o ciclo de vida de planos e assinaturas de clientes. O sistema utiliza arquitetura orientada a eventos (*Outbox Pattern*) integrada ao **RabbitMQ** para garantir a entrega confiável de notificações e atualizações de cobrança, além de persistência segura utilizando **PostgreSQL**.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3.4+**
* **Spring Data JPA** (Persistência e ORM)
* **Spring AMQP (RabbitMQ)** (Mensageria assíncrona)
* **PostgreSQL** (Banco de dados relacional)
* **Lombok** (Produtividade e redução de boilerplate)
* **Jackson** (Mapeamento JSON com `@JsonProperty`)

---

## 📦 Dependências Principais (`pom.xml`)

```xml
<dependencies>
    <!-- Web e REST -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Banco de Dados e Persistência -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!-- Mensageria (RabbitMQ) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    <!-- Utilitários -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

## 🚀 Principais Rotas da API

### 📋 Planos (`/api/plans`)
* `POST /api/plans` - Cria um novo plano 

### 💳 Assinaturas (`/api/subscriptions`)
* `POST /api/subscriptions` - Inicia uma assinatura com status `PENDING` e calcula a próxima cobrança.
* `POST /api/subscriptions/webhook` - Recebe confirmação de pagamento do gateway e dispara eventos.
* `PUT /api/subscriptions/{id}/change-plan` - Realiza a alteração de plano (Upgrade/Downgrade).
* `DELETE /api/subscriptions/{id}/cancel` - Cancela uma assinatura ativa.

---

## ⚙️ Como Baixar e Rodar Localmente

### Pró-requisitos
* **Java 21** instalado.
* **Maven 3+** instalado.
* Instâncias locais (ou via Docker) do **PostgreSQL** e **RabbitMQ** rodando.

### 1. Clonar o Repositório
```bash
git clone https://github.com/valeriasoars/subscription-manager-java.git
cd subscription-manager
```

### 2. Configurar o `application.properties`
Ajuste suas credenciais de banco e mensageria em `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/subscription_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

server.error.include-message=always
```

### 3. Compilar e Rodar a Aplicação
```bash
mvn clean install
mvn spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

---

## 🧪 Como Testar no Postman

### 1. Criar um Plano (POST)
* **URL:** `http://localhost:8080/api/plans`
* **Body (JSON):**
```json
{
  "name": "Plano Premium Anual",
  "price": 299.90,
  "billing_cycle": "ANUAL"
}
```

### 2. Criar uma Assinatura (POST)
* **URL:** `http://localhost:8080/api/subscriptions`
* **Body (JSON):**
```json
{
  "plan_id": "coloque-o-uuid-do-plano-criado",
  "customer_email": "cliente@email.com"
}
```

### 3. Simular Pagamento do Webhook (POST)
* **URL:** `http://localhost:8080/api/subscriptions/webhook`
* **Body (JSON):**
```json
{
  "subscription_id": "coloque-o-uuid-da-assinatura",
  "amount": 299.90,
  "event": "PAYMENT_CONFIRMED"
}
```
