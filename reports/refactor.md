## Рефакторинг проекта

### Этап 1
- [Отчет](https://docs.google.com/document/d/19-l0TzHefwnna_3xf68ium_rQj5Rn8MxjrSuKXKPAEE/edit?usp=sharing)
- [Версия кода](https://github.com/averagefun/BirthdayBot/commits/v1)
- [Сравнение изменений кода](https://github.com/averagefun/BirthdayBot/compare/v0...v1)

#### Изменения
- Добавление локализации
- Реализация модуля для работы бота в группах с соответствующими правами
- Рефакторинг флагов сущностей - заменили их на соответствующие enum'ы уровня кода и бд

### Этап 2
- [Отчет](https://docs.google.com/document/d/1QN0nBBxs4cYjdbJaSs3lBk4mZrmG_rrWgx08r-7IQMw/edit?usp=sharing)
- [Версия кода](https://github.com/averagefun/BirthdayBot/commits/v2)
- [Сравнение изменений кода](https://github.com/averagefun/BirthdayBot/compare/v1...v2)

#### Изменения
- Добавление unit тестов
- Добавление логирования
- Рефакторинг модели хранения уведомлений

### Этап 3
- [Отчет](https://docs.google.com/document/d/1ul8za-oatkZL3k-iCT-mLz5CWr9HjdspLitI70o8UiQ/edit?usp=sharing)
- [Версия кода](https://github.com/averagefun/BirthdayBot/commits/v3)
- [Сравнение изменений кода](https://github.com/averagefun/BirthdayBot/compare/v2...v3)

#### Изменения
- Разворачивание приложения с использованием docker-compose
- Настройка GitHub CI/CD с запуском тестов
