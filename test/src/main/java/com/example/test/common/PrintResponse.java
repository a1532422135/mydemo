package com.example.test.common;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

public class PrintResponse {

    public static void main(String[] args) {
        get("http://www.baidu.com", "get", "{}");
    }

    public static String get(String basepath, String method, String jsondata) {
        Response resp = null;
        try {

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(basepath);
            if (method.equals("get")) {
                resp = target.request().accept("application/json").get();
            } else if (method.equals("put")) {
                resp = target.request().accept("application/json").put(Entity.entity(jsondata, MediaType.APPLICATION_JSON_TYPE));
            } else if (method.equals("post")) {
                resp = target.request().accept("application/json").post(Entity.entity(jsondata, MediaType.APPLICATION_JSON_TYPE));
            } else if (method.equals("delete")) {
                resp = target.request().accept("application/json").delete(Response.class);
            }
            if (resp.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + resp.getStatus());
            }

            // ----------------------

            printCollection("getAllowedMethods", resp.getAllowedMethods());
            printCollection("getCookies", resp.getCookies().entrySet());
            printValue("getDate", resp.getDate());
            printValue("getEntityTag", resp.getEntityTag());
            printCollection("getHeaders", resp.getHeaders().entrySet());
            printValue("getLanguage", resp.getLanguage());
            printValue("getLastModified", resp.getLastModified());
            printValue("getLength", resp.getLength());
            printCollection("getLinks", resp.getLinks());
            printValue("getLocation", resp.getLocation());
            printValue("getMediaType", resp.getMediaType());
            printCollection("getMetadata", resp.getMetadata().entrySet());
            printValue("getStatus", resp.getStatus());
            printValue("getStatusInfo", resp.getStatusInfo());
            printCollection("getStringHeaders", resp.getStringHeaders().entrySet());

            // ----------------------
            return resp.readEntity(String.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void printValue(String key, Object obj) {
        System.out.println("\n" + key);
        System.out.println(obj);
    }

    private static void printCollection(String name, Collection<?> collection) {
        System.out.println("\n" + name);
        for (Object o : collection) {
            System.out.println(o);
        }
    }
}
