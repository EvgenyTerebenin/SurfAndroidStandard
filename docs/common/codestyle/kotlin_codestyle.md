[Главная](../../main.md)

[TOC]

# Kotlin CodeStyle
**В разработке**

## Основные положения

Основано на [Coding Conventions][conv] и [Android Style Guide][style]

Перед *Pull Request* **ОБЯЗАТЕЛЬНО** делаем форматирование кода по `Ctrl+Alt+L`

## Правила оформления комментариев

см. [Android Code Style][surf]

Оформляются по правилам KDoc. см [Coding Conventions][conv], [Android Style Guide][style]

## Именование сущностей
см. [Coding Conventions][conv], [Android Code Style][surf]

**Указаны отличия от код-стайла из вики Surf**

### Имена констант в xml:

* screen_semantic_postfix, где

    * screen - имя экрана, на котором используется константа,
может отсутствовать, если используется на многих экранах (без префикса activity/fragment)

    * semantic - смысл

    * postfix - назначение:
        * _text строковая константа используется в TextView,
        * _btn строковая константа используется для Button
        * _message, _error_message - используется в Toast / Snack
        * _hint - подсказка в EditText
        * _padding, _margin - паддинг и марджин соответственно
        * _width, _height - ширина и высота соответственно

### Именование свойств

Используется `camelCase`. Тип свойства указывается **явно**.

Если используются свойства с функциональном типом, то делаем постфикс `Lambda`
```
val someBeatifulLambda: (Int) -> Int = { it*2 }
```

Если такое свойство используется как *callback*, то вместо `Lambda` ->  `Callback`.

### Именование методов

Используется `camelCase`. Правила оформления смотри [Android Code Style][surf].

### Использование спец символов
см. [Special Characters from Android Style Guide](https://android.github.io/kotlin-guides/style.html#special-characters)

## Структура класса

* companion object
* приватные поля-ключи(константы)
* приватные val / var свойства
* публичные val/ var свойства
* публичные интерфейсы
* методы
* вложенные классы

#### Расположение методов в классе

1 вариант:
* override методы
* публичные методы
* приватные методы

2 вариант: см.  [Android Code Style][surf]

## Организация файлов и пакетов

Выносить extension-методы в отдельные, логически связанные файлы,
если они не используются только в одном месте. Файл именуется с
постфиксом Extensions, например `UiExtensions`, `InputExtensions` …

Публичные глобальные константы хранить как val свойства в отдельном файле,
на глобальном уровне.

Утилитные функции удобно хранить либо в виде *глобальных функций*, либо
оборачивать в `object`.

## Форматирование

### Пустые/ не пустые блоки кода
см [Non-empty/Empty Blocks](https://android.github.io/kotlin-guides/style.html#non-empty-blocks)

### Методы
см. [Function formatting](https://kotlinlang.org/docs/reference/coding-conventions.html#function-formatting),
[Вызов методов](https://kotlinlang.org/docs/reference/coding-conventions.html#method-call-formatting)

*Замечание*: форматирование аргументов метода, когда не помещается
в одну строку. Либо в соответствии с оф. код-стайлом, либо так:

1)
```
override fun onPageScrolled(position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int) {
   //some actions
}
```
2)
```
override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
) {
   //some actions
}
```


Если выражение однострочного метода не помещается в одну строку, то переносить
его на следующую.
```
fun authByEmail(email: String, password: String): Observable<User> =
    authApi.auth(AuthRequest(email = email, password = password))
       .transform()
```

### Свойства
см. [Форматирование свойств](https://kotlinlang.org/docs/reference/coding-conventions.html#property-formatting)

### Заголовок классов
см. [Форматирование заголовка класса](https://kotlinlang.org/docs/reference/coding-conventions.html#class-header-formatting)

*Возможен другой вариант*:
```
class SocialNetworksInteractor @Inject constructor(private val vkRepository: VkRepository,
                                                  private val fbRepository: FbRepository,
                                                  private val analyticsService: AnalyticsService,
                                                  private val activityProvider: ActivityProvider
) : OAuthCallback, ActivityResultDelegate {
```

### Форматирование управляющих конструкций
см. [Форматирование управляющих конструкций](https://kotlinlang.org/docs/reference/coding-conventions.html#formatting-control-flow-statements)

todo

#### Boolean?.
При использовании `nullable Boolean` возможны два варианта написания if конструкции:
```
if (b ?: false) ...
```
```
if (b == true) ...
```

### Форматирование цепочных вызовов
см. [Цепочные вызовы](https://kotlinlang.org/docs/reference/coding-conventions.html#chained-call-wrapping)

### Форматирование лямбд
см. [Форматирование лямбд](https://kotlinlang.org/docs/reference/coding-conventions.html#lambda-formatting)

Если метод принимает только лямбду, сразу пишем ее после названия метода,
без круглых скобок

```
someMeth { //do something }
```

Внутри лямбды удобно использовать `it`, если не требуется уточнения или
внутри не используется еще одна лямбда, требующая параметр внешней лямбды(см. 2)
1)
``` kotlin
someList.map { it.id }.toHashSet()

someList.map {
	it.map { it.name }
}
```
2)
```kotlin
someList.map { objInList ->
	// someAnotherListj - коллекция вне лямбды
	someAnotherList.filter { it.id == objInList.id}
}
```

### Форматирование выражений

В методах `subscribe` писать лямбды с новой строки для лучшей читаемости кода.
Для сравнения первый(без переноса) и второй(с переносом) варианты:

```
subscribe(activityNavigator.observeResult(CategoryChooserRoute::class.java)
   .filter { result -> result.isSuccess }, { categoryChooserParam ->
   screenModel.categoryList = categoryChooserParam.data.categoryList
   view.render(screenModel)
})
```
```
subscribe(activityNavigator.observeResult(AddressChooserRoute::class.java)
   .filter { result -> result.isSuccess },
   { pinWithAddresss ->
       screenModel.pinWithAddress = pinWithAddresss.data
       view.render(screenModel)
   })
```

Плюс к этому в случае передачи в subscribe метода принимающего большое
количество аргументов лучше переносить вызов метода на следующую строку
```
subscribeIoHandleError(
    placesRepository.getShortPlaces(
        filter?.categories?.toList(),
        param,
        param
    ),
    {
         //some actions
    })
```
```
subscribeIoHandleError(placesRepository.getShortPlaces(
       filter?.categories?.toList(),
       param,
       filter?.isPromo ?: false,
       geoPosition = placesLoadRequestData.geodataRequest),
{})
```

В **логических выражениях** предпочтительнее использовать логические
операторы &, | и  тд, а не методы в явном виде. Данный тезис не касается `nullable` типов.
```
return isEmptyFirstName.not().and(isEmptyLastName.not()).and(isEmptyPhone.not())

return !isEmptyFirstName && !isEmptyLastName && !isEmptyPhone
```

Здесь нельзя написать `!screenModel.currentUserLocation?.isLocationDefault()`,
так как currentLocation может вернуть null
```
fun someBool() : Boolean = screenModel.currentUserLocation?.isLocationDefault()?.not() ?: false
```

## Идиомы
см. [Idiomatic use of language features](https://kotlinlang.org/docs/reference/coding-conventions.html#idiomatic-use-of-language-features)

### Kotlin конструкции

#### .apply{}

Удобно использовать, когда необходимо  проинициализировать свойства
объекта при передачи, как аргумента, в метод. Но надо быть внимательным
и по возможности не собирать все в одном месте.


[conv]: https://kotlinlang.org/docs/reference/coding-conventions.html
[style]: https://android.github.io/kotlin-guides/style.html
[robot]: https://habrahabr.ru/company/redmadrobot/blog/343458/
[surf]: java_codestyle.md
[r_naming]: https://github.com/RedMadRobot/kotlin-style-guide#%D0%9F%D1%80%D0%B0%D0%B2%D0%B8%D0%BB%D0%B0-%D0%B8%D0%BC%D0%B5%D0%BD%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F
[special_chars]: https://android.github.io/kotlin-guides/style.html#special-characters
[ q]


