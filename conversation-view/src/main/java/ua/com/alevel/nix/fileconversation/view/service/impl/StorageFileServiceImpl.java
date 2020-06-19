package ua.com.alevel.nix.fileconversation.view.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import ua.com.alevel.nix.fileconversation.util.ConversationUtil;
import ua.com.alevel.nix.fileconversation.view.config.ConversationType;
import ua.com.alevel.nix.fileconversation.view.config.StorageProperties;
import ua.com.alevel.nix.fileconversation.view.exception.StorageException;
import ua.com.alevel.nix.fileconversation.view.exception.StorageFileNotFoundException;
import ua.com.alevel.nix.fileconversation.view.service.StorageFileService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class StorageFileServiceImpl implements StorageFileService {

    private final Path locationIdentityDir;
    private final Path locationSplitDir;
    private final Path locationReplaceDir;
    private final Path locationCountSymbolsDir;
    private final Path locationCountWordsDir;
    private final Path locationReverseDir;
    private final Path locationFindRootsDir;
    private final List<Path> locationDirs;

    public StorageFileServiceImpl(StorageProperties storageProperties) {
        this.locationIdentityDir = Paths.get(storageProperties.getLocationIdentityDir());
        this.locationSplitDir = Paths.get(storageProperties.getLocationSplitDir());
        this.locationReplaceDir = Paths.get(storageProperties.getLocationReplaceDir());
        this.locationCountSymbolsDir = Paths.get(storageProperties.getLocationCountSymbolsDir());
        this.locationCountWordsDir = Paths.get(storageProperties.getLocationCountWordsDir());
        this.locationReverseDir = Paths.get(storageProperties.getLocationReverseDir());
        this.locationFindRootsDir = Paths.get(storageProperties.getLocationFindRootsDir());
        this.locationDirs = Arrays.asList(this.locationIdentityDir, this.locationSplitDir, this.locationReplaceDir, this.locationCountSymbolsDir, this.locationCountWordsDir, this.locationReverseDir, this.locationFindRootsDir);
    }

    @Override
    public void store(MultipartFile file, ConversationType conversationType) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.locationIdentityDir.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            if (!conversationType.equals(ConversationType.IDENTITY)) {
                storeIfConversation(filename, conversationType);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll(ConversationType conversationType) {
        switch (conversationType) {
            case IDENTITY : return loadAll(this.locationIdentityDir);
            case SPLIT : return loadAll(this.locationSplitDir);
            case REPLACE : return loadAll(this.locationReplaceDir);
            case COUNTSYMBOLS: return loadAll(this.locationCountSymbolsDir);
            case COUNTWORDS: return loadAll(this.locationCountWordsDir);
            case REVERSE: return loadAll(this.locationReverseDir);
            case FINDROOTS: return loadAll(this.locationFindRootsDir);
        }
        return null;
    }

    @Override
    public Path load(String filename, ConversationType conversationType) {
        switch (conversationType) {
            case IDENTITY : return locationIdentityDir.resolve(filename);
            case SPLIT : return locationSplitDir.resolve(filename);
            case REPLACE : return locationReplaceDir.resolve(filename);
            case COUNTSYMBOLS: return locationCountSymbolsDir.resolve(filename);
            case COUNTWORDS: return locationCountWordsDir.resolve(filename);
            case REVERSE: return locationReverseDir.resolve(filename);
            case FINDROOTS: return locationFindRootsDir.resolve(filename);
        }
        return null;
    }

    @Override
    public Resource loadAsResource(String filename, ConversationType conversationType) {
        try {
            Path file = load(filename, conversationType);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void init() {
        this.locationDirs.forEach(path -> {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new StorageException("Could not initialize storage", e);
            }
        });
    }

    @Override
    public void deleteAll() {
        this.locationDirs.forEach(path -> FileSystemUtils.deleteRecursively(path.toFile()));
    }

    @Override
    public void delete(String filename, ConversationType conversationType) {
        try {
            Path file = load(filename, conversationType);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                Files.deleteIfExists(file);
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (IOException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    private Stream<Path> loadAll(Path rootPath) {
        try {
            return Files.walk(rootPath, 1)
                    .filter(path -> !path.equals(rootPath))
                    .map(rootPath::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }



    private void storeIfConversation(String filename, ConversationType conversationType) {
        Path path = load(filename, ConversationType.IDENTITY);
        try {
            String readFile = Files.readString(path);
            switch (conversationType) {
                case SPLIT : {
                    String linesAfterConversation = ConversationUtil.splitConversation(readFile);
                    storeAfterConversation(filename, this.locationSplitDir, conversationType, linesAfterConversation);
                } break;
                case REPLACE : {
                    String linesAfterConversation = ConversationUtil.replaceConversation(readFile);
                    storeAfterConversation(filename, this.locationReplaceDir, conversationType, linesAfterConversation);
                } break;
                case COUNTSYMBOLS: {
                    String linesAfterConversation = ConversationUtil.countSymbolsConversation(readFile);
                    storeAfterConversation(filename, this.locationCountSymbolsDir, conversationType, linesAfterConversation);
                } break;
                case COUNTWORDS: {
                    String linesAfterConversation = ConversationUtil.countWordsConversation(readFile);
                    storeAfterConversation(filename, this.locationCountWordsDir, conversationType, linesAfterConversation);
                } break;
                case REVERSE: {
                    String linesAfterConversation = ConversationUtil.reverseConversation(readFile);
                    storeAfterConversation(filename, this.locationReverseDir, conversationType, linesAfterConversation);
                } break;
                case FINDROOTS: {
                    String linesAfterConversation = ConversationUtil.findRootsConversation(readFile);
                    storeAfterConversation(filename, this.locationFindRootsDir, conversationType, linesAfterConversation);
                } break;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to read file " + filename, e);
        }
    }

    private void storeAfterConversation(String filename, Path locationDir, ConversationType conversationType, String linesAfterConversation) {
        String newFileName = conversationType.name() + "_" + filename;
        Path newPath = Paths.get(locationDir.getFileName() + "/" + newFileName);
        try {
            Files.writeString(newPath, linesAfterConversation);
        } catch (IOException e) {
            throw new StorageException("Failed to write file " + filename, e);
        }
    }
}
