# Core ui
Cоздан в результате развития идей проекта [ferro](https://github.com/MaksTuev/ferro).

Модуль расширяющий возможности ui части Android Framework.
Может быть использован для создания базовых классов, необходимых для реализации паттернов MVP, MVVM

### Основные сущности:

1. PersistentScope - хранилище для остальных сущностей,
   переживает смену конфигурации.
2. PersistentScopeStorage - хранилище всех PersistentScope в контексте приложения
3. ScreenState - текущее состояние экрана (пр. был ли он пересоздан в результате смены конфигурации или восстановлен с диска)
4. `Configurator` - используется для настройки DI
    * [`BaseActivityConfigurator`][bac]
    * [`BaseFragmentConfigurator`][bfc]
    * [`Configurator`][c]
5. ScreenEventDelegateManager - позволяет подписываться на системные события экрана, такие как onActivityResult, onCompletelyDestroy, onNewIntent и др.

Все эти сущности, переживают смену конфигурации, таким образом решается большая часть проблем, связанных с этим свойством фреймворка. 

### Дополнительные сущности:

1. ActivityNavigator, FragmentNavigator, TabFragmentNavigator, ChildFragmentNavigator,
 GlobalNavigator - позволяют осуществлять навигацию по приложению c помощью специальных сущностей Route
1. PermissionManager - позволяет запрашивать RuntimePermissions
1. ActivityProvider, FragmentProvider - предоставляют "живые" Activity или Fragment даже после смены конфигурации

Механизм делегирования событий экрана (см ScreenEventDelegateManager) позволяет создавать законченные сущности, которые необходимы для "чистой" архитектуры. Например, использование ActivityNavigator избавляет от необходимости переопределять метод onActivityResult и полностью инкапсулирует работу с Intent.

Большинство асинхронных взаимодействий осуществляются через rxJava. 

# Использование
[Пример использования](../core-ui-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:core-ui:X.X.X"
```

TODO: Добавить страницу с описанием нашей архитектуры и подходов

[bac]: src/main/java/ru/surfstudio/android/core/ui/configurator/BaseActivityConfigurator.java
[bfc]: src/main/java/ru/surfstudio/android/core/ui/configurator/BaseFragmentConfigurator.java
[c]: src/main/java/ru/surfstudio/android/core/ui/configurator/Configurator.java