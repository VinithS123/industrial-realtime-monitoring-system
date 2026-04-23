package com.example.PRISM.controller;

import com.example.PRISM.dto.MachineDto;
import com.example.PRISM.dto.MachineResponseDto;
import com.example.PRISM.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    @GetMapping("/machines")
    public ResponseEntity<List<MachineDto>> getAllMachines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5" ) int size,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "ASC") String orderAs,
            @RequestParam (defaultValue = "false") boolean fetchAll){

        return ResponseEntity.ok(machineService.getAllMachines(page,size,orderBy,orderAs,fetchAll));
    }

    @PostMapping("/machines")
    public ResponseEntity<MachineDto> addMachine(@RequestBody MachineDto machineDto){
        return ResponseEntity.ok(machineService.addMachine(machineDto));
    }


    @GetMapping("/machines/{id}/latest")
    public ResponseEntity<MachineResponseDto> getLatestInfo(@PathVariable Long id ){
        return ResponseEntity.ok(machineService.getLatestInfo(id));
    }

    @PostMapping("/machines/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id,String status){
        machineService.updateStatus(id,status);
        return ResponseEntity.ok().build();
    }



}
