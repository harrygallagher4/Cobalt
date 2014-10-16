package com.stlmissouri.cobalt.plugins.repo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Stl
 * Date: 4/12/2014
 * Time: 2:10 AM
 * Use:  Stores repository information allows generation of package URLs
 */
public class RepositoryInfo {

    @Expose
    @SerializedName("repo")
    private String name;
    @Expose
    @SerializedName("URL")
    private String url;
    @Expose
    @SerializedName("package-format")
    private String packagePath;
    @Expose
    @SerializedName("download-format")
    private String downloadPath;
    @Expose
    @SerializedName("available-packages")
    private List<String> packages;

    public RepositoryInfo(String name, String url, String packagePath, String downloadPath) {
        this.name = name;
        this.url = url;
        this.packagePath = packagePath;
        this.downloadPath = downloadPath;
        this.packages = new ArrayList<>();
        while (this.url.endsWith("/")) {
            this.url = this.url.substring(0, this.url.length() - 1);
        }
    }

    public URL locatePackage(String pkg) throws MalformedURLException {
        if(!this.packages.contains(pkg)) {
            System.out.println(pkg);
            for(String s : this.packages) {
                System.out.println(s);
            }
            return null;
        }
        return new URL(getPackagePath(pkg));
    }

    public String getPackagePath(String pkg) {
        return this.url + this.format(this.packagePath, pkg, "");
    }

    public String getDownloadPath(String pkg, String version) {
        return this.url + this.format(this.downloadPath, pkg, version);
    }

    private String format(String input, String pkg, String version) {
        return input.replaceAll("\\{package\\}", pkg).replaceAll("\\{version\\}", version);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public List<String> getPackages() {
        return packages;
    }
}
