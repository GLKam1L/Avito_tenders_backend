# Выполнение тестового задания Авито

## Условие задачи

В ходе задания было необходимо реализовать сервис, который позволит бизнесу создать тендер на оказание каких-либо услуг.
А
пользователи/другие бизнесы будут предлагать свои выгодные условия для получения данного тендера.

### Используемый стек

- Java
- Spring, Lombok
- Postgres

## Выполнение задания

### Бизнес-логика

Были соблюдены все условия бизнес-логики, указанные в задании.
#### Тендеры

Тендеры могут создавать только пользователи от имени своей организации:

Доступные действия с тендером:

- **Создание**:- Тендер будет создан. - Доступен только ответственным за организацию. - Статус: `CREATED`.

- **Публикация**:- Тендер становится доступен всем пользователям. - Статус: `PUBLISHED`.

- **Закрытие**:- Тендер больше не доступен пользователям, кроме ответственных за организацию. - Статус: `CLOSED`.

- **Редактирование**:- Изменяются характеристики тендера. - Увеличивается версия.

#### Предложение

Предложения могут создавать пользователи либо от имени своей организации.

Предложение связано только с одним тендером.

Доступные действия с предложениями:

- **Создание**:- Предложение будет создано. - Доступно только автору и ответственным за организацию. - Статус:
  `CREATED`.

- **Публикация**:- Предложение становится доступно ответственным за организацию и автору. - Статус: `PUBLISHED`.

- **Отмена**:- Виден только автору и ответственным за организацию. - Статус: `CANCELED`.

- **Редактирование**:- Изменяются характеристики предложения. - Увеличивается версия.

- **Согласование/отклонение**:- Доступно только ответственным за организацию, связанной с тендером. - Решение может быть
  принято любым ответственным. - При согласовании одного предложения, тендер автоматически закрывается.

### Дополнительные требования

1. Расширенный процесс согласования:

    - Если есть хотя бы одно решение reject, предложение отклоняется.- Для согласования предложения нужно получить
      решения больше или равно кворуму.- Кворум = min(
      3, количество
      ответственных
      за
      организацию).

3. Просмотр отзывов на прошлые предложения:

    - Ответственный за организацию может просмотреть отзывы на предложения автора, который создал предложение для его
      тендера.

5. Оставление отзывов на предложение:

    - Ответственный за организацию может оставить отзыв на предложение.

7. Добавить возможность отката по версии (Тендер и Предложение):

    - После отката, считается новой правкой с увеличением версии.

9. Описание конфигурации линтера.

### Дополнительный функционал

Для удобства тестирования созданы классы для добавления в таблицы новых пользователей и организаций, а также для
просмотра уже существущих. Создание
выполняется с помощью запросов:
#### /api/employees/new, /api/organizations/new

Пример:
```yaml
GET /api/employees/new

Request Body:
  
  {
    "username": "glkamil",
    "firstName": "Камиль",
    "lastName": "Исхаков"
  }

Response:

  200 OK

Body:
  
  {
    "id": 1,
    "username": "glkamil",
    "firstName": "Камиль",
    "lastName": "Исхаков"
  }
```

## Тестирование

### 1. Проверка доступности сервера

- **Эндпоинт:** GET /ping
- **Цель:** Убедиться, что сервер готов обрабатывать запросы.
- **Ожидаемый результат:** Статус код 200 и текст "ok".

```yaml
GET /api/ping

Response:

    200 OK

  Body: ok
```

### 2. Тестирование функциональности тендеров

#### Получение списка тендеров

- **Эндпоинт:** GET /tenders
- **Описание:** Возвращает список тендеров с возможностью фильтрации по типу услуг.
- **Ожидаемый результат:** Статус код 200 и корректный список тендеров.

```yaml
GET /api/tenders

Response:

    200 OK

  Body: [ { ... }, { ... }, ... ]
```

#### Создание нового тендера

- **Эндпоинт:** POST /tenders/new
- **Описание:** Создает новый тендер с заданными параметрами.
- **Ожидаемый результат:** Статус код 200 и данные созданного тендера.

```yaml
POST /api/tenders/new

Request Body:

    {

    "name": "Тендер 1",

    "description": "Описание тендера",

    "serviceType": "Construction",

    "status": "Open",

    "organizationId": 1,

    "creatorUsername": "user1"

                           }

Response:

    200 OK

  Body:
   
    {
    "id": 1,
    "name": "Тендер 1",
    "description": "Описание тендера",
                         ...
                       }
```

#### Получение тендеров пользователя

- **Эндпоинт:** GET /tenders/my
- **Описание:** Возвращает список тендеров текущего пользователя.
- **Ожидаемый результат:** Статус код 200 и список тендеров пользователя.

```yaml
GET /api/tenders/my?username=user1

Response:

    200 OK

  Body: [ { ... }, { ... }, ... ]  
```

#### Редактирование тендера

- **Эндпоинт:** PATCH /tenders/{tenderId}/edit
- **Описание:** Изменение параметров существующего тендера.
- **Ожидаемый результат:** Статус код 200 и обновленные данные тендера.

```yaml
PATCH /api/tenders/1/edit

Request Body:

    {

    "name": "Обновленный Тендер 1",

    "description": "Обновленное описание"

                       }

Response:

    200 OK

  Body:
    {
    "id": 1,
    "name": "Обновленный Тендер 1",
    "description": "Обновленное описание",
                         ...
                       }  
```

#### Откат версии тендера

- **Эндпоинт:** PUT /tenders/{tenderId}/rollback/{version}
- **Описание:** Откатить параметры тендера к указанной версии.
- **Ожидаемый результат:** Статус код 200 и данные тендера на указанной версии.

```yaml
PUT /api/tenders/1/rollback/2

Response:

    200 OK

  Body:
    {
    "id": 1,
    "name": "Тендер 1 версия 2",
                  ...
                }
```

### 3. Тестирование функциональности предложений

#### Создание нового предложения

- **Эндпоинт:** POST /bids/new
- **Описание:** Создает новое предложение для существующего тендера.
- **Ожидаемый результат:** Статус код 200 и данные созданного предложения.

```yaml
POST /api/bids/new

Request Body:

    {

    "name": "Предложение 1",

    "description": "Описание предложения",

    "status": "Submitted",

    "tenderId": 1,

    "organizationId": 1,

    "creatorUsername": "user1"

                           }

Response:

    200 OK

  Body:
    {
    "id": 1,
    "name": "Предложение 1",
    "description": "Описание предложения",
                         ...
                       }
```

#### Получение списка предложений пользователя

- **Эндпоинт:** GET /bids/my
- **Описание:** Возвращает список предложений текущего пользователя.
- **Ожидаемый результат:** Статус код 200 и список предложений пользователя.

```yaml
GET /api/bids/my?username=user1

Response:

    200 OK

  Body: [ { ... }, { ... }, ... ]
            ```
           
  #### Получение списка предложений для тендера
          - **Эндпоинт:** GET /bids/{tenderId}/list
          - **Описание:** Возвращает предложения, связанные с указанным тендером.
          - **Ожидаемый результат:** Статус код 200 и список предложений для тендера.

          ```yaml
          GET /api/bids/1/list

Response:

    200 OK

  Body: [ { ... }, { ... }, ... ]
            ```
           
  #### Редактирование предложения
          - **Эндпоинт:** PATCH /bids/{bidId}/edit
          - **Описание:** Редактирование существующего предложения.
          - **Ожидаемый результат:** Статус код 200 и обновленные данные предложения.

          ```yaml
          PATCH /api/bids/1/edit

Request Body:

    {

    "name": "Обновленное Предложение 1",

    "description": "Обновленное описание"

                       }

Response:

    200 OK

  Body:
    {
    "id": 1,
    "name": "Обновленное Предложение 1",
    "description": "Обновленное описание",
                         ...,
                       }
```

#### Откат версии предложения

- **Эндпоинт:** PUT /bids/{bidId}/rollback/{version}
- **Описание:** Откатить параметры предложения к указанной версии.
- **Ожидаемый результат:** Статус код 200 и данные предложения на указанной версии.

```yaml
PUT /api/bids/1/rollback/2

Response:

    200 OK

  Body:
    {
    "id": 1,
    "name": "Предложение 1 версия 2",
                  ...
                }
```

### 4. Тестирование функциональности отзывов

#### Просмотр отзывов на прошлые предложения

- **Эндпоинт:** GET /bids/{tenderId}/reviews
- **Описание:** Ответственный за организацию может посмотреть прошлые отзывы на предложения автора, который создал
  предложение для его тендера.
- **Ожидаемый результат:** Статус код 200 и список отзывов на предложения указанного автора.

```yaml
GET /api/bids/1/reviews?authorUsername=user2&organizationId=1

Response:

    200 OK

  Body: [ { ... }, { ... }, ... ]
```