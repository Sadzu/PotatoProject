# Электронный кошелек Картошка REST API

В качестве ответа возвращается application/json

В качестве тела запроса используется application/json

# Endpoints:
users, sessions, wallets, payments, remittances

Пользователь, сессия, кошелек, счета на оплату и денежные переводы соответственно

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
    "password": "string"
}
```

phone - номер телефона пользователя, также используется как уникальный id, записывается без +7/8

surname, name, patronymicName - ФИО пользователя, отчество не является обязательным полем

email - адрес электронной почты пользователя

birthDate - дата рождения пользователя в формате ISO 8601

password - пароль пользователя

#### Example body:

```
{
    "phone": 8005553535,
    "surname": "Иванов",
    "name": "Иван",
    "patronymicName": "Иванович",
    "email": "example@example.com",
    "birthDate": "2012-12-12",
    "password": "password"
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
}
```

В качестве ответа возвращаются данные пользователя, кроме пароля 

##### Http status codes: 400, 500:

```
{
    "message": "error_message"
}
```

В качестве ответа возвращается сообщение об ошибке

### GET /users - отвечает за получение данных о пользователе

#### Body:
Тело запроса отсутствует

#### Response: 

##### Http status codes: 200
```
[
{
    "phone": long,
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "email": "string",
    "birthDate": "string"
},
{
    "phone": long,
    "surname": "string",
    "name": "string",
    "patronymicName": "string",
    "email": "string",
    "birthDate": "string"
}
]
```

В качестве ответа возвращается массив пользователей

##### Http status codes: 400, 500

```
{
    "message": "error_message"
}
```
В качестве ответа возвращается сообщение об ошибке.

#### URL параметры:

phone - номер телефона

#### Example:

GET /users?phone=8005553535

Response: 

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