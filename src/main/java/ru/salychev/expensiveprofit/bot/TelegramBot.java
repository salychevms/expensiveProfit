package ru.salychev.expensiveprofit.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.salychev.expensiveprofit.botconfig.BotConfig;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            //get userId
            long chatId = update.getMessage().getChatId();
            //get userName, if it hides we get default userName
            String userName = update.getMessage().getChat().getUserName() != null ? update.getMessage().getChat().getUserName() : "NONAME";

            switch (messageText) {
                case "/start":
                    sendMessage(chatId, "Hi, " + userName + "! You activated this bot.");
                    break;
                case "/info":
                    sendMessage(chatId, "Author: Mikhail Salychev.\nJust for fun.\nBerlin 2023");
                    break;
                default:
                    sendMessage(chatId, "Sorry! It does not works.");
            }
        }
    }

    private void sendMessage(long chatId, String toSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(toSend);

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}