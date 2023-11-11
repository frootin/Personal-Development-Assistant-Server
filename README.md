# Personal-Development-Assistant-Server
## Инструкция по запуску сервера
### Скомпилируйте приложение
1. Убедитесь, что у вас установлены: Java 17, Tomcat 9, Maven 3.
2. Перейдите в папку source/tomcat.
3. Из этой папки запустите:
```
mvn clean package
```
4. Проверьте, что существует файл source/tomcat/target/assistant.war.

### Запустите сервер Tomcat у себя на машине:
```
catalina.bat start
```
Предполагается, что сервер Tomcat 9 уже настроен.
### Откройте стартовую страницу Tomcat
Перейдите на: http://localhost:8080
### Разверните приложение
1. Откройте графический менеджер приложений (кнопка Manager App)
![Alt text](/instruction-images/1.png)
2. В появившемся всплывающем окне введите логин и пароль из файла ваш-путь-к-tomcat/conf/tomcat-users.xml. Пользователи определены в конце файла, найдите пользователя с ролью "manager-gui".
3. Если приложение не устанавливалось ранее, то сразу перейти в раздел Развернуть -> WAR файл для развёртывания -> Выбор файла. Там выбрать файл source/tomcat/target/assistant.war:
![Alt text](/instruction-images/2.png)
4. После успешного выбора файла нажмите "Развернуть":
![Alt text](/instruction-images/3.png)
5. В списке приложений должно появиться "/assistant":
![Alt text](/instruction-images/4.png)
6. Если до этого была развёрнута старая версия приложения, то начать пункт 3 с её удаления (такая возможность должна быть в столбце "Команды"):
![Alt text](/instruction-images/5.png)

### Клиент может запрашивать данные с сервера!
После завершения работы с сервером не забудьте:
```
catalina.bat stop
```

## Информация по адресам
* Адрес приложения: http://localhost:8080/assistant
* Адрес для информации по задаче: http://localhost:8080/assistant/task
* Адрес для информации за сегодня: http://localhost:8080/assistant/today
* Адрес для информации за календарный день: http://localhost:8080/assistant/day
