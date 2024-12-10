package com.quyetdw.winmall.controller;

import com.quyetdw.winmall.model.Deal;
import com.quyetdw.winmall.response.ApiResponse;
import com.quyetdw.winmall.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal){

        Deal createDeals = dealService.createDeal(deal);
        return new ResponseEntity<>(createDeals, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id,
                                           @RequestBody Deal deal)throws Exception{
        Deal updateDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updateDeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception{
        dealService.deleteDeal(id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Xóa Deal thành công!");

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}
