package com.stlmissouri.cobalt.plugins.repo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: Stl
 * Date: 4/8/2014
 * Time: 12:26 AM
 * Use:  Used to download plugins from external sources
 */
public class PluginRepository {

    private URL url;
    private RepositoryInfo repositoryInfo;

    public PluginRepository(String url) throws MalformedURLException {
        this(new URL(url));
    }

    public PluginRepository(URL url) {
        this.url = url;
    }

    public void index() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setUseCaches(false);
        connection.setDoInput(true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder buffer = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            buffer.append(str).append("\r\n");
        }
        reader.close();
        String content = buffer.toString();
        System.out.println(content);
        RepositoryInfo info = gson.fromJson(content, RepositoryInfo.class);
        this.repositoryInfo = info;
    }

    public PackageInfo getPackage(String pkg) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HttpURLConnection connection = (HttpURLConnection) this.repositoryInfo.locatePackage(pkg).openConnection();
        connection.setUseCaches(false);
        connection.setDoInput(true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder buffer = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            buffer.append(str).append("\r\n");
        }
        reader.close();
        String content = buffer.toString();
        System.out.println(content);
        PackageInfo info = gson.fromJson(content, PackageInfo.class);
        info.setParent(this);
        return info;
    }

    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

}
