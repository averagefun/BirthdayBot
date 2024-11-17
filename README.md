# Проект по РБДиП «TelegramBirthdayBot»

## Выбранный сценарий: рефакторинг существующего программного проекта (2)

## Описание исходного проекта

### Предметная область

* Telegram бот, который напоминает о днях рождения
* У пользователя есть возможности добавить новое день рождения, для каждого день рождения выбрать время уведомлений (из фиксированных значений)
* У пользователя есть возможность просмотреть список дней рождения, удалить любое день рождения, редактировать уведомления
* У пользователя есть возможность поделиться добавленными днями рождения с другими пользователями
* У пользователей есть возможность добавить бота в группу, каждый пользователь в группе может редактировать бота в зависимости от своих прав (is_admin), функционал бота в группе также будет зависеть от прав самого бота в группе (is_bot_admin)

### Реализованные бизнес-процессы

1. При старте бота прописывается команда `/start`, пользователь добавляется в БД, затем устанавливает язык и часовой пояс

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXfI7_7p3mRZuvJCJjUTyazhIv_YhYFU3LAaPSsNloJLZX5QpEtgd4l5l_KLB4aazbZz9Epfp4QU0zZThNLte4guM5ueALx2jHFD1cEFPz8ESwCzc-epuJrFvPH4oP_0ZTE4pTmm3z56EoR3fvCaY2kf2Js7?key=juPnhHH0lr8xZ7oKYeOxUA)

2. После регистрации пользователь переходит в состояние “Главное меню”, в этом состоянии ему доступны несколько действий, которые можно вызвать нажатием на соответствующую кнопку или вызовом определенной команды

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXd_b9kpTiy82tjNU8vGkbphA2y_56qds0x1wfSPzAC9n8ZC3FGxcjyJ7B6_8K1lQhbohjCRPFDCenseKxfho1tc_A13gnzvHdSlKjkncy0sLkX4gblH64yG-qCer0xfko84i1rpp6cVKEky1bzGq4VUYqs?key=juPnhHH0lr8xZ7oKYeOxUA)

3. Команды в личных сообщениях с ботом
   
* 3.1. `/add` или кнопка “Добавить День Рождения” - создать новое ДР. Создание происходит в несколько этапов, на каждом из которых осуществляется валидация: Ввод ФИО именинника → ввод даты рождения → установка уведомлений

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXfDoQ5xEld-QJb3VHVfvZcxsFpE1QIJqHz6q1pBLQg4E5CuQ_Y0kIfdxHL_9QV54v2wzdO3lw5WiZ3NuYszyHvC-4v8k3K8YGnFpZ-R6BBJifAFNDR_R_VH8mGtcDHlCVogNRjSZRvr5G90-Gb4y1Ah8sbc?key=juPnhHH0lr8xZ7oKYeOxUA)

* 3.2 `/show` или кнопка “Показать дни рождения” → показать все ДР. Сначала предлагается выбрать месяц, затем в этом месяце можно выбрать конкретное ДР, в этом состоянии можно посмотреть/добавить уведомления, а также удалить ДР

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXeZbd-8m17G9jeTitWT7tTt9HYVeXU_SnXaXWnuhA8AtobZTaZ01Rs9bc7ZrBOeritR8Cgsq0lh2V54PJkxKtfADEvoxZPh8e8o_nYCN75uQ9rEHkh7QsMD5BdKdo6v2K25ahmaBfsth8FBdrF6UrWBzz4?key=juPnhHH0lr8xZ7oKYeOxUA)

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXcrEpLhIYSR0xLMKEYSKqkGp7zgt_7HAL5vr2Aq7tX8oj4GYcPMSXfAyl_acWdb2ccwFDKGnzmQiJ7jOL8o3KwqHFQWx6HmV25Njiw3JwzyDDNb_kLrdscx9F9hkB_8jxGh0mpADCWdSH2VsPHfsCzcYwtw?key=juPnhHH0lr8xZ7oKYeOxUA)

* 3.3 `/share` или кнопка “Поделиться”

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXfBJtYvwUN-5uFBSd7f0VDZCnpaDaITGuqVTHF1R027wIYHfwlmYPs-jS_6t2hcT4DCCHT27jTB26Zrn57DG-5eAl8BxhvEOBUZ_AQQNVHxTJMZlxoEmNUEdtMnwxE0X74nfzYvbh72nkcXvLsB9ixbZw54?key=juPnhHH0lr8xZ7oKYeOxUA)

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXdkUFYiM84PqUGInfjoXB0dLb9k8Wj5qVOokltNQh0yud7xspycaKJznAbhmqWInNKFbDAKPrGPb2Vxp7ZhPd8Pk9bseTsFz3UuosPqOicCn6GBIlFwLUBokIv0kjnrtBPvDtYM0AFxSt87iq2OCyBhqNk0?key=juPnhHH0lr8xZ7oKYeOxUA)

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXdrbnm6OS7GqPcL6FgoZejuz1rhGiVgvcrFw7eRshRmmnxZF-FZERG5qpdRNCypZ2alrNJY3NEo13k5oFcJo7nuLmk_lrB6a5iJ2EYo_7ixv0kSG6Rb68sQb_-R4RXJVK4xrGRlLfz7cQvgAsTc7jvw0-jk?key=juPnhHH0lr8xZ7oKYeOxUA)![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXe6-TyFWu4l5eNVmA9KIZSAaj20HcnkFpqkpbyq_2v5dlYvFI0w0klvwtJn1tEuyoqPDPcb5X4FvgXz1EXk1Yw6pIznu4CqP1qea-pkm9xTYGVugOr2IdwNpr5sHdyOXOflWlHrIPn2B3TeWLbRDqpk8Aff?key=juPnhHH0lr8xZ7oKYeOxUA)

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXe4AMHH2nFkYl_AXfr9ylsuYUuyHCFxRbhw4VgRbiVOok-P9xDrpuO566pdsG8WhqXPyyXzOnbEhZEdlN9kKdOqXOtvgolj2IHt60uAp5dF1BNI7F-AyqJAd__EMNtNxyN9-80ZWut2YybBps4Whbmh7Cfq?key=juPnhHH0lr8xZ7oKYeOxUA)

* 3.4 `/timezone`

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXdeaXZ5u4_bEzMWOIqw5IkxnQhjoMvE55Ogof2k-sT9B3fAbaXfq0AQn8MGt53FzkKEUbtTJbgEURUzd1KTSwxNH7chYZgjAmBxesW4ioUE4bLgL09JUVpf7dk7a9nweSwG34Yp-qFgrnH0WFcAmz4-1Lal?key=juPnhHH0lr8xZ7oKYeOxUA)

* 3.5 `/info` или кнопка “Информация”. Инструкция по пользованию ботом

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXenkXxllfVwfe4BgSrgGKJQmLn1a4WO2r_fpQxkjB-lIAfhxkipwH9L9G5Da3Ny0sTPuzn9ddSSARlssk16mPJiH1gAfYRe1x-vc6qjakyvf5FUGA_4eUUsYZ_X-mqnpE6co0zQO8l8Yuc1anOZJkD5_USY?key=juPnhHH0lr8xZ7oKYeOxUA)

4. Работа в группе

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXfBFwcplIR29Rkyf6DKOON6zJ5mW2eh1s5Qc72vbrPO6sxs0m9_pwzqtO5xPv2rzaT19LUBjrMjNCQUdoDRsfVP3hX11RCYC4yZyIuZA70ivAJCyhfQaKV8dvE-sPfuaJsvhZMOtjx4wScmfcmxfgQIbmO3?key=juPnhHH0lr8xZ7oKYeOxUA)

* 4.1 `/editGroup` Перейти в режим редактирования данной группы в личных сообщениях с ботом

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXc5BTvD05cn7-NS-B9bK-XpprEewRRAkygQUpRmPpvQzOQpaPYkI4cV5_1hMCbcSNvqjyl9FmbzZpM517PEUA43u65PuNTIbC3zwAbAlOUSmrn06TN_GqRV9udkWO8svF6XoMQyBZkMjspB3acNbjrbJo8?key=juPnhHH0lr8xZ7oKYeOxUA)

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXc8nKv8VQs0zHShvg2PTaTpzToEK859AabAD341mRT3bQ4mBgqM4O9-FhXTGRaHMEcxITJF5Rd3o1iVccEnWbhXNK_sf76PIOG8y-bbbemiXGcI1W2MMN7cSOT4BJkp-qR8CS_xowbUMj4tw6brznrP-Day?key=juPnhHH0lr8xZ7oKYeOxUA)

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXeyx9Xj0YORX82Eur_gcYS_JS1imaipfq3dkFNakACGBI_aLWiGVlpUr8aI-8SN5ydKmxdXQHo740I2yxMpXbOglkh705E8H7KWfGNYrobdddgnL_w6lvEjsC60f_gIpecYcJNsqhb2Pm7z9c-r3sADH_-J?key=juPnhHH0lr8xZ7oKYeOxUA)

* 4.2 `/exit` Выйти из режима редактирования группы

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXeLo13cwM0lULDYNZ6Y5GefcJCXXUMuJ3hG9YDvR15KX3w9Dlv0Nx-VAKo4HHHDdLclzLzuO9C1jXfI8YhbqmsJ1SAQys98fw4eUbpwYKJwt5CopjYgQ2OB7efCqmiarcg9ts7SmkmYkmJoiTGZI-BpY1CV?key=juPnhHH0lr8xZ7oKYeOxUA)

### Инфологическая модель данных

![](https://lh7-rt.googleusercontent.com/docsz/AD_4nXd3zOfsAje28KVJ6XjbDZVNzkqRDjLemP_ylb4g1V7_HSzwUsnpxvGVSqmKzFz4PHfrigHJFFnG0FQgm_dTxQWntDF0pBumVlnbYH4oZz3W0oNcCSEJukjp9jU9tnlcI64KAfck16darfdli8MVZ1J4KOg?key=TiSBvky5thOm2G3YYybbNw)

## Рефакторинг проекта

### Цели рефакторинга

Рефакторинг проекта направлен на улучшение внутренней структуры, модульности, и повышения качества кода с учетом требований дисциплины. В рамках работы предполагается выполнение следующих задач:

1. **Реорганизация архитектуры проекта:**
    - Перевод проекта на многокомпонентную архитектуру с четким разделением ответственности между сервисами
    - Выделение следующих компонентов:
        - **Core-сервис:** отвечает за бизнес-логику управления днями рождения ич уведомлениями
        - **Notification-сервис:** занимается отправкой уведомлений
        - **Group-сервис:** отвечает за обработку действий в группах
        - **UI Layer (Telegram API Layer):** взаимодействие с Telegram API

2. **Оптимизация взаимодействия компонентов:**
    - Конфигурируемость сервисов через файлы конфигурации

3. **Переработка моделей данных:**
    - Актуализация схемы базы данных с учетом требований для обеспечения независимости компонентов
    - Добавление миграций через Liquibase/Flyway для управления схемами БД

4. **Улучшение тестового покрытия:**
    - Разработка юнит-тестов для всех сервисов
    - Покрытие интеграционными тестами взаимодействий между компонентами
    - Тестирование API через Postman или аналогичный инструмент

5. **Соблюдение принципов разработки:**
    - Использование принципов SOLID, DRY, и KISS
    - Строгое разделение публичной и приватной функциональности модулей
    - Упрощение и стандартизация кода для улучшения читаемости и поддержки

### Этапы рефакторинга

#### 1. **Реорганизация структуры проекта**
- Перенос текущей логики в отдельные модули
- Создание отдельного модуля для управления уведомлениями
- Реализация отдельного модуля для управления группами и их правами

#### 2. **Переработка базы данных**
- Введение новой таблицы для хранения конфигурации уведомлений
- Обеспечение миграций базы данных с помощью Liquibase

#### 3. **Реализация конфигурации через файлы**
- Перевод всех параметров настройки (например, таймзоны, частоты уведомлений) в конфигурационные файлы (`application.yml`)
- Реализация валидаторов для проверки корректности конфигурации

#### 4. **Обновление логики работы с Telegram API**
- Разделение бизнес-логики от слоя взаимодействия с Telegram API
- Введение паттернов, упрощающих добавление новых команд

#### 5. **Улучшение процесса сборки и развертывания**
- Настройка CI/CD через GitHub Actions:
    - Запуск линтеров, тестов и сборки при каждом пуше
    - Автоматическое развертывание Docker-контейнеров для всех компонентов
- Упаковка проекта в Docker для обеспечения совместимости

### Ожидаемые результаты

- Улучшение читаемости и поддержки кода
- Обеспечение модульности и повторного использования компонентов
- Возможность масштабирования отдельных компонентов
- Повышение надежности работы и покрытия тестами
