# Кратко о запросах на mapping /cloud
Глобальная точка входа представлена следующим образом
`
[path-to-server]/cloud/[методы]
`
## Путь для загрузки
При помощи его создаётся запись на сервере для сохранения данных. 
- Метод для работы: `POST`
- Путь до endpoint: `/cloud/upload`

Список возможных параметров:
- password (Обязательный) - пароль на запись (устанавлииватся клиентом);
- single [значение по стандарту: false] - Режим одинарного доступа; 
- type [] - тип установленного пароля, а точнее его вид; 
- days [значение по стандарту: 365] - количество дней сколько будет храниться запись;

__Тело запроса__, это запись которая должна храниться. 

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
Ответ токен используемая для последующего доступа
Пример ответа:
``` txt 
33352 <- token 
```

## Просмотр информации о записис
Просмотр информации о сохраненных данных, для последующего получения контекстной информации.
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
Используется для получения информации сохраненных по токену.
- Метод для работы: `GET`
- Путь до end point: `/cloud/{token }`

Список возможных параметров:
- password (Обязательный) - пароль на запись (устанавливается клиентом);
- type [] - тип установленного пароля, а точнее его вид; 

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
Ответ токен используемая для последующего доступа
Пример ответа:
``` json
{
  "some": "some"
}
```

## Удаление записи сохраненной на сервере
Используется для получения информации сохраненных по токену.
- Метод для работы: `DELETE`
- Путь до end point: `/cloud/{token }`
Список возможных параметров:
- password (Обязательный) - пароль на запись (устанавливается клиентом);
- type [] - тип установленного пароля, а точнее его вид; 

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
Ответ токен используемая для последующего доступа
Пример ответа:
В качестве успеха возвращает код 200 

# Коды возвращаемые маппингом и ошибки. 
Ошибки, возвращаемые в сервере, фигурируют причины почему сервер тебя посылает.
В качестве успеха везде возвращает код `200`.
В случае неуспеха коды возвращается следующие:
- upload, GET, PATCH -> `400`;
- DELETE -> `404`

#  API информационные
## Типы паролей
Перечисление всех возможных типов паролей.
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

# Пример клиента, который использовался в качестве примера
Ниже приведены пример клиента сформулированного на js, библиотеке node.js, виде featch запроса. 
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


