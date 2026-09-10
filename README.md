# EasyVerbs

Android-приложение для изучения английских неправильных глаголов. Нативное приложение на Kotlin + Jetpack Compose, ориентированное на русскоязычных пользователей с поддержкой испанского, немецкого, французского и португальского языков.

## Возможности

### Экраны

| Экран | Описание |
|---|---|
| **Главная** | Панель статистики: количество выученных глаголов, точность, стрика |
| **Словарь** | Полный список глаголов с поиском, фильтрами, избранными и озвучкой |
| **Карточки** | Режим флеш-карт: V1 на лицевой стороне, V2/V3 на обратной |
| **Буквы** | Заполнение пропущенных букв — введите V1 по переводу |
| **Тренажер** | Мультирежим: прямой/обратный перевод, формы, предложения, аудио, сопоставление |
| **Ошибки** | Список глаголов с ошибками, отсортированный по количеству |
| **Скорость** | 60-секундный спринт: правильный ответ +1 секунда, рекорд сохраняется |
| **Экзамен** | 20 вопросов со случайными типами заданий |
| **Статистика** | Сессия, точность, стрика, выученные глаголы, частые ошибки |
| **Предлоги** | 20 упражнений на сочетание предлогов с глаголами |
| **Настройки** | Количество вопросов, темная тема, звук/вибрация, язык, акценты, сброс |

### Данные

- **102 неправильных глагола** (от "be" до "write") с формами V1/V2/V3
- Переводы на **5 языков**: русский, испанский, немецкий, французский, португальский
- Прогресс сохраняется в **Room Database** (избранное, ошибки, уверенность)
- Настройки хранятся в **DataStore Preferences**

## Технологии

- **Kotlin 2.1.0**
- **Jetpack Compose** (BOM 2024.12.01) + Material 3
- **Room 2.6.1** (KSP)
- **DataStore Preferences 1.1.1**
- **Navigation Compose 2.8.5**
- **ViewModel Compose 2.8.7**
- Android TTS + Haptic Feedback
- Gradle 8.11.1, AGP 8.7.3

## Требования

- Android Studio Ladybug (2024.2.1) или новее
- JDK 17
- Android SDK 35
- Min SDK: 26 (Android 8.0)

## Запуск

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/EzyLabsHQ/EasyVerbs-app.git
   ```
2. Откройте проект в Android Studio
3. Дождитесь синхронизации Gradle
4. Нажмите **Run** или выберите эмулятор/устройство

## Структура проекта

```
app/src/main/java/com/easylearn/easyverbs/
├── EasyVerbsApp.kt              # Application класс
├── MainActivity.kt              # Single Activity
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt       # Room Database
│   │   ├── VerbDao.kt           # DAO для глаголов
│   │   ├── VerbSeedData.kt      # 102 глагола (hardcoded)
│   │   └── SettingsManager.kt   # DataStore настройки
│   ├── model/
│   │   ├── Verb.kt              # Room Entity
│   │   ├── UserStats.kt         # Модель статистики
│   │   └── TrainerMode.kt       # Режимы тренажера
│   └── repository/
│       └── VerbRepository.kt    # Репозиторий данных
├── ui/
│   ├── EasyVerbsMainScreen.kt   # NavHost + Scaffold
│   ├── navigation/
│   │   ├── Screen.kt            # Маршруты
│   │   └── NavItem.kt           # Нижняя навигация
│   ├── theme/
│   │   ├── Color.kt             # Палитра + акценты
│   │   ├── Theme.kt             # Material 3 тема
│   │   └── Type.kt              # Типографика
│   └── screens/
│       ├── home/HomeScreen.kt
│       ├── dictionary/DictionaryScreen.kt
│       ├── flashcards/FlashcardsScreen.kt
│       ├── letters/LettersScreen.kt
│       ├── trainer/TrainerScreen.kt
│       ├── mistakes/MistakesScreen.kt
│       ├── speed/SpeedScreen.kt
│       ├── exam/ExamScreen.kt
│       ├── stats/StatsScreen.kt
│       ├── prepositions/PrepositionsScreen.kt
│       └── settings/SettingsScreen.kt
└── util/
    ├── TtsManager.kt            # Text-to-Speech
    └── HapticManager.kt         # Вибрация
```

## Лицензия

EasyVerbs распространяется под лицензией [GNU Affero General Public License v3.0](LICENSE) (`AGPL-3.0-only`).

**Copyright © 2026 EasyLabsHQ**

Кратко: вы можете свободно использовать, модифицировать и распространять программу при условии, что производные работы публикуются под той же лицензией AGPL-3.0, а исходный код доступен пользователям — в том числе при доступе через сеть (принцип network copyleft).

Полный текст лицензии: [LICENSE](LICENSE)
