# Электронный кошелек Картошка
# REST API Documentation

В качестве ответа возвращается application/json

В качестве тела запроса используется application/json

# Endpoints:
users, users/sessions, users/wallets, users/payment(s), users/remittance(s)

Пользователи, сессии, кошельки, счета на оплату и денежные переводы соответственно

# Endpoint users

### POST /users - отвечает за создание пользователя

#### Body:

```
{
    "phone": long,
    "password": "string",
    "firstName": "string",
    "lastName": "string",
    "patronymicName": "string",
    "email": "string",
    "birthday": "string"
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

- birthday - дата рождения пользователя в формате ISO 8601 в качестве строки.

- password - пароль пользователя. Особые требования к паролю:

  - от 8 до 64 символов
  - только латинские символы, цифры, знаки ?!
  - обязательно наличие минимум 1 буквы верхнего и нижнего регистра, цифры и знака

#### Response:

##### Http status codes: 200

```
{
    "phone": long,
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "email": "string",
    "birthday": "string",
    "registrationDate": "string",
    "lastUpdateDate": "string"
}
```

В качестве ответа возвращаются данные пользователя, кроме пароля.

- registrationDate - дата регистрации
- lastUpdateDate - дата последнего изменения


##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### GET /users/{id} - отвечает за получение данных о пользователе

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
    "phone": long,
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "email": "string",
    "birthday": "string",
    "registrationDate": "string",
    "lastUpdateDate": "string"
}
```

В качестве ответа возвращается пользователь с запрошенным id.

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### PATCH /users - отвечает за изменение данных о пользователе

Разрешено менять только ФИО и дату рождения

#### Headers:

"X-Session-Token": "string"

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
ни одного параметра будет выброшена ошибка.
При изменении хотя бы одного поля записывается новая дата изменения пользователя.

#### Response:

##### Http status codes: 200

```
{
  "phone": long,
  "surname": "string",
  "name": "string",
  "patronymicName": "string",
  "email": "string",
  "birthday": "string",
  "registrationDate": "string",
  "lastUpdateDate": "string"
}
```

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

# Endpoint sessions

### POST /users/sessions - отвечает за создание сессии

#### Body:

```
{
    "email": "string",
    "password": "string"
}
```

- email - адрес электронной почты пользователя
- password - пароль пользователя

#### Response:

##### Http status codes: 200

```
{
    "expirationTime": "string",
    "value": "string",
    "active": boolean
}
```

В качестве ответа возвращается токен и время жизни токена.

- expirationTime - время истечения срока действия сессии
- value - токен
- active - флаг активности сесии

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### GET users/sessions/ - отвечает за получение информации об активных сессиях пользователя

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response: 

##### Http status codes: 200

```
[
{
    "expirationTime": "string",
    "value": "string",
    "active": boolean
}
]
```

В качестве ответа возвращает массив всех сессий пользователя

- expirationTime - время истечения срока действия сессии
- value - токен
- active - флаг активности сесии

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### GET users/sessions/current - отвечает за получение информации о текущей

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
    "expirationTime": "string",
    "value": "string",
    "active": boolean
}
```

В качестве ответа возвращает информацию о сессии

- expirationTime - время истечения срока действия сессии
- value - токен
- active - флаг активности сесии

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### DELETE /users/sessions - отвечает за закрытие сессии

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

Тело ответа отсутствует

В качестве ответа возвращает только http status code 200

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

# Endpoint wallets

### GET users/wallets - отвечает за получение информации о кошельке

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
    "walletId": long,
    "userId": long,
    "balance": long
}
```

В качестве ответа возвращает информацию о кошельке

- userId - ID владельца кошелька
- walletId - номер кошелька
- balance - баланс кошелька в д. е. Не может быть отрицательным. Начальное значение - 100

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

# Endpoint users/payments

### POST /payment - отвечает за создание счета на оплату

#### Headers:

"X-Session-Token": "string"

#### Body:

```
{
    "recipientPhone": long,
    "comment": "string",
    "cost": long
}
```
- recipientPhone - номер пользователя, которому выставляется счет
- comment - комментарий к счету на оплату. Является опциональным полем. Длина комментария не более 250 символов
- cost - сумма счета на оплату

#### Response:

##### Http status codes: 200

```
{
    "paymentId": "string",
    "cost": long,
    "ownerPhone": long,
    "recipientPhone": long,
    "comment": "stirng",
    "status": integer,
    "time": "string"
}
```

В качестве ответа возвращается созданный счет.

- status - состояние счета в виде целого числа. Статусы:
  - 0 - не оплачен
  - 1 - оплачен
  - -1 - отменен
- paymentId - uuid чета на оплату. Требуется для дальнейших операций со счетом на оплату.
- time - время создания счета на оплату
- ownerPhone - омер телефона владельца счета

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке

###  POST /users/payment/cancel/{id} - отвечает за отмену счета отправителем

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
  "paymentId": "string",
  "cost": long,
  "ownerPhone": long,
  "recipientPhone": long,
  "comment": "string",
  "status": integer,
  "time": "string"
}
```

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке

###  POST /users/payment/pay/{id} - отвечает за оплату счета получателем

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
  "paymentId": "string",
  "cost": long,
  "ownerPhone": long,
  "recipientPhone": long,
  "comment": "string",
  "status": integer,
  "time": "string"
}
```

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке

### GET /users/payments/one/{id} - отвечает за получение информации о счете по его id

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
  "paymentId": "string",
  "cost": long,
  "ownerPhone": long,
  "recipientPhone": long,
  "comment": "string",
  "status": integer,
  "time": "string"
}
```

В качестве ответа возвращает нформацию о счете на оплату

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке

### GET /users/payments/owner - отвечает за получение выставленных счетов на оплату

#### Url parameters:

- /{status} - фильтрация по статусу счета

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
  "paymentId": "string",
  "cost": long,
  "ownerPhone": long,
  "recipientPhone": long,
  "comment": "string",
  "status": integer,
  "time": "string"
}
]
```

В качестве ответа возвращает массив счетов на оплату

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке

### GET /users/payments/recipient - отвечает за получение полученных счетов на оплату

#### Url parameters:

- /{status} - фильтрация по статусу счета

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
  "paymentId": "string",
  "cost": long,
  "ownerPhone": long,
  "recipientPhone": long,
  "comment": "string",
  "status": integer,
  "time": "string"
}
]
```

В качестве ответа возвращает массив счетов на оплату

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращает сообщение об ошибке

# Endpoint remittances

### POST users/remittance - отвечает за создание перевода

#### Headers:

"X-Session-Token": "string"

#### Body:

```
{
    "recipientPhone": long,
    "recipientWallet": long,
    "cost": long,
}
```

- cost - сумма перевода. Не может быть больше баланса кошелька
- recipientPhone/Wallet - номер телефона/кошелька адресата перевода. Должно быть заполнено хотя бы
одно поле из двух

#### Response:

##### Http status codes: 200

```
{
    "remittanceId": long,
    "owner": long,
    "recipientPhone": long,
    "recipientWallet": long,
    "time": "string",
    "cost": long
}
```

В качестве ответа возвращается созданный перевод.

- remittanceId - ID перевода
- owner - номер телефона отправителя
- recipientPhone - номер телефона получателя
- recipientWallet - номер кошелька получателя
- time - дата и время перевода
- cost - сумма перевода

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### GET /remittance/{id} - отвечает за получение информации о конкретном переводе по его ID

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
{
    "remittanceId": long,
    "owner": long,
    "recipientPhone": long,
    "recipientWallet": long,
    "time": "string",
    "cost": long
}
```

В качестве ответа возвращает информацию о переводе

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### GET /remittances/owner - отвечает за получение информации о переводах, где пользователь является отправителем

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
    "remittanceId": long,
    "owner": long,
    "recipientPhone": long,
    "recipientWallet": long,
    "time": "string",
    "cost": long
}
]
```

В качестве ответа возвращает массив переводов, где пользователь является отправителем

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### GET /remittances/recipient - отвечает за получение информации о переводах, где пользователь является получателем

#### Headers:

"X-Session-Token": "string"

#### Body:

Тело запроса отсутствует

#### Response:

##### Http status codes: 200

```
[
{
    "remittanceId": long,
    "owner": long,
    "recipientPhone": long,
    "recipientWallet": long,
    "time": "string",
    "cost": long
}
]
```

В качестве ответа возвращает массив переводов, где пользователь является отправителем

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке