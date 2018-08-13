package com.kingbom.linebot.controller;

import com.kingbom.linebot.dto.DownloadedContent;
import com.kingbom.linebot.service.FileManagerService;
import com.kingbom.linebot.service.ReplyService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.concurrent.ExecutionException;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@LineMessageHandler
public class ImageMessageController {

    private final LineMessagingClient lineMessagingClient;

    private final FileManagerService fileManagerService;

    private final ReplyService replyService;

    public ImageMessageController(LineMessagingClient lineMessagingClient, FileManagerService fileManagerService, ReplyService replyService) {
        this.lineMessagingClient = lineMessagingClient;
        this.fileManagerService = fileManagerService;
        this.replyService = replyService;
    }

    @EventMapping
    public void handleImageMessage(MessageEvent<ImageMessageContent> event) {
        ImageMessageContent content = event.getMessage();
        String replyToken = event.getReplyToken();
        try {
            MessageContentResponse response = lineMessagingClient.getMessageContent(content.getId()).get();
            DownloadedContent jpg = fileManagerService.saveContent("jpg", response);
            DownloadedContent previewImage = fileManagerService.createTempFile("jpg");
            fileManagerService.system("convert", "-resize", "240x", jpg.path.toString(), previewImage.path.toString());
            replyService.reply(replyToken, new ImageMessage(jpg.getUri(), previewImage.getUri()));
        } catch (InterruptedException | ExecutionException e) {
            replyService.reply(replyToken, new TextMessage("Cannot get image: " + content));
            throw new RuntimeException(e);
        }
    }
}
