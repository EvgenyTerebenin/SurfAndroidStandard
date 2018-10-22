[TOC]

# Release Notes

## 0.3.0 - SNAPSHOT

* NotificationCenter - устарел
* Добавлен новый класс `PushManager` - его апи соответсвует апи `NotificationCenter`
* Теперь существует возможность конфигурировать помощник через даггер.
* Существует возможность подписаться на пуш через `PushInteractor`

## 0.2.2-SNAPSHOT
* Добавлен экспериментальный модуль Core mvp binding
* В PictureProvider добавлен метод возвращающий Uri(вместо реального пути) выбранного файла
* Испрвлен package name для TitleSubtitleView


## 0.2.1
* ANDDEP-137 Добавить возможность изменять errorHandler в basePresenter
* ANDDEP-138 Доработки PlaceHolderView
    * Добавлен набор индикаторов загрузки
    * Добавлено новое состояние LoadState "No Internet";
    * Добавлены атрибуты `pvProgressBarWidth` и `pvProgressBarHeight` - через них можно задавать ширину и высоту прогресс-индикатора соответственно;
    * Добавлены атрибуты `pvOpaqueBackground` и `pvTransparentBackground` - через них можно задавать drawable-ресурсы в качестве фона;
    * Появилась поддержка 28-и кастомных прогресс-индикаторов `pvProgressBarType`;
    * Атрибут `pvProgressBarColor` теперь принимает не только ссылки на цветовые ресурсы, но и коды вида `#00FFAA`;
    * Правка гравитации подписей
    * Добавлены атрибуты `pvTitleLineSpacingExtra` и `pvSubtitleLineSpacingExtra` для изменения высоты строки заголвоков
    * Правка проблем с перехватом жестов на плейсхолдере
* ANDDEP-140 TextViewExtensions добавлены публичные методы:
    * EditText.selectionToEnd() - перевод каретки в конец
    * EditText.allowMatch(predicate) - фильтрация символов
    * EditText.restrictMatch(predicate) - // -
* ANDDEP-50 исправлены задержки переключения состояний с в PlaceHolderView
* ANDDEP-142 исправлен баг с неправильным добавлением элемента в методе ItemList.addStickyHeaderIf()
* ANDDEP-145 Добавлена в BasePaginationableAdpater поддержка StaggeredGrid
* TitleSubtitleView - отключение Clickable при установке пустого листенера, мелкие правки
* ANDDEP-148 BaseCallAdapterFactory - добавлена поддержка Flowable, Maybe, Single, Completable
* AnimationUtil - правки анимаций FadeIn/FadeOut
* ANDDEP-79 Добавлены распространенные классы и интерфейсы обертки над данными: BlockableData, CheckableData, DeletableData, ExpandableData, LoadableData, ScrollableData, SelectableData, VisibleData и методы расширения на их коллекции
* ANDDEP-194 BaseCallAdapterFactory добавлена возможность повторить неудавшийся запрос через метод доступный в предке класса
* ANDDEP-197 добавлен модуль broadcast-extension c RxBroadcastReceiver и BaseSmsBroadcastReceiver
* ANDDEP-199 PictureProvider переход сразу в галерею (изменен роут, убран Chooser)
* ANDDEP-198 Добавлены метод shouldShowRequestPermissionRationale в PermissionManager, позволяющий понять показывается ли еще диалог запроса пермишена или он уже запрещен
* ImageLoader -
    * Поднятие версии Glide до 3.7.1
    * Правка оптимизации
    * Исправлена загрузка в SimpleTarget
    * Добавлен downSampling
    * Добавлена возможность принудительного обновления изображения с помощью метода force()
    * ANDDEP-212 Добавлен метод crossFade для плавного рендеринга изображений
* MessageController
    * ANDDEP-210 добавлен параметр - длительность показа снека
    * ANDDEP-210 Добавлена возможность убирать снек, важно при наличии снеков, которые не уходят сами
* Исправлено падение IllegalStateException "Can't find Persistent scope ..." при переходе с восстановленного экрана, содержащего фрагменты в состоянии detached
* ANDDEP-213 Исправлено закрытие диалога StandardDialog если установлен флаг isCancelable
* ANDDEP-215 Перевод всех диалогов на AppCompatDialog


## 0.2.0
* Fix сравнения путей для SimpleCache
* ANDDEP-127 Добавлена поддержка слияния data-list с 2х сторон
* ANDDEP-136 fix не логгируются http запросы
* ANDDEP-107 Исправления PlaceholderView
* ANDDEP-125 sticky header fix
* ANDDEP-109  правка StandardDialogRoute
* ANDDEP-108  Сделать TabFragmentNavigator через detach/attach