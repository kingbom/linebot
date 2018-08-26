package com.kingbom.linebot.controller;

import com.kingbom.linebot.service.ReplyService;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@Slf4j
@LineMessageHandler
public class LocationMessageController {

    private final ReplyService replyService;

    public LocationMessageController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @EventMapping
    public void handleLocationMessage(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent message = event.getMessage();
        replyService.reply(event.getReplyToken(), new LocationMessage(
                (message.getTitle() == null) ? "Location replied" : message.getTitle(),
                message.getAddress(),
                message.getLatitude(),
                message.getLongitude()
        ));
    }
}
