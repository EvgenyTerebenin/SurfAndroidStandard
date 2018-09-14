[TOC]

# Создание нового экрана

##### Создание экрана
Для создания экрана необходимо проделать следующие шаги:

1. Расширить [4 класса ..Route, BasePresenter, ..View, , ..ScreenModel][core-mvp]

1. Расширить ScreenConfigurator в app-injector'е и занести его в
[ScreenConfiguratorStorage][multi];

При наследовании от одной из базовых View следует реализовать следующие методы:

* `#getPresenters()` - должен возвращать Presenter'ы экрана. View получает
Presenter с помощью инъекции в поле;

* `#getContentView()` - (только для BaseActivityView) должен вернуть
Id Layout’а этого экрана.

* `#createConfigurator()` - получает конфигуратор через компонент
провайдер -  ComponentProvider.

**ScreenConfigurator** инкапсулирует всю логику работы с Dagger.
ScreenConfigurator должен содержать:

* Интерфейс наследник от ScreenComponent,
для которого следует указать View экрана как параметр типа.
Если на этом экране используются диалоги с возвращением результата,
то в этом компоненте следует также определить методы `#inject()`
для каждого из диалогов.
В компоненте должен быть указан родительский компонент
(в большинстве случаев AppComponent) в `dependencies` и
ActivityViewModule(FragmentViewModule) и другие модули,
необходимые для этого экрана в `modules`;

* Dagger модуль экрана (опционально),
который необходим передачи аргументов, с которыми стартовал экран,
и для простоты может расширять CustomScreenModule.

При наследовании от BasePresenter следует указать View экрана как параметр типа.

##### Шаблоны

Для создания нового экрана предусмотрены шаблоны.
Чтобы их использовать необходимо выполнить следующее:

1. Скопировать папку `surf` из директории `android-standard/templates/file`
в папку `<android-studio-folder>/plugins/android/lib/templates`

1. Нажать ПКМ в дереве пакетов -> New -> Surf -> Activity/Fragment

Шаблоны работают в студии 3.0 и выше

[core-mvp]: ../../core-mvp/docs/usage.md
[multi]: ../common/multimodule/detail.md