package com.kingbom.linebot.controller;

import com.kingbom.linebot.service.ReplyService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@LineMessageHandler
public class TextMessageController {

    private final LineMessagingClient lineMessagingClient;

    private final ReplyService replyService;

    public TextMessageController(LineMessagingClient lineMessagingClient, ReplyService replyService) {
        this.lineMessagingClient = lineMessagingClient;
        this.replyService = replyService;
    }

    @EventMapping
    public void handleTextMessage(MessageEvent<TextMessageContent> event) {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        String text = content.getText().toLowerCase();
        switch (text) {
            case "profile": {
                String userId = event.getSource().getUserId();
                if(userId != null) {
                    lineMessagingClient.getProfile(userId).whenComplete((profile, throwable) -> {
                        if(throwable != null) {
                            replyService.replyText(replyToken, throwable.getMessage());
                            return;
                        }
                        String profileInfo = getProfileInfo(profile);
                        replyService.reply(replyToken, Arrays.asList(new TextMessage(profileInfo)));
                    });
                }
                break;
            }
            case "หิว" : {
                replyService.reply(replyToken, Arrays.asList(new TextMessage("กินไหนดี")));
                break;
            }

            default: replyService.replyText(replyToken, text);
        }
    }

    private String getProfileInfo(UserProfileResponse profile) {
        return new StringBuilder("")
                .append("Display name   : $Display ".replace("$Display", profile.getDisplayName()) + "\n")
                .append("Status message : $StatusMessage ".replace("$StatusMessage", profile.getStatusMessage()) + "\n")
                .toString();
    }
}
