package nl.spring.brilkwijt.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import nl.spring.brilkwijt.repos.BrilRepository;
import nl.spring.brilkwijt.repos.dto.Bril;

@CrossOrigin
@RestController
@RequestMapping("")
public class BrilController {

    @Autowired
    BrilRepository brilRepository;

    @Value("${google.project.id}")
    private String projectId;

    @Value("${google.bucket.name}")
    private String bucketName;

    public Storage getStorage() {
        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/credentials/storage.json"));
            System.out.println("Credentials loaded" + credentials.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage() + " credentials cannot be loaded");
        }

        return StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
    }

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
            if (!bril.getImageBlobIds().isEmpty()) {
                tempBril.setImageBlobIds(Collections.singletonList(bril.getImageBlobIds().get(0)));
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
    public ResponseEntity<byte[]> getBrilImage(@RequestParam("imageName") String imageName) {
        byte[] image = downloadImage(imageName);
        if (image != null) {
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Adjust content type as needed
                .body(image);
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
            BlobId imageBlobId = uploadImage(imageFileName, image);
            if (imageBlobId != null && !imageBlobId.getName().isEmpty())
                imageFileNames.add(imageFileName);
        }
        savedBril.setImageBlobIds(imageFileNames);
        brilRepository.save(savedBril);
        return savedBril;
    }

    private BlobId uploadImage(String imageName, MultipartFile image) {
        BlobId blobId = BlobId.of(bucketName, imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            Storage storage = this.getStorage();
            storage.create(blobInfo, image.getBytes());
            return blobId;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return blobId;
    }

    private byte[] downloadImage(String imageName) {
        BlobId blobId = BlobId.of(bucketName, imageName);
        Storage storage = this.getStorage();
        Blob blob = storage.get(blobId);
        return blob.getContent();
    }
}
