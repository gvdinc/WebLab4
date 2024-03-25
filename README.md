## Данная часть лабораторной работы была полностью выплнена [Зениным Мироном](https://github.com/GreatMimperator). Форк подтверждён.

﻿## Создание таблиц:

```sql
CREATE TABLE shots (
    id SERIAL PRIMARY KEY,
    area_id VARCHAR(20) NOT NULL,
    owner_login VARCHAR(20) NOT NULL,
    x_coordinate DOUBLE PRECISION NOT NULL,
    y_coordinate DOUBLE PRECISION NOT NULL,
    scope DOUBLE PRECISION NOT NULL,
    hit BOOLEAN NOT NULL,
    datetime TIMESTAMP NOT NULL,
    processing_time_nano BIGINT NOT NULL,
);
```

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL
);
```

# Какой апи?

- /login - возвращает результат входа
- /register - возвращает результат регистрации
- /shot - возвращает результат выстрела, записывая его данные в базу
- /shots - возвращает все выстрелы как json массив

# Перед подробным описанием опишу общие моменты, касающиеся ошибок:

Об ошибке можно догадаться по коду ответа, так что флага error в json не будет.
Однако же будет описание ошибки:

Ключ "error\_type", значения пока: 
- "wrong\_json\_structure" при ошибках парсинга строки-json'а
- "absent\_key", к нему добавляется "absent\_keys" со списком отсутствующих ключей 
(возможно не всех, но всех на определенном этапе,
например, если начал обработку user-а, и нет хэш-пароля и логина, 
то дальше обработка не пойдет и будет отправлена информация только о них)

Код ошибки в обоих случаях будет 422 (Unprocessable Entity)

Но не все ошибки должны помечаться определенным кодом ответа, ведь некоторые "отказы" могут быть в рамках бизнес-логики, не только связанные с валидацией приходящих данных.

# Данные отправляемые с каждым запросом

Т.к. сессия реализуется на стороне клиента, а REST стейтлесс протокол, с каждым запросом (кстати с логином то же, но логика ответа другая) будет отправляться логин и хэш пароля.
Клиент должен понимать, что он обязан обрабатывать и связанные с логином ошибки:

### Отправляется:

- "login", строковое значение длиной от 5 до 20 ([a-zA-Z_])
- "password" длиной от 8 до 30 ([a-fA-F0-9_])

### Ответ:

- "login\_state", если не удалось войти: значения "wrong\_login", "wrong\_password", "wrong_character" или "wrong_length"
- Если "wrong_character", то добавляется массив "wrong_character_params" с возможными значениями "login" и "password"
- Если "wrong_length", то добавляется массив "wrong_length_params" с возможными значениями "login" и "password"

# Подробное описание:

## Название:

/login

### Отправляется:

Ничего нового

### Ответ:

- добавляется значение "logon" к возможным значениям "login\_state" - этот ключ будет всегда (если не будет критической ошибки, конечно)

## Название:

/register

### Отправляется:

Ничего нового

### Ответ:

- "register\_state", значения "registered", "duplicate\_login"

## Название:

/shot

### Отправляется:

- "x", Double
- "y", Double
- "R", Double
- "area_id", String 
(в данной версии API от 1 до 20 символов, доступны "Vadim" и "Miron")

### Ответ:

- "hit", ```true``` или ```false```
- "datetime", в формате ISO 8601 
- "processing\_time\_nano", в наносекундах
- "wrong\_type", массив с ["x", "y", "R"] - смотря что имеет неверный тип
- "wrong\_value", массив с ["x", "y", "R", "area_id"] - смотря что имеет неверное значение

## Название:

/shots

### Отправляется:

Ничего нового

### Ответ:

Массив по ключу "shots" со словарями с ключами:
- "x", Double
- "y", Double
- "R", Double
- "hit", ```true``` или ```false```
- "datetime", в формате ISO 8601 
- "processing\_time", в наносекундах
