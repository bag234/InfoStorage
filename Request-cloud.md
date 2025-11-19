# Кратко о запросах на mapping /cloud
Глобальная точка вохода представленна следующим образом
`
[path-to-server]/cloud/[методы]
`
## Путь для загрузки
При помощие его создаеться запись на сервере для сохранения данных. 
- Метод для работы: `POST`
- Путь до end point: `/cloud/upload`

Список возможных параметров:
- password (Обязательный) - пароль на запись (устанавлииватся клиентом);
- single [значение по стандарту: false] - Режим одинарного доступа; 
- type [] - тип устоновленного пароля, а точнее его вид; 
- days [значение по стандарту: 365] - количество дней сколько будет храниться запись;

__Тело запроса__, это запись которая должна харниться. 

**пример**
запрос:
`http://contact.by.mrbag.org:8881/cloud/upload?password=22&single=true&days=4`
``` js
const url = 'http://contact.by.mrbag.org:8881/cloud/upload?password=22&single=true&days=4';
const options = {
  method: 'POST',
  body: '{"some":"some"}'
};

```
Ответ токен используеммая для последующего доступа
Пример ответа:
``` txt 
33352 <- token 
```

## Просмотр информации о записис
Просмотр инофрмации о сохраненных данных, для последующего получения контекстной информации.
- Метод для работы: `PATCH`
- Путь до end point: `/cloud/{token}`

**Тело запроса** пустое.
**Пример:**
Запрос:
`http://contact.by.mrbag.org:8881/cloud/33352`
``` js 
const url = 'http://contact.by.mrbag.org:8881/cloud/33352';
const options = {
  method: 'PATCH',
};
```

Пример ответа: 
``` json 
{
  "key": "33352", // токен который использовался для доступа
  "type": "ANOTHER", // Тип устоновленного пароля
  "time": "2025-11-23T12:51:27.333395812", // дата до которой будет храниться
  "single": true // удалиться ли запись после первого запроса
}
```

## Получение сохраненных данных
Испольхуеться для получения информации сохраненных по токену.
- Метод для работы: `GET`
- Путь до end point: `/cloud/{token }`

Список возможных параметров:
- password (Обязательный) - пароль на запись (устанавлииватся клиентом);
- type [] - тип устоновленного пароля, а точнее его вид; 

**Тело запроса** пустое.
**Пример**
Запрос:
`http://contact.by.mrbag.org:8881/cloud/33352?password=22&type=ANOTHER`
``` js
const url = 'http://contact.by.mrbag.org:8881/cloud/33352?password=22&type=ANOTHER';
const options = {
  method: 'GET',
};

```
Ответ токен используеммая для последующего доступа
Пример ответа:
``` json
{
  "some": "some"
}
```

## Удаление записи сохраненной на сервере
Испольхуеться для получения информации сохраненных по токену.
- Метод для работы: `DELETE`
- Путь до end point: `/cloud/{token }`
Список возможных параметров:
- password (Обязательный) - пароль на запись (устанавлииватся клиентом);
- type [] - тип устоновленного пароля, а точнее его вид; 

**Тело запроса** пустое.
**Пример**
Запрос:
`http://contact.by.mrbag.org:8881/cloud/33352?password=22&type=ANOTHER`
``` js
const url = 'http://contact.by.mrbag.org:8881/cloud/33352?password=22&type=ANOTHER';
const options = {
  method: 'DELETE',
};

```
Ответ токен используеммая для последующего доступа
Пример ответа:
В качестве успеха возращает код 200 

# Коды возращаемыые маппингами и ошибки. 
Ошибки возращаемые в сервере фигуриют причны почему сервер тебя посылает.
В качестве успеха везде возращеет код `200`.
В случае не успеха коды возращаеться слудкющие:
- upload, GET, PATCH -> `400`;
- DELETE -> `404`

#  API информационные
## Типы паролей
Перечесление всех возможных типов паролей.
- Метод для работы: `GET`
- Путь до end point: `/info/password`
Пример:
Запрос на endpoint: 
`http://contact.by.mrbag.org:8881/info/password`
Пример выполнения: 
``` json
[
  "PASS",
  "PIN",
  "NONE",
  "ANOTHER"
]
```

# Пример клиента который использовался в качестве примера
Ниже приведе пример клиента сфомулированного на js, библиотеке node.js, ввиде featch запроса. 
Пример реализации:
``` js 
const fetch = require('node-fetch');

const url = 'http://contact.by.mrbag.org:8881/info/password';
const options = {
  method: 'GET',
  headers: {
    Accept: '*/*',
    'User-Agent': 'Thunder Client (https://www.thunderclient.com)',
    'Content-Type': 'application/json'
  },
  body: '{"some":"some"}'
};

try {
  const response = await fetch(url, options);
  const data = await response.json();
  console.log(data);
} catch (error) {
  console.error(error);
}
```
