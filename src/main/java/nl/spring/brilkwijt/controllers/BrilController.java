package nl.spring.brilkwijt.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.spring.brilkwijt.repos.BrilRepository;
import nl.spring.brilkwijt.repos.dto.Bril;

@CrossOrigin
@RestController
@RequestMapping("")
public class BrilController {

    @Autowired
    BrilRepository brilRepository;

    @Value("${image.upload.path}")
    private String imageUploadPath;

    @PostMapping
    public String create(@RequestBody Bril bril) {
        brilRepository.save(bril);
        return "Bril is created";
    }

    @GetMapping("/brillen")
    public List<Bril> getAllBrillen() {
        List<Bril> brillen = brilRepository.findAll();
        List<Bril> toReturnBrillen = new ArrayList<>();
        for (Bril bril : brillen) {
            Bril tempBril = new Bril();
            tempBril.setId(bril.getId());
            tempBril.setTitel(bril.getTitel());
            tempBril.setAddress(bril.getAddress());
            tempBril.setLostAtDate(bril.getLostAtDate());
            tempBril.setBrand(bril.getBrand());
            tempBril.setCustomer(bril.getCustomer());
            if (!bril.getImageFilenames().isEmpty()) {
                tempBril.setImageFilenames(Collections.singletonList(bril.getImageFilenames().get(0)));
            }
            toReturnBrillen.add(tempBril);
        }

        return toReturnBrillen;
    }

    @GetMapping("/bril/{id}")
    public ResponseEntity<Bril> getBril(@PathVariable("id") String id) {
        long idLongy = Long.parseLong(id);
        Optional<Bril> brilById = this.brilRepository.findById(idLongy);
        return brilById.map(bril -> ResponseEntity.ok().body(bril)).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/brilImage")
    public ResponseEntity<Resource> getBrilImage(@RequestParam("imageName") String imageName) {
        Resource resource = new FileSystemResource(imageName);
        if (resource.exists()) {
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Adjust content type as needed
                .body(resource);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/description", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Bril postDescription(@RequestPart String bril, @RequestPart List<MultipartFile> images) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Bril realBril = mapper.readValue(bril, Bril.class);
        Bril savedBril = brilRepository.save(realBril);
        List<String> imageFileNames = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageFileName = "/images/" + savedBril.getId() + "_" + image.getOriginalFilename();
            Path imagePath = Path.of(imageUploadPath + imageFileName);
            if (!Files.exists(imagePath)) {
                Files.createDirectories(imagePath);
            }
            //write data to file
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            imageFileNames.add(imagePath.toString());
        }
        savedBril.setImageFilenames(imageFileNames);
        brilRepository.save(savedBril);
        return savedBril;
    }
}
