/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulval.stetoskop.resources;

import com.dulval.stetoskop.domain.Posology;
import com.dulval.stetoskop.resources.utils.URL;
import com.dulval.stetoskop.services.PosologyService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Diego Dulval
 */
@RestController
@RequestMapping(value = "/posology")
public class PosologyResource {

    @Autowired
    private PosologyService service;

    @GetMapping("/{id}")
    public ResponseEntity find(@PathVariable Integer id) {
        Posology obj = service.readById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity insert(@Valid @RequestBody Posology obj) {
        obj = service.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@Valid @RequestBody Posology obj, @PathVariable Integer id) {
        obj.setId(id);
        service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity read(
            @RequestParam(value = "description", defaultValue = "", required = false) String description,
            @RequestParam(value = "doctor", defaultValue = "0", required = false) Integer doctor,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "doctor") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        String descDecode = URL.decodeParam(description);
        Page<Posology> list = service.readByCriteria(descDecode, doctor, page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }
}
