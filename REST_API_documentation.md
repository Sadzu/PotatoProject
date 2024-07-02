# Электронный кошелек Картошка
# REST API Documentation

В качестве ответа возвращается application/json

В качестве тела запроса используется application/json

# Endpoints:
users, sessions, wallets, payments, remittances

Пользователи, сессии, кошельки, счета на оплату и денежные переводы соответственно

# Endpoint users

### POST /users - отвечает за создание пользователя

#### Body:

```
{
    "phone": long,
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "email": "string",
    "birthDate": "string",
    "password": "string",
}
```

- phone - номер телефона пользователя, включает в себя 11 цифр, начинается с "7"
также используется как уникальный id, один номер телефона - один пользователь

- surname, name, patronymicName - ФИО пользователя,
допускаются только буквы русского алфавита, первые буквы - заглавные,
не более 50 символов по отдельности,
отчество является опциональным полем

- email - адрес электронной почты пользователя, записывается в стандартном формате
к электронной почте может быть привязан только один пользователь

- birthDate - дата рождения пользователя в формате ISO 8601 в качестве строки.

- password - пароль пользователя. Особые требования к паролю:

  - от 8 до 64 символов
  - только латинские символы, цифры, знаки ?!
  - обязательно наличие минимум 1 буквы верхнего и нижнего регистра, цифры и знака
  - Пароль хранится исключительно в БД в хэшированном виде, алгоритм хэширования BCrypt


#### Example body:

```
{
    "phone": 78005553535,
    "surname": "Иванов",
    "name": "Иван",
    "patronymicName": "Иванович",
    "email": "example@example.com",
    "birthDate": "2012-12-12",
    "password": "Passw0rd?"
}
```

#### Response:

##### Http status codes: 200

```
{
    "phone": long,
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "email": "string",
    "birthDate": "string",
    "create": "string"
}
```

В качестве ответа возвращаются данные пользователя, кроме пароля и временная метка создания пользователя.
- created - временная метка создания пользователя. Заполняется автоматически при успешном запросе.
  Хранится в формате ISO 8601 в качестве строки.

- changed - временная метка последнего изменения данных пользователя. Заполняется автоматически при успешном запросе.
  При создании пользователя считается одинаковой с меткой создания. Хранится в формате ISO 8601 в качестве строки.


##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 402 - некорректные данные

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status codes 500:
- 504 - превышено время ожидания от сервера

### GET /users - отвечает за получение данных о пользователе

#### Body:

Тело запроса отсутствует

#### Response:

#### URL параметры:

phone - номер телефона

#### Example:

GET /users?phone=78005553535

Response: 

##### Http status codes: 200

```
{
    "phone": 8005553535,
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "email": "string",
    "birthDate": "string"
}
```

В качестве ответа возвращается пользователь с запрошенным номером телефона.

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:

- 4 - пользователь не найден
- 2 - некорректный формат номера телефона

###### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message;
}
```

В качестве ответа возвращается сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 500:

- 4 - превышено время ожидания от сервера

### PATCH /users/{phone} - отвечает за изменение данных о пользователе

Разрешено менять только ФИО и дату рождения

#### Body:

```
{
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "birthDate": "string"
}
```

Все параметры опциональные. Разрешается менять только их. Если в запросе не передано
ни одного параметра в теле запроса, то ничего не будет изменено.
При изменении хотя бы одного поля записывается новая дата изменения пользователя.

#### Response:

##### Http status codes: 200

```
{
    "message": "success_message"
}
```

В качестве ответа возвращается сообщение об успешном изменении данных

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status codes 400:

- 2 - некорректные даныне
- 4 - пользователь с данным номером телефона не найден

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status codes 500:

- 4 - превышено время ожидания от сервера

# Endpoint sessions

### POST /sessions - отвечает за создание сессии

#### Body:

```
{
    "phone": long,
    "password": "string"
}
```

 - phone - номер телефона пользователя, к которому привязана сессия
 - password - пароль пользователя, хранится исключительно на сервере в качестве "соленого" хэша

#### Response:

##### Http status codes: 200

```
{
    "accessToken": "string",
    "refreshToken": "string",
    "expiresIn": long,
    "sessionId": integer
}
```

В качестве ответа возвращается два токена и время жизни токена доступа.

- accessToken - токен доступа
- refreshToken - токен обновления
- expiresIn - время истечения жизни токена доступа
- sessionId - ID сессии. Формат:
    - Положительное число - активная сессия
    - Отрицательное число - "протухшая" сессия

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Неверный логин или пароль

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### GET /sessions/{phone} - отвечает за получение информации о сессиях пользователя

#### Body:

Тело запроса отсутствует

#### Response: 

##### Http status codes: 200

```
[
{
    "sessionId": integer,
    "phone": long
    "openTimestamp": long,
    "closedTimestamp": long
},
{
    "sessionId": integer,
    "phone": long
    "openedTimestamp": long,
    "closedTimestamp": long
},
]
```

В качестве ответа возвращает массив всех сессий пользователя

- sessionId - ID сессии. Формат:
    - Положительное число - активная сессия
    - Отрицательное число - "протухшая" сессия
- phone - номер телефона владельца сессии
- openedTimestamp - время открытия сессии
- closedTimestamp - время закрытия сессии. Поле отсутствует, если сессия активна

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный формат номера телефона
- 4 - Не найден пользователь с таким номером телефона

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### GET /sessions/{phone}/{sessionId} - отвечает за получение информации о конкретной сессии пользователя

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
    "sessionId": integer,
    "phone": long
    "openTimestamp": long,
    "closedTimestamp": long
}
```

В качестве ответа возвращает информацию о сессии

- sessionId - ID сессии. Формат:
    - Положительное число - активная сессия
    - Отрицательное число - "протухшая" сессия
- phone - номер телефона владельца сессии.
- openedTimestamp - время открытия сессии
- closedTimestamp - время закрытия сессии. Поле отсутствует, если сессия активна

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный номер или ID сессии
- 4 - Не найден пользователь с таким номером или сессия с таким ID

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### PATCH /sessions/{phone}/{sessionId} - отвечает за закрытие сессии

#### Body:

```
{
    "sessionId": integer
}
```

#### Response:

##### Http status codes: 200

```
{
    "message": "success_message"
}
```

В качестве ответа возвращает сообщение об успехе

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:

- 2 - Некорректный номер телефона или ID сессии
- 4 - Не найден пользователь с таким номером или сессия с таким ID

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

# Endpoint wallets

### GET /wallets/{walletId} - отвечает за получение информации о кошельке

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
    "phone": long,
    "walletId": long,
    "balance": unsigned integer
}
```

В качестве ответа возвращает информацию о кошельке

- phone - номер телефона владельца кошелька
- walletId - номер кошелька
- balance - баланс кошелька в д. е. Не может быть отрицательным. Начальное значение - 100

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный формат номера кошелька
- 4 - Не найден такой номер кошелька

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

# Endpoint payments

### POST /payments

#### Body:

```
{
    "uuid": long,
    "cost": unsigned integer,
    "sellerPhone": long,
    "buyerPhone": long,
    "status": integer,
    "date": "string",
    "comment": string
}
```

- uuid - номер счета, ID счета
- cost - стоимость счета (сколько требуется к оплате)
- sellerPhone - номер телефона отправителя счета (продавца)
- buyerPhone - номер телефона получателя счета (покупателя)
- status - состояние счета в виде целого числа. Статусы:
    - 0 - не оплачен
    - 1 - оплачен
    - -1 - отменен
- date - дата и время выставления счета в формате ISO 8601 в виде строки
- comment - коментарий к счету. Не является обязательным полем. Длина не более 250 символов.

#### Response:

##### Http status codes: 200

```
{
    "uuid": long,
    "cost": unsigned integer,
    "sellerPhone": long,
    "buyerPhone": long,
    "status": integer,
    "date": "string",
    "comment": string
}
```

В качестве ответа возвращается созданный счет.

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный номер отправителя или получателя
- 4 - Номер отправителя или получателя не найдены

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

###  PATCH payments/{uuid} - отвечает за отмену счета отправителем

#### Headers:

```
{
    "requesterPhone": long
}
```

- requesterPhone - номер телефона пользователя, который запросил отмену счета.
Номер проверяется на соответствие номеру отправителя, и, если отменить пытается
не отправитель, запрос на отмену отклоняется

#### Body:

```
{
    "status": -1,
}
```

#### Response:

##### Http status codes: 200

```
{
    "message": "success_message"
}
```

В качестве ответа возвращает сообщение об успехе

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный формат номера счета
- 4 - Не найден счет с таким номером
- 1 - Нет прав на отмену счета

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

###  PATCH payments/{uuid} - отвечает за оплату счета получателем

#### Headers:

```
{
    "requesterPhone": long
}
```

- requesterPhone - номер телефона пользователя, который запросил оплату счета.
  Номер проверяется на соответствие номеру получателя, и, если оплатить пытается
  не получатель, запрос на оплату отклоняется

#### Body:

```
{
    "status": 1,
}
```

#### Response:

##### Http status codes: 200

```
{
    "message": "success_message"
}
```

В качестве ответа возвращает сообщение об успехе

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный формат номера счета
- 4 - Не найден счет с таким номером
- 1 - Нет прав на оплату счета

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### GET /payments?sellerPhone={sellerPhone} - отвечает за получение выставленных счетов

#### URL параметры:
- status - фильтрация по статусу счета
#### Example:
GET /payments?sellerPhone=78005553535&status=0 - 
получение неоплаченных счетов

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
    "uuid": long,
    "cost": unsigned integer,
    "sellerPhone": long,
    "buyerPhone": long,
    "status": integer,
    "date": "string",
    "comment": string
},
{
    "uuid": long,
    "cost": unsigned integer,
    "sellerPhone": long,
    "buyerPhone": long,
    "status": integer,
    "date": "string",
    "comment": string
}
]
```

В качестве ответа возвращает коллекцию счетов

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный статус или номер телефона отправителя
- 4 - Не найден пользователь с таким номером или счета с таким статусом

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### GET /payments?buyerPhone={buyerPhone} - отвечает за получение счетов на оплату

#### URL параметры:
- status - фильтрация по статусу счета
#### Example:
GET /payments?sellerPhone=78005553535&status=0 -
получение неоплаченных счетов

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
    "uuid": long,
    "cost": unsigned integer,
    "sellerPhone": long,
    "buyerPhone": long,
    "status": integer,
    "date": "string",
    "comment": string
},
{
    "uuid": long,
    "cost": unsigned integer,
    "sellerPhone": long,
    "buyerPhone": long,
    "status": integer,
    "date": "string",
    "comment": string
}
]
```

В качестве ответа возвращает коллекцию счетов

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный статус или номер телефона получателя
- 4 - Не найден пользователь с таким номером или счета с таким статусом

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

# Endpoint remittances

### POST /remittances/forUsers - отвечает за создание перевода пользователю

#### Body:

```
{
    "date": "string",
    "sum": integer,
    "phone": long,
    "walletId": long,
    "id": int
}
```

- date - дата перевода в стандарте ISO 8601 в виде строки
- sum - сумма перевода. Не может быть больше баланса кошелька
- phone/walletId - номер телефона/кошелька адресата перевода. Должно быть заполнено
одно поле из двух
- id - уникальный ID перевода

#### Response:

##### Http status codes: 200

```
{
    "date": "string",
    "sum": integer,
    "phone": long,
    "walletId": long,
    "id": int
}
```

В качестве ответа возвращается созданный перевод.

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный номер получателя
- 4 - Номер получателя не найден
- 1 - Сумма превышает баланс кошелька

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### POST /remittances/forService - отвечает за создание перевода в качестве оплаты услуги

#### Body:

```
{
    "date": "string",
    "sum": integer,
    "serviceId": long,
    "id": int
}
```

- date - дата перевода в стандарте ISO 8601 в виде строки
- sum - сумма перевода. Не может быть больше баланса кошелька
- serviceId - номер услуги для перевода.
- id - уникальный ID перевода

#### Response:

##### Http status codes: 200

```
{
    "date": "string",
    "sum": integer,
    "serviceId": long,
    "id": int
}
```

В качестве ответа возвращается созданный перевод.

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный номер услуги
- 4 - Номер услуги не найден
- 1 - Сумма превышает баланс кошелька

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### POST /remittances/byUsers - отвечает за создание полученного перевода

#### Body:

```
{
    "date": "string",
    "sum": integer,
    "phone": long,
    "id": int
}
```

- date - дата перевода в стандарте ISO 8601 в виде строки
- sum - сумма перевода. Не может быть больше баланса кошелька
- phone - номер телефона отправителя перевода.
- id - уникальный ID перевода

#### Response:

##### Http status codes: 200

```
{
    "date": "string",
    "sum": integer,
    "phone": long,
    "id": int
}
```

В качестве ответа возвращается созданный перевод.

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный номер отправителя
- 4 - Номер отправителя не найден

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### GET /remittances - отвечает за получение истории переводов

#### Дополнительные endpoints:

/forUsers - только отправленные пользователю
/byUsers - только полученные от пользователя
/forService - только для оплаты услуги

#### URL параметры:

phone - фильтрация по номеру телефона получателя

#### Example:
GET /remittances/forUsers?phone=78005553535 -
получение переводов пользователям с определенным номером

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
    "date": "string",
    "sum": integer,
    "phone": long,
    "id": int
},
{
    "date": "string",
    "sum": integer,
    "phone": long,
    "id": int
}
]
```

В качестве ответа возвращает коллекцию переводов

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный номер получателя
- 4 - Не найден пользователь с таким номером

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера

### GET /remittances&id={id} - отвечает за получение информации о переводе

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
    "date": "string",
    "sum": integer,
    "phone": long,
    "id": int
}
```

В качестве ответа возвращает информацию о переводе

##### Http status codes: 400

```
{
    "code": integer,
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке и код ошибки

###### Возможные коды ошибки http status code 400:
- 2 - Некорректный id
- 4 - Не найден перевод с таким id

##### Http status codes: 500

```
{
    "code": integer,
    "message": "error_message"
}
```

###### Возможные коды ошибки http status code 500:
- 4 - превышено время ожидания от сервера