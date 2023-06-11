package nl.spring.brilkwijt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.spring.brilkwijt.dto.Bril;
import nl.spring.brilkwijt.repos.BrilRepository;

@Controller
@RequestMapping("/api/bril")
public class BrilController {
	
	@Autowired
	BrilRepository brilRepository;
	
	@PostMapping
	public String create(@RequestBody Bril bril) {
		brilRepository.save(bril);
		return "Bril is created";
	}
    @GetMapping
    public List<Bril> getAllBrillen() {
        List<Bril> brillen = brilRepository.findAll();
        return brillen;
    }

}
