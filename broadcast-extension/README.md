# Broadcast-extension
Модуль содержит расширения для Broadcast Receiver.

# Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо,
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.android:broadcast-extension:X.X.X"
```

# Использование

## 1. [RxBroadcastReceiver](src/main/java/ru/surfstudio/android/broadcast/extension/RxBroadcastReceiver.kt)
Реактивная обертка над BroadcastReceiver, преобразующая поступающую информацию в Observable.
## 2. [BaseSmsRxBroadcastReceiver](/home/azaytsev/StudioProjects/android-standard/broadcast-extension/src/main/java/ru/surfstudio/android/broadcast/extension/BaseSmsRxBroadcastReceiver.kt)
Дополнение для RxBroadcastReceiver, предназначенное для перехвата и обработки получаемых SMS-сообщений.
