# Парсер файлов

## Описание
REST-API для целей разбора файла на составные части.  \
API принимает на вход файл (ограничение в 1 Мб). \
Файл является текстовым с возможными переводами строк в стиле CRLF, CR и/или LF.  \
В начале каждой строки может быть признак начала раздела или подраздела - символ '#'.  \
Количество символов '#' в начале строки указывает на уровень вложенности раздела.  \
Результат разобранный файл в виде структура разделов, посредством которой можно будет осуществлять навигацию по разделам, + набора строк. \

## Endpoints
`/api/v1/files/parser` - этот endpoint который принимает текстовый файл на вход. HTTP-заголовок `Content-Type` должен иметь значение `multipart/form-data`.
Размер файла не должен привешать 1 Мб. Возвращает ответ в виде JSON объекта. \
### Структура ответа
Ответ в формате JSON. Поле `code` сигнализирует о коде ответа. В поле `data` содержится ответ в виде HTML. Сначала идет структура разделов, а затем сам текст. \
**Пример ответа:** \
```
{
    "code": "SUCCESS",
    "data": "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>Title</title>\n    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n    <link href=\"https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap\" rel=\"stylesheet\">\n    <style>\n        * {\n            font-family: 'Roboto', sans-serif;\n            color: #333;\n            font-size: 15px;\n            font-weight: 400;\n        }\n        a {\n            font-family: 'Roboto', sans-serif;\n            color: #333;\n            font-size: 15px;\n            font-style: normal;\n            font-weight: 400;\n            text-decoration: none;\n        }\n        a:visited, a:focus, a:hover {\n            color: #333;\n        }\n        a.section_link {\n            font-style: italic;\n        }\n        a.section_link:hover {\n            text-decoration: underline;\n        }\n        h1 {\n            font-family: 'Roboto', sans-serif;\n            color: #333;\n            font-size: 22px;\n            font-weight: 400;\n\n            margin: 10px 0;\n        }\n    </style>\n</head>\n<body>\n<h1>Содержание:</h1>\n<div><a class=\"section_link\" href=\"#2_1905325072\">- Chapter one</a></div><div><a class=\"section_link\" href=\"#6_788082205\">-- Jack's characteristics</a></div><div><a class=\"section_link\" href=\"#8_1126781824\">--- height: 71 inch</a></div><div><a class=\"section_link\" href=\"#10_860969632\">--- weight: 190 pounds</a></div><div><a class=\"section_link\" href=\"#12_1905319978\">- Chapter two</a></div><div><a class=\"section_link\" href=\"#18_1646119242\">-- Jack's patents</a></div><div><a class=\"section_link\" href=\"#20_1011739441\">--- mosquito net</a></div><div><a class=\"section_link\" href=\"#22_1313993870\">--- x-ray</a></div><div><a class=\"section_link\" href=\"#24_285856401\">--- internal combustion engine</a></div>\n<h1>Текст:</h1>\n<div>GREATEST MAN IN ALIVE</div>\n<div></div>\n<div><a name=\"2_1905325072\">Chapter one</a></div>\n<div></div>\n<div>this story about awesome dude that call name is Jack</div>\n<div></div>\n<div><a name=\"6_788082205\">Jack's characteristics</a></div>\n<div></div>\n<div><a name=\"8_1126781824\">height: 71 inch</a></div>\n<div></div>\n<div><a name=\"10_860969632\">weight: 190 pounds</a></div>\n<div></div>\n<div><a name=\"12_1905319978\">Chapter two</a></div>\n<div></div>\n<div>Jack was most famous man in alive</div>\n<div></div>\n<div>his fame was greater than his popularity</div>\n<div></div>\n<div><a name=\"18_1646119242\">Jack's patents</a></div>\n<div></div>\n<div><a name=\"20_1011739441\">mosquito net</a></div>\n<div></div>\n<div><a name=\"22_1313993870\">x-ray</a></div>\n<div></div>\n<div><a name=\"24_285856401\">internal combustion engine</a></div>\n</body>\n</html>\n"
}
```

## Коды ответа
~ **SUCCESS**: ответ является успешным и содержит в себе данные \
~ **INTERNAL_SERVER_ERROR**: на сервере произошла ошибка и стоит попробовать сделать запрос чуть позже \
~ **INCORRECT_REQUEST_TYPE**: который вы передали на endpoint `/api/v1/files/parser` неправильного типа \
~ **INCORRECT_ENCODING**: кодировка файла некорректная (используйте UTF-8) \
~ **HTTP_REQUEST_METHOD_NOT_SUPPORTED**: HTTP-метод, который вы используете для запроса не поддерживается сервером \
~ **HTTP_MEDIA_TYPE_NOT_SUPPORTED**: тип контента в POSTs, PUTs, PATCHes запросах не поддерживается endpoint'ом \
~ **HTTP_MEDIA_TYPE_NOT_ACCEPTABLE**: заголовок Accept у клиента, не поддерживает тот тип, который хочет вернуть сервер \
~ **MISSING_PATH_VARIABLE**: клиент в URI пропустил необходимую переменную \
~ **MISSING_SERVLET_REQUEST_PARAMETER**: что клиент пропустил необходимый параметр \
~ **MISSING_SERVLET_REQUEST_PART**: сервер не нашел часть 'multipart/form-data' запроса с указанным именем \
~ **MISSING_SERVLET_BINDING_EXCEPTION**: сервер не может привязать те параметры, которые передал ему клиент к контроллеру \
~ **METHOD_ARGUMENT_NOT_VALID**: переданный параметр не валидный \
~ **NO_HANDLER_FOUND_EXCEPTION**: не нашлось подходящего endpoint'а \
~ **ASYNC_REQUEST_TIMEOUT_EXCEPTION**: время ожидания ассинхронного запроса подошло к концу \
~ **ERROR_RESPONSE_EXCEPTION**: произошла ошибка при обработке запроса \
~ **CONVERSION_NOT_SUPPORTED**: не удалось преобразовать запрос или ответ в нужный формат \
~ **TYPE_MISMATCH**: при получения параметров и привязки их к внутренним структурам данных возникла ошибка соответствии типов, передайте корректный тип данных \
~ **HTTP_MESSAGE_NOT_READABLE**: сервер не может прочитать или разобрать тело HTTP-запроса в нужный объект или тип данных \
~ **HTTP_MESSAGE_NOT_WRITABLE**: серверу не удалось записать ответ в тело HTTP-запроса \
~ **SIZE_LIMIT_EXCEEDED_EXCEPTION**: превышен лимит размера максимально загружаемого файла на сервер \

## Структура ошибок
Структура ошибок разная, но она всегда имеет два обязательных поля `message` и `code`. В поле `code` находится фиксированное значение, которое очень долго не будет меняться, поэтому при обработке ошибок от сервера стоит смотреть на это поле, а не на `message`, которое хранит в себе сообщение для пользователя. \
\
**Пример ошибки:**
```
{
    "message": "The part of the \"multipart/form-data\" query identified by name could not be found",
    "code": "MISSING_SERVLET_REQUEST_PART",
    "statusCode": 400,
    "requestPartName": "file"
}
```
