# Notepad — приложение "Заметки" для Android

Учебный проект: мобильное приложение для создания, редактирования и удаления
текстовых заметок с авторизацией пользователя и облачной синхронизацией.

## Функциональность

- Регистрация и вход по email/паролю (Firebase Authentication)
- Добавление заметки через поле ввода и кнопку «Добавить»
- Список заметок в RecyclerView
- Короткое нажатие на заметку — редактирование
- Долгое нажатие — удаление с подтверждением (AlertDialog)
- Кнопка «Выйти» из аккаунта
- Данные хранятся в облаке (Cloud Firestore: `users/{uid}/notes`)
  и кэшируются локально (SQLite) для работы без интернета

## Стек технологий

- Java
- Android SDK (min API 24, target API 34)
- SQLite (SQLiteOpenHelper) — локальное хранилище
- Firebase Authentication — вход/регистрация
- Cloud Firestore — облачная база данных
- RecyclerView, AlertDialog — UI

## Архитектура

Без MVVM/RxJava/Coroutines. Простая слоистая структура:

- `Note` — модель данных
- `DatabaseHelper` — работа с SQLite
- `NoteRepository` — единая точка доступа к данным (SQLite + Firestore)
- `LoginActivity`, `MainActivity` — экраны
- `NoteAdapter` — адаптер для RecyclerView

## Как запустить

1. Клонировать репозиторий
2. Открыть в Android Studio
3. Создать свой проект в Firebase Console, скачать `google-services.json`
   и положить в папку `app/`
4. Включить в Firebase Authentication метод Email/Password
5. Создать Firestore Database
6. Собрать и запустить проект (Run ▶)





## Автор

Максим Туркин , студент: 23-СПО-ИСИП-01 
