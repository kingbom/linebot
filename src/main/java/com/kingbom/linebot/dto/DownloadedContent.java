package com.kingbom.linebot.dto;

import lombok.Value;

import java.nio.file.Path;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@Value
public class DownloadedContent {

    public Path path;

    public String uri;
}
