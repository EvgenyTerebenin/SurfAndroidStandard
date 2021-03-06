[Главная](../main.md)

[TOC]

# Работа с Сервером

Работа с сервером обычно происходит с помощью библиотек retrofit2, okhttp, gson, rxjava.

Для работы с сетью предусмотрены модули:
 * [network](../../network/README.md)
 * [Gson-конвертер](../../converter-gson/README.md)

При работе с сервером возникает необхдимость добавлять те или иные параметры
в запрос. Для этих целей хорошо подходит сущности типа `Interceptor`. Такие
сущности передаются в билдер OkHttp-клента.

Также удобно [логгировать][log] ответы сервера, а также ошибки парсинга на удаленный сервер
с целью быстрого нахождения ошибок. **Не следует логгировать пользовательские
данные**.

### Обработка ошибок

Обработка большинства ошибок происходит [на уровне Presenter'а][handle_errors_on_presenter].

Если на сервере существует механизм ошибок, когда не с 200-м кодом
приходит еще и тело ошибки, то такие сообщения следует приводить к специфичному
исключению. Эти операции следует производить на уровне [`CallAdapter`'а][call]

Ошибки парсинга Json (`JsonSyntaxException`) следует конвертировать в некоторую
кастомную ошибку. Этим занимается [`Converter`][gson].

###### Открытие экранов по спец ошибкам

В случае если возникает необходимость открыть экран прямо из слоя Interactor,
например, когда у пользователя устарел токен и необходимо перекинуть его на экран
авторизации, необходимо использовать глобальный навигатор, поставляемый модулем
[core-ui](../../core-ui/README.md).
Это необходимо,  чтобы не тащить данную логику на ui.

### Маппинг ответов сервера

Хорошей практикой является абстрагирование доменного слоя на клиенте от
серверной модели данных (доменной модели).
*Примеры:*
* сервер(плохой) на несколько запросов отдает информацию о пользователе,
но в разных форматах.

* на сервере структура объектов усложнена, присутствует большая вложенность.

* маппинг вложенных объектов, типов, например `String` -> `Enum`.

* у доменной сущности есть дополнительные поля, которых нет на сервере,
тогда нам необходимо их проинициализировать при маппинге. Здесь как раз
играет роль то, что доменная область описывает нужды приложения. Допустим
есть сущность в виде медиа-элемента, который мы можем загрузить с устройства.
В домменой области, он может иметь доп.поля, например состояние загруженности.
Этих полей не будет в серверном слое.

В целом, маппинг необходим для разделения ответственности. Одни модели
предназначены для парсинга, другие - доменные - для внутреннего использования.

Доменная область на клиенте(в приложении) должна отвечать в первую очередь
бизнес-логике приложения.

Исходя из этих соображений, возникает необходимость преобразовывать ответы сервера
в модели на клиенте.

В наших приложениях приняты следующие **договоренности** - классы,
которые используются:

- для парсинга ответа должны быть с суффиксом `Response`

- для формирования запроса должны быть с суффиксом `Request`

- для парсинга вложенных Json объектов должны быть с суффиксом `Obj`

Маппинг серверных ответов происходит в [репозитории][interactor] `Repository` с помощью
[специальных методов, утилит или
операторов(так как используем Rx)][mapping].

### Тесты API

**todo**
Следует покрывать тестами все методы сервера, чтобы понимать состояние сервера.


[log]: ../common/logging.md
[gson]: ../../converter-gson/README.md
[call]: ../../network/README.md
[simple_cache]: ../../network/docs/usage.md
[etag]: ../../network/docs/etag.md
[hybrid]: ../../network/docs/hybrid.md
[handle_errors_on_presenter]: ../ui/presenter.md
[file_cache]: ../../filestorage/README.md
[mapping]: ../../network/docs/usage.md
[interactor]: interactor.md
