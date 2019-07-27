package com.igorkhromov.controller;

public class BaseController {

    private static final String REDIRECT_INSTRUCTION = "redirect:";

    String redirect(String uri) {
        return REDIRECT_INSTRUCTION.concat(uri);
    }
}
