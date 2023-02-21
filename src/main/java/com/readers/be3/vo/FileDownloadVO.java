package com.readers.be3.vo;

import java.nio.file.Path;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDownloadVO {
    Path folderLocation; 
    String filename;
}
