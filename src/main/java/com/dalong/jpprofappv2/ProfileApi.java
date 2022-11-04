package com.dalong.jpprofappv2;

import jpprof.CPUProfiler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@RestController
public class ProfileApi {

    @GetMapping("/debug/pprof/profile")
    public void  demo(HttpServletRequest request, HttpServletResponse response){
        String seconds = request.getParameter("seconds");
        Duration duration = Duration.ofSeconds((long) Integer.parseInt(seconds));
        try {
            response.setHeader("Content-Encoding", "gzip");
            response.setStatus(200);
            CPUProfiler.start(duration, response.getOutputStream());
        } catch (Exception var9) {
            response.setStatus(500);
        } finally {
            System.out.println("finally");
        }
    }
}
