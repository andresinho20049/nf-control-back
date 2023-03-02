# Controle de NF
API Desenvolvida para projeto Controle de NF Freelancer, teste 8612.

[preview](https://nf-control-front.vercel.app)
 - E-mail:  

## API Rest com autenticação Oauth2 e documentação usando Swagger

**Sobre**:
 - Controle de acesso baseado em regras com JWT
 - Tecnologias utilizadas
    - Java 8
    - Spring Boot 2.7.0
    - Spring JPA latest
    - Spring Security 5.6.4     
    - Spring Secutiry Oauth2 Autoconfigure 2.1.5
    - Springfox (Swagger) 3.0.0
    - PostgresSql
    - Lombok - [Ajuda para configurar o lombok](https://projectlombok.org/setup/eclipse)
    - Project Maven
- Application.properties - Padrão
    - Port: 5000
    - Profile: dev
    - Base path: /api
    - Encrypt: bcrypt
    - Hibernate DDL: update
- ExceptionHandler
    - ProjectException:
        - Status: 400
        - Description: Exceção provocada, n motivos, mas principalmente regra de negócio \
        (A ideia aqui é o Dev em qualquer ponto do código fazer um throw new ProjectException(msg), que será retornado um 400 com a mensagem)
    - AuthorizationException:
        - Status: 403
        - Description: Acesso negado, mesmo conceito do ProjectException, porem utilizada para informar que o usuário logado não tem permissão para executar a ação.

## Começando
1. Git clone project
 ```git
    git clone https://git.vibbra.com.br/nf-control-8612/nf-control-back.git
 ```

2. cd folder
```cmd
  cd ./nf-control-back
```

3. Build Project
```mvn
    mvn clean && mvn install package
```

4. Start Project
```mvn
  mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=5000
```

5. Open Swagger
  - No navegador digite a url: http://localhost:5000/api/swagger-ui/index.html
  - ou se preferir [clique aqui](http://localhost:5000/api/swagger-ui/index.html)

### Config Lombok

> Neste exemplo vou considerar o uso do Eclipse, mas basta adaptar ao seu uso, ok.

1. Este projeto usa o lombok, se você ainda não instalou este plugin no seu eclipse, clique em:
Ajuda > Instalar novo software...
No campo 'Work with', cole: https://projectlombok.org/p2
Marque lombok e depois click em finish

2. Etapa opcional: Click Project > Update maven project > Ok

## Modelos
### User
```json
{
  "id": 2,
  "name": "Andre",
  "email": "andre.andresinho2009@hotmail.com",
  "updatePassword": false,
  "active": true,
  "roles": [
    {
        "name": "ROLE_ADMIN"
    }
  ]
}
```

### Role
```json
[
  {
    "name": "ROLE_ADMIN"
  },
  {
    "name": "ROLE_VIEW_USER"
  },
  {
    "name": "ROLE_CREATE_USER"
  },
  {
    "name": "ROLE_UPDATE_USER"
  },
  {
    "name": "ROLE_DISABLE_USER"
  }
]
```

### JWT Payload Exemplo
```json
{
  "aud": [
    "restservice"
  ],
  "updatePassword": true,
  "user_name": "andre.andresinho2009@hotmail.com",
  "scope": [
    "all"
  ],
  "id": 2,
  "name": "André",
  "exp": 1677677296,
  "authorities": [
    "ROLE_ADMIN"
  ],
  "jti": "77e8d226-25ae-4a26-a4bb-d04059ac8e5d",
  "client_id": "52da334b25d96304a09901705846663fef41ce8f"
}
```

## Mail Service
Foi desenvolvido a implementação de envio de e-mails utilizando SMTP do Google, porem o google removeu a possibilidade de permitir apps menos seguros. Então no momento o envio de e-mail não esta funcionando.

```Java
  private JavaMailSender getMailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setProtocol("smtp");
		sender.setHost("smtp.gmail.com");
		
		sender.setPort(587);
		sender.setUsername(username);
		sender.setPassword(password);

		Properties mailProps = new Properties();

		mailProps.put("mail.smtp.auth", "true");
		mailProps.put("mail.smtp.starttls.enable", "true");
		mailProps.put("mail.smtp.starttls.required", "false");
		mailProps.put("mail.smtp.ssl.enable", "false");

		sender.setJavaMailProperties(mailProps);

		return sender;
	}
```

As credenciais utilizadas no portal são carregadas do environment.

## Lançamento de Notas
Conforme apresentado abaixo as notas são filtradas por ano, podendo validar o lançamento de notas e realizar as estimativas de faturamento anual.

```Java
  @Override
	public Page<Invoice> findByPage(Integer year, Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);
		
		Long userId = SecurityContext.getUserLogged().getId();

		if(year != null)
			return invoiceRepository.findByYearAndUser(year, userId, pageRequest);
		
		return invoiceRepository.findByUser_Id(userId, pageRequest);
	}
```

Todos os valores lançados no portal são vinculados ao usuálrio logado, então cada usuário tem permissão de visualizar apenas os proprios lançamentos.

> **Projeto:** NF Control \
> **Dev:** André Carlos [(andresinho20049)](https://github.com/andresinho20049)       
<!-- > **Url-Teste:** https://liga-bjj-back-api.up.railway.app/api -->
