# Jobs

Aplicação REST em java para agendar, listar e cancelar jobs periódicos.
A execução do job é um `System.out.println` da mensagem passada como parâmetro.

## Tecnologia
- Java
- Spring Boot
 - Spring WebMvc

## Subindo a aplicação

- Diretamente com java
Após baixar o projeto e compilar com `.mvnw clean install`, basta rodar o seguinte comando:

`
.mvnw spring-boot:run
`

- Container Docker
Outra possibilidade é subir a aplicação containerizada. Com o docker instalado, rode:

`
./docker.sh
`

## Acessando a aplicação

- Criar um job
`
curl -v -H "Content-Type: application/json" -X POST -d '{"name": "<NOME DO SEU JOB>", "msg": "<MENSAGEM DO SEU JOB>", "cron": "<CRON DO SEU JOB>"}' http://localhost:8080/api/jobs
`

ex: `curl -v -H "Content-Type: application/json" -X POST -d '{"name": "Job loucao", "msg": "Mensagem loucona", "cron": "1/15 * * * * *"}' http://localhost:8080/api/jobs`

- Listando os jobs
`
curl http://localhost:8080/api/jobs
`

Saída esperada:
`
[{"name":"job-name","msg":"Hello World","cron":"1/5 * * * * *"},{"name":"Job loucao","msg":"Mensagem loucona","cron":"1/15 * * * * *"}]
`

- Removendo um job
`
curl -X DELETE http://localhost:8080/api/jobs/<NOME DO JOB>
`

ex: `curl -X DELETE http://localhost:8080/api/jobs/Job%20loucao`