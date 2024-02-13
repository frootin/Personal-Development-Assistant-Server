# Personal-Development-Assistant-Server
## Инструкция по запуску сервера
### Разверните базу данных
1. Запустите cmd из папки source/tomcat.
2. Для задания кодировки выполните:
```
set PGCLIENTENCODING=UTF8
```
3. Для создания БД:
```
psql -U postgres -h localhost -p 5432 -f scripts/create_db.sql
```
4. Для заполнения БД:
```
psql -U postgres -h localhost -p 5432 -f scripts/manual_pop.sql
```
5. Если понадобится очистить БД (например, при смене схемы), то:
```
psql -U postgres -h localhost -p 5432 -f scripts/drop_db.sql
```
### Запустите приложение
1. Убедитесь, что у вас установлены: Java 17, Tomcat 9, Maven 3.
2. Перейдите в папку source/tomcat.
3. Если пароль пользователя postgres отличается от qwerty, то в файле src/main/resources/application.properties отредактируйте:
```
spring.datasource.password=вашпароль
```
4. Из папки source/tomcat запустите:
```
mvn spring-boot:run
```

## Информация по адресам
* Адрес приложения: http://localhost:8080/assistant
* Адрес для информации на 3 дня на главном экране: http://localhost:8080/assistant/api/{year}/{month}/{day}
* Адрес для работы с задачами: http://localhost:8080/assistant/api/tasks (примеры запросов в source/tomcat/http_req_examples)
