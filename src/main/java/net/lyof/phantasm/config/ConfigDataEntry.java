package net.lyof.phantasm.config;

public class ConfigDataEntry {
    private final ConfigEntry<Boolean> entry;
    private String filePath;
    private String fileContent;

    public ConfigDataEntry(String entryPath, String filePath, String fileContent) {
        this.entry = new ConfigEntry<>(entryPath, true);
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    public void reload() {}
}
