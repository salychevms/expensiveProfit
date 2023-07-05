package ru.salychev.expensiveprofit.botConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.salychev.expensiveprofit.bot.TelegramBot;

@Component
public class BotInitializer {
    @Autowired
    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public  void init() throws TelegramApiException{
        TelegramBotsApi telegramBotsApi=new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(bot);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
}
