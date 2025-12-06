# 🏰 AbilkaLand Core Plugin

Центральный плагин для сервера Minecraft клана **AbilkaLand**, объединяющий функциональность всех основных плагинов.

## ✨ Особенности

- 🥛 **Система "Молочко"** - внутренняя валюта клана
- 🛒 **Встроенный магазин** - покупка ресурсов и привилегий
- ⚙️ **Автонастройка плагинов** - автоматическая конфигурация Essentials, WorldGuard и др.
- 🔗 **Интеграции** - DiscordSRV, PlaceholderAPI, Vault
- 🎨 **Кастомизация** - приветствия, сообщения, GUI
- 📊 **Статистика** - отслеживание активности игроков

## 🚀 Установка

1. Скачайте последнюю версию из [Releases](https://github.com/gordey9992/AbilkaLand-Core/releases)
2. Поместите `AbilkaLand-Core.jar` в папку `plugins/`
3. Перезапустите сервер
4. Настройте конфигурацию в `plugins/AbilkaLand-Core/`

## ⚙️ Конфигурация

Основные файлы конфигурации:
- `config.yml` - основные настройки
- `messages.yml` - все текстовые сообщения
- `milk-shop.yml` - магазин валюты
- `clan-settings.yml` - настройки клана

## 🎮 Команды

| Команда | Описание | Права |
|---------|----------|-------|
| `/abilka` | Главное меню клана | `abilkaland.use` |
| `/milk` | Управление молочком | `abilkaland.use` |
| `/milk shop` | Открыть магазин | `abilkaland.use` |
| `/milk pay <игрок> <сумма>` | Перевести молочко | `abilkaland.use` |

## 🔌 Зависимости

### Обязательные:
- Vault
- PlaceholderAPI

### Рекомендуемые:
- EssentialsX
- LuckPerms
- WorldGuard
- GriefPrevention
- DiscordSRV

## 👥 Разработчики

- **DeepSeek** - Главный разработчик
- **gordey25690** - Помощник разработчика
- **PCshelly** - Основатель клана, Казначей и тестировщик

## 📄 Лицензия

MIT License. Смотрите файл [LICENSE](LICENSE).

---

*Создано с ❤️ для клана AbilkaLand*
