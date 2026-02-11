package com.mimirandoms.arep1.microspringboot;

import com.mimirandoms.arep1.HttpServer;

public class MicroSpringBoot {
    public static void main(String[] args) {
        try {
            HttpServer.staticfiles("page");
            HttpServer.start(args);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
