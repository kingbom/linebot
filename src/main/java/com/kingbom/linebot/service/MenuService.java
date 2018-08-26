package com.kingbom.linebot.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.core.JsonParser.Feature.*;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.google.common.util.concurrent.Futures.getUnchecked;
/**
 * Created by bombay on 19/8/2018 AD.
 */
@Slf4j
@Component
public class MenuService {

    private static String token = "pw+qoPZAMq8m+LA74lbjdAyuWHiEMigfngtgAQ6M6neDWMeIoUXGkIKsn9vqOQA5gcvxplz+wdDqLIvIYFP/byGJruxzOifab6d6HIqRaj7L2o7o1g3+XUr2nfdVcOyZ2ixkSFYcWK3nD38uQbDnbAdB04t89/1O/w1cDnyilFU=";

    private static LineMessagingClient lineMessagingClient = LineMessagingClient.builder(token).build();

    public void initMenu() throws IOException {
        // Load menu from YAML
        String pathYamlHome = MenuService.class.getResource("/asset/richmenu-home.yml").getPath();
        String pathYamlMore = MenuService.class.getResource("/asset/richmenu-more.yml").getPath();

        // Rich Image Menu
        String pathImageHome = MenuService.class.getResource("/asset/home.png").getPath();
        String pathImageMore = MenuService.class.getResource("/asset/home.png").getPath();

        String richMenuId;

        // Create 1st Rich Menu (Home Menu)
        richMenuId = createRichMenu(pathYamlHome);
        log.info("createRichMenu richMenuHomeId : {}", richMenuId);
        imageUploadRichMenu(richMenuId, pathImageHome);

        // Create 2nd Rich Menu (More Menu)
        richMenuId = createRichMenu(pathYamlMore);
        log.info("createRichMenu richMenuMoreId : {}", richMenuId);
        imageUploadRichMenu(richMenuId, pathImageMore);

        listRichMenu(); // Show created Rich Menus
    }

    private String createRichMenu(String path) throws IOException {
        RichMenu richMenu = loadYaml(path);
        log.info("{}", richMenu);

        RichMenuIdResponse richMenuResponse = getUnchecked(lineMessagingClient.createRichMenu(richMenu));
        log.info("Successfully finished.");
        log.info("{}", richMenuResponse);
        return richMenuResponse.getRichMenuId();
    }

    private void imageUploadRichMenu(String richMenuId, String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        BotApiResponse botApiResponse = getUnchecked(lineMessagingClient.setRichMenuImage(richMenuId, MediaType.IMAGE_PNG_VALUE, bytes));
        log.info("Successfully finished.");
        log.info("{}", botApiResponse);
    }

    private List<String> listRichMenu() {
        List<String> listMenuString = new ArrayList<>();

        RichMenuListResponse richMenuListResponse = getUnchecked(lineMessagingClient.getRichMenuList());
        List<RichMenuResponse> listMenus = richMenuListResponse.getRichMenus();
        log.info("You have {} RichMenus", listMenus.size());

        log.info("Successfully finished.");
        listMenus.forEach(richMenuResponse -> {
            listMenuString.add(richMenuResponse.getRichMenuId());
            log.info("{}", richMenuResponse);
        });

        return listMenuString;
    }

    private RichMenu loadYaml(String path) throws IOException {
        final Yaml YAML = new Yaml();
        final ObjectMapper OBJECT_MAPPER = ModelObjectMapper
                .createNewObjectMapper()
                .configure(ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(ALLOW_COMMENTS, true)
                .configure(ALLOW_SINGLE_QUOTES, true)
                .configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(INDENT_OUTPUT, true);

        Object yamlAsObject;
        try(FileInputStream is = new FileInputStream(path)) {
            yamlAsObject = YAML.load(is);
        }

        return OBJECT_MAPPER.convertValue(yamlAsObject, RichMenu.class);
    }
}
