# Flaze

Flaze - это API для создания и чтения статей. Этот проект предоставляет простой и удобный способ управления статьями через RESTful интерфейс.

## Использование

### Создание статьи

Чтобы создать новую статью, выполните HTTP POST запрос на `/api/v1/articles/add?userId=идентифекатор пользователя` с JSON телом статьи. Пример запроса:

```http
POST /api/v1/articles/add?userId=1
Content-Type: application/json

{
  "title": "Python",
  "description": "Высокоуровневый язык программирования",
  "text": "Python — высокоуровневый язык программирования общего назначения с динамической 
  строгой типизацией и автоматическим управлением памятью, 
  ориентированный на повышение производительности 
  разработчика, читаемости кода и его качества, 
  а также на обеспечение переносимости написанных на нём программ."
}
```

### Чтение статьи

Чтобы прочитать статью, выполните HTTP GET запрос на `/api/v1/articles/{id}`, где `{id}` - это идентификатор статьи. Пример запроса:

```http
GET /articles/1
```

### Документация Swagger

Вы можете ознакомиться с документацией Swagger для этого API, перейдя по следующему URL:

```
http://localhost:8080/swagger-ui/index.html#/
```

## Установка и запуск

1. Клонируйте репозиторий:

```bash
git clone https://github.com/yourusername/flaze.git
```

2. Перейдите в директорию проекта:

```bash
cd flaze
```

3. Установите зависимости:

```bash
npm install
```

4. Запустите проект:

```bash
npm start
```

## Лицензия

Этот проект лицензируется в соответствии с [Лицензией MIT](LICENSE).