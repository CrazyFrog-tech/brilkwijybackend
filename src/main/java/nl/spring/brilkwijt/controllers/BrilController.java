package nl.spring.brilkwijt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.spring.brilkwijt.dto.Bril;
import nl.spring.brilkwijt.repos.BrilRepository;

@RestController
@RequestMapping("")
public class BrilController {
	
	@Autowired
	BrilRepository brilRepository;
	
	@PostMapping
	public String create(@RequestBody Bril bril) {
		brilRepository.save(bril);
		return "Bril is created";
	}
    @GetMapping("/brillen")
    public List<Bril> getAllBrillen() {
        List<Bril> brillen = brilRepository.findAll();
        return brillen;
    }

    @PostMapping("/description")
    public Bril postDescription(@RequestBody Bril bril) {
        return brilRepository.save(bril);

    }

}
