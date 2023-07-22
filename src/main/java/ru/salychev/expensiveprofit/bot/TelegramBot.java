package ru.salychev.expensiveprofit.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.salychev.expensiveprofit.botconfig.BotConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "welcome message"));
        listOfCommands.add(new BotCommand("/registeruser", "new user registration"));
        listOfCommands.add(new BotCommand("/login", "login user"));
        listOfCommands.add(new BotCommand("/logout", "logout user"));
        listOfCommands.add(new BotCommand("/status", "current status"));
        listOfCommands.add(new BotCommand("/mydata", "all user current data"));
        listOfCommands.add(new BotCommand("/deleteuser", "delete user"));
        listOfCommands.add(new BotCommand("/help", "information"));
        listOfCommands.add(new BotCommand("/info", "about bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Errors setting bot's command list: "+e.getMessage());
        }
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