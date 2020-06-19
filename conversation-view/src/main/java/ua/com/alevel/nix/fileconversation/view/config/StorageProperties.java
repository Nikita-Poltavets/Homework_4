package ua.com.alevel.nix.fileconversation.view.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    private String locationIdentityDir = "identity-dir";
    private String locationSplitDir = "split-dir";
    private String locationReplaceDir = "replace-dir";
    private String locationCountSymbolsDir = "countSymbols-dir";
    private String locationCountWordsDir = "countWords-dir";
    private String locationReverseDir = "reverse-dir";
    private String locationFindRootsDir = "findRoots-dir";

    public String getLocationIdentityDir() {
        return locationIdentityDir;
    }

    public void setLocationIdentityDir(String locationIdentityDir) {
        this.locationIdentityDir = locationIdentityDir;
    }

    public String getLocationSplitDir() {
        return locationSplitDir;
    }

    public void setLocationSplitDir(String locationSplitDir) {
        this.locationSplitDir = locationSplitDir;
    }

    public String getLocationReplaceDir() {
        return locationReplaceDir;
    }

    public void setLocationReplaceDir(String locationReplaceDir) {
        this.locationReplaceDir = locationReplaceDir;
    }

    public String getLocationCountSymbolsDir() {
        return locationCountSymbolsDir;
    }

    public void setLocationCountSymbolsDir(String locationCountSymbolsDir) {
        this.locationCountSymbolsDir = locationCountSymbolsDir;
    }

    public String getLocationCountWordsDir() {
        return locationCountWordsDir;
    }

    public void setLocationCountWordsDir(String locationCountWordsDir) {
        this.locationCountWordsDir = locationCountWordsDir;
    }

    public String getLocationReverseDir() {
        return locationReverseDir;
    }

    public void setLocationReverseDir(String locationReverseDir) {
        this.locationReverseDir = locationReverseDir;
    }

    public String getLocationFindRootsDir() {
        return locationFindRootsDir;
    }

    public void setLocationFindRootsDir(String locationFindRootsDir) {
        this.locationFindRootsDir = locationFindRootsDir;
    }
}
