package com.stlmissouri.cobalt.plugins.repo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * User: Stl
 * Date: 4/8/2014
 * Time: 12:27 AM
 * Use:  Serializable plugin object for downloading plugins
 */
public class PackageInfo {

    @SerializedName("package")
    @Expose
    private String packageName;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("depend")
    @Expose
    private String[] dependencies;
    @SerializedName("latest-version")
    @Expose
    private String latestVersion;

    private PluginRepository parent;

    public PackageInfo(String packageName, String author, String[] dependencies, String latestVersion, PluginRepository parent) {
        this.packageName = packageName;
        this.author = author;
        this.dependencies = dependencies;
        this.latestVersion = latestVersion;
        this.parent = parent;
    }

    public void downloadLatestVersion(File target) throws IOException {
        this.downloadVersion(target, this.latestVersion);
    }

    public void downloadVersion(File target, String version) throws IOException {
        FileUtils.copyURLToFile(new URL(this.getVersionURL(version)), target, 10000, 20000);
    }

    public PluginRepository getParent() {
        return parent;
    }

    public String getLatestVersionURL() {
        return this.getVersionURL(this.latestVersion);
    }

    public String getVersionURL(String version) {
        return this.parent.getRepositoryInfo().getDownloadPath(this.packageName, version);
    }

    public void setParent(PluginRepository parent) {
        this.parent = parent;
    }
}