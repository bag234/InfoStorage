# InfoStorage

InfoStorage — это небольшое Spring Boot приложение для хранения любой информации. Оно предоставляет два способа передачи данных:
- через кодирование в QR-код для удобной передачи
- через облачную передачу без создания пользовательских аккаунтов

Приложение поддерживает локальное и облачное хранение, а также интеграцию с Redis для управления ключами.

## Контроллеры
- **AccessApi** — REST API для получения, сохранения и удаления информации, а также для последующей генерации QR-кодов.
- **CloudApi** — REST API для облачной передачи и получения данных без регистрации пользователя.

## Основные возможности
- REST API для доступа к данным
- Поддержка облачного и локального хранения
- Генерация QR-кодов
- Интеграция с Redis для хранения ключей
- Гибкая настройка через `application.yml`

## Структура проекта
```
src/
  main/
    java/org/mrbag/InfoStorage/
      InfoStorageApplication.java         # Точка входа
      Controller/                        # REST API
        AccessApi.java
        CloudApi.java
      Storge/                            # Логика хранения
        IdUtils.java
        KeyAccess.java
        RedisConfgStore.java
        Store.java
        Cloud/
          Cloud.java
          CloudKeyAccess.java
          TypeAccessPassword.java
      Util/
        AppInfo.java
        SimpleCodeGenerator.java
    resources/
      application.yml                    # Конфигурация
      static/                            # Статические файлы
      templates/                         # Шаблоны
  test/
    java/org/mrbag/InfoStorage/
      InfoStorageApplicationTests.java   # Тесты приложения
      TestingCloudStorage.java           # Тесты облачного хранилища
      TestingQrcodeStorage.java          # Тесты QR-кодов
    resources/
      application.yml                    # Тестовая конфигурация
```

## Быстрый старт
1. Установите JDK 17+
2. Склонируйте репозиторий:
   ```sh
   git clone https://github.com/bag234/InfoStorage.git
   cd InfoStorage
   ```
3. Соберите проект:
   ```sh
   ./mvnw clean package
   ```
4. Запустите приложение:
   ```sh
   java -jar target/InfoStorage*.jar
   ```

## Конфигурация
Все настройки находятся в `src/main/resources/application.yml`. Пример:

## Запуск тестов
```sh
./mvnw test
```

## Переменные окружения
- `APP_mainURI` — адрес для Redis

Другие переменые среды:
```properis
app:
#Метки для заголовков при хранение в redis 
  header: "data-test" 
  cloud: "store-test"
  freaz-tag: "freaz"
```


## CI/CD
Сборка и деплой автоматизированы через Jenkins (см. `jenkinsfile`).

## Контакты
- Автор: bag234
- GitHub: [bag234/InfoStorage](https://github.com/bag234/InfoStorage)
- EMail: work@mrbag.org

## Лицензия
MIT
