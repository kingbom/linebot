package com.kingbom.linebot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@SpringBootApplication
public class LinebotApplication {

	public static Path downloadedContentDir;

	public static void main(String[] args) throws IOException {
		downloadedContentDir = Files.createTempDirectory("line-bot");
		//MenuService menuService = new MenuService();
		//menuService.initMenu();
		SpringApplication.run(LinebotApplication.class, args);
	}
}
