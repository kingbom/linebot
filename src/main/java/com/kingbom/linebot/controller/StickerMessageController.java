package com.kingbom.linebot.controller;

import com.kingbom.linebot.service.ReplyService;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@Slf4j
@LineMessageHandler
public class StickerMessageController {

    private final ReplyService replyService;

    public StickerMessageController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @EventMapping
    public void handleStickerMessage(MessageEvent<StickerMessageContent> event) {
        StickerMessageContent message = event.getMessage();
        log.info("PackageId : {} StickerId : {} ", message.getPackageId(), message.getStickerId());
        replyService.reply(event.getReplyToken(), new StickerMessage(message.getPackageId(), message.getStickerId()));
    }
}
