package com.lab9.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration reader that loads environment-specific properties.
 */
public final class ConfigReader {

    private static volatile ConfigReader instance;

    private final Properties properties;
    private final String env;

    private ConfigReader(String env) {
        this.env = env;
        this.properties = new Properties();
        loadConfig();
    }

    public static ConfigReader getInstance() {
        String activeEnv = System.getProperty("env", "dev").trim().toLowerCase();

        if (instance == null || !instance.env.equals(activeEnv)) {
            synchronized (ConfigReader.class) {
                if (instance == null || !instance.env.equals(activeEnv)) {
                    instance = new ConfigReader(activeEnv);
                }
            }
        }

        return instance;
    }

    public String getBaseUrl() {
        return getRequired("base.url");
    }

    public int getExplicitWait() {
        return Integer.parseInt(getRequired("explicit.wait"));
    }

    public int getRetryCount() {
        return Integer.parseInt(getRequired("retry.count"));
    }

    public String getStandardUsername() {
        return getRequired("login.standard.username");
    }

    public String getStandardPassword() {
        return getRequired("login.standard.password");
    }

    public String getLockedOutUsername() {
        return getRequired("login.locked.username");
    }

    public String getInvalidPassword() {
        return getRequired("login.invalid.password");
    }

    private void loadConfig() {
        String fileName = switch (env) {
            case "dev" -> "config-dev.properties";
            case "staging" -> "config-staging.properties";
            default -> throw new IllegalArgumentException("Unsupported environment: " + env);
        };

        System.out.println("Đang dùng môi trường: " + env);

        try (InputStream inputStream = Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Cannot find config file: " + fileName);
            }

            properties.load(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration from " + fileName, ex);
        }
    }

    private String getRequired(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required config key: " + key);
        }
        return value.trim();
    }
}
