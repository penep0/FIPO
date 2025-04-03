package springproject.financeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springproject.financeproject.service.StockService;

@RestController
@RequiredArgsConstructor
public class StockApiController {

    @Autowired
    private final StockService stockService;


}
