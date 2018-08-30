#Скрипты с механизмом подключения модулей Android Standard

Скрипты позволяют переключаться между локальным искодным кодом android-standard и артефактами из artifactory.
Использование локального исходного кода позволит быстро тестировать изменения в android-standard на своем проекте без деплоя артефактов.

##Переключение источника android-standard

1. Изменить флаг ```androidStandardDebugMode``` в файле ```android-standard/androidStandard.properties```
2. Выполнить ```Build - Clean Project```.

##Первичная настройка на конкретной машине

Добавить в папку android-standard файл ```androidStandard.properties``` со следующим содержимым:
```
androidStandardDebugDir=/full/path/to/your/local/android-standard
androidStandardDebugMode=false       # флаг для активации режима локальной загрузки репозитория android-standard
```

##Подключение скриптов к сборщику gradle
Этот раздел будет полезен для тех кто собирается перенести эти скрипты в существующий проект

+ **settings.gradle** уровня проекта

В конец данного файла необходимо добавить ```apply from: 'android-standard/androidStandardSettings.gradle'```
для подключения локального репозитория android-standard.

+ **build.gradle** уровня модуля приложения

Добавить модули android-standard следующим образом:
```
dependencies {
    // other dependencies

    gradle.ext.androidStandard.addApi(
            this,
            [
                    "core-ui",     // массив модулей android-standard, который необходимо подключить к текущему модулю
                    "core-mvp",
                    "core-app"
            ]
    )
}
```

+ **gitignore** уровня проекта
Добавить ```/android-standard/androidStandard.properties```
