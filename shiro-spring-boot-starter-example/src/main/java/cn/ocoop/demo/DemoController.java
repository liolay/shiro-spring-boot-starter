package cn.ocoop.demo;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/a")
    public void a() {
    }

    @RequestMapping("/b")
    public void b() {
    }


    @RequiresPermissions("permissionC")
    @RequestMapping("/c")
    public void c() {
    }

    @RequiresGuest
    @RequestMapping("/d")
    public void d() {
    }

}
