package com.xcy.controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 * 生成验证码的 Controller
 */
@RestController
public class VerifyCodeController {

    // 生成验证码的 制造者（生产者）
    private final Producer producer;

    // 构造注入
    @Autowired
    public VerifyCodeController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/vc.jpg")
    public String getVerifyCode(HttpSession session) throws IOException {
        //1.生成验证码
        String text = producer.createText();
        //2.放入 session redis 实现
        session.setAttribute("kaptcha", text);
        //3.生成图片
        BufferedImage bi = producer.createImage(text);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        // 将当前的 BufferedImage 以jpg 的格式写入的 fos流中 FastByteArrayOutputStream
        ImageIO.write(bi, "jpg", fos);
        //4.返回 base64
        return Base64.encodeBase64String(fos.toByteArray());
    }


    public Producer getProducer() {
        return producer;
    }
}
