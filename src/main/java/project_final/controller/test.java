package project_final.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/t")
public class test {
    @GetMapping
    public String h(){
        return "/t";
    }

    @GetMapping("/data")
    @ResponseBody
    public String getData() {
        return "Hello from the server!";
    }

    @PostMapping("/process")
    @ResponseBody
    public String processData(@RequestBody String inputData) {
        // Xử lý dữ liệu được gửi từ client (POST request)
        // Trong ví dụ này, chúng ta chỉ trả về dữ liệu đã nhận được.
        return "Received data: " + inputData;
    }
}
