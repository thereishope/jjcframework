package com.development.plugin.codeCheck.controller;

import com.development.plugin.codeCheck.codeCheck.CodeCheckHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author chenjiajun
 * @title CtrlChcekController
 * @project plugins
 * @date 2019-04-26
 */
@RestController
public class CtrlChcekController {

    @RequestMapping("/ctrl/check")
    public Map getCtrlCheckInfo(){
        return CodeCheckHandler.ctrMap;
    }
}
