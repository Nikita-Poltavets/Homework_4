package ua.com.alevel.nix.fileconversation.view.data;

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import ua.com.alevel.nix.fileconversation.view.config.ConversationType;
import ua.com.alevel.nix.fileconversation.view.controller.files.*;

import java.nio.file.Path;

public class FileData {

    private final UriComponents uri;
    private final String fileName;

    public FileData(Path path, ConversationType conversationType) {
        this.fileName = path.getFileName().toString();
        this.uri = initUriComponents(this.fileName, conversationType);
    }

    public UriComponents getUri() {
        return uri;
    }

    public String getFileName() {
        return fileName;
    }

    private UriComponents initUriComponents(String fileName, ConversationType conversationType) {
        switch (conversationType) {
            case IDENTITY : return MvcUriComponentsBuilder.fromMethodName(IdentityFileController.class,
                    "serveFile", fileName).build();
            case SPLIT : return MvcUriComponentsBuilder.fromMethodName(SplitFileController.class,
                    "serveFile", fileName).build();
            case REPLACE : return MvcUriComponentsBuilder.fromMethodName(ReplaceFileController.class,
                    "serveFile", fileName).build();
            case COUNTSYMBOLS: return MvcUriComponentsBuilder.fromMethodName(CountSymbolsFileController.class,
                    "serveFile", fileName).build();
            case COUNTWORDS: return MvcUriComponentsBuilder.fromMethodName(CountWordsFileController.class,
                    "serveFile", fileName).build();
            case REVERSE: return MvcUriComponentsBuilder.fromMethodName(ReverseFileController.class,
                    "serveFile", fileName).build();
            case FINDROOTS: return MvcUriComponentsBuilder.fromMethodName(FindRootsFileController.class,
                    "serveFile", fileName).build();
        }
        return null;
    }
}
