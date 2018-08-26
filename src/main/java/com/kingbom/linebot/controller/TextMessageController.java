package com.kingbom.linebot.controller;

import com.kingbom.linebot.flex.*;
import com.kingbom.linebot.helper.RichMenuHelper;
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

import org.springframework.core.io.ClassPathResource;


import java.io.IOException;
import java.util.Arrays;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@Slf4j
@LineMessageHandler
public class TextMessageController {

    String homeMenu = "richmenu-5e3d444a9fcbc90763aa8ff650866055";
    String moreMenu = "richmenu-99d1a5ddbed05dd802a4ab983984f37e";

    private final LineMessagingClient lineMessagingClient;

    private final ReplyService replyService;

    public TextMessageController(LineMessagingClient lineMessagingClient, ReplyService replyService) {
        this.lineMessagingClient = lineMessagingClient;
        this.replyService = replyService;
    }

    @EventMapping
    public void handleTextMessage(MessageEvent<TextMessageContent> event) throws IOException {
        TextMessageContent message = event.getMessage();
         handleTextContent(event.getReplyToken(), event, message);
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws IOException {
        String text = content.getText().toLowerCase();
        log.info("TextMessage : {}", text);
        String userId = event.getSource().getUserId();
        switch (text) {
            case "profile": {
                log.info("TextMessage case is profile");
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
                log.info("TextMessage case is หิว");
                replyService.reply(replyToken, Arrays.asList(new TextMessage("กินไหนดี"), new TextMessage("ตึก set"), new TextMessage("ฟอร์จูน") , new TextMessage("Food court esplanade")));
                break;
            }
            case "merchant service" : {
                replyService.reply(replyToken, new TextMessage(" merchant service"));
                break;
            }
            case "payment service" : {
                replyService.reply(replyToken, new TextMessage("payment service -> truemoney service"));
                break;
            }

            case "bs service" : {
                replyService.reply(replyToken, new TextMessage("bs service เป็นของทีม จาวา ที่ไปต่อหลาย service เช่น merchant service , payment service, redeem service"));
                break;
            }
            case "edc service" : {
                replyService.reply(replyToken, new TextMessage("edc service -> bs service"));
                break;
            }

            case "richmenu": {
                if(userId != null) {
                    lineMessagingClient.linkRichMenuIdToUser(userId, homeMenu);
                    return;
                }
                break;
            }
            case "richmenu unlink": {
                if(userId != null) {
                    lineMessagingClient.unlinkRichMenuIdFromUser(userId);
                    return;
                }
                break;
            }
            case "richmenu more": {
                if(userId != null) {
                    lineMessagingClient.linkRichMenuIdToUser(userId, moreMenu);
                    return;
                }
                break;
            }
            case "richmenu back": {
                if(userId != null) {
                    lineMessagingClient.linkRichMenuIdToUser(userId, homeMenu);
                    return;
                }
                break;
            }

            /*FlexMesage*/
            case "flex": {
                String pathImageFlex = new ClassPathResource("richmenu/richmenu-flexs.jpg").getFile().getAbsolutePath();
                String pathConfigFlex = new ClassPathResource("richmenu/richmenu-flexs.yml").getFile().getAbsolutePath();
                RichMenuHelper.createRichMenu(lineMessagingClient, pathConfigFlex, pathImageFlex, userId);
                break;
            }
            case "flex Back": {
                RichMenuHelper.deleteRichMenu(lineMessagingClient, userId);
                break;
            }

            case "flex restaurant": {
                replyService.reply(replyToken, new RestaurantFlexMessageSupplier().get());
                break;
            }
            case "flex menu": {
                replyService.reply(replyToken, new RestaurantMenuFlexMessageSupplier().get());
                break;
            }
            case "flex receipt": {
                replyService.reply(replyToken, new ReceiptFlexMessageSupplier().get());
                break;
            }
            case "flex news": {
                replyService.reply(replyToken, new NewsFlexMessageSupplier().get());
                break;
            }
            case "flex ticket": {
                replyService.reply(replyToken, new TicketFlexMessageSupplier().get());
                break;
            }
            case "flex catalogue": {
                replyService.reply(replyToken, new CatalogueFlexMessageSupplier().get());
                break;
            }


            default:
                log.info("TextMessage case is default");
                replyService.replyText(replyToken, "ขออภัยค่ะไม่พบข้อมูลที่ท่านต้องการ");
        }
    }

    private String getProfileInfo(UserProfileResponse profile) {
        return new StringBuilder("")
                .append("Display name   : $Display ".replace("$Display", profile.getDisplayName()) + "\n")
                .append("Status message : $StatusMessage ".replace("$StatusMessage", profile.getStatusMessage()) + "\n")
                .toString();
    }
}
