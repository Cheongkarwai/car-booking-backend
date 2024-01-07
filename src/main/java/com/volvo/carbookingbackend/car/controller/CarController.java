package com.volvo.carbookingbackend.car.controller;

import com.volvo.carbookingbackend.car.dto.*;
import com.volvo.carbookingbackend.car.entity.Category;
import com.volvo.carbookingbackend.car.entity.Condition;
import com.volvo.carbookingbackend.car.service.ICarService;
import com.volvo.carbookingbackend.file.service.IFileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final ICarService carService;

    private final IFileService fileService;

    public CarController(ICarService carService,IFileService fileService) {
        this.carService = carService;
        this.fileService = fileService;
    }

    @GetMapping
    public HttpEntity<Page<CarDTO>> findAll(Pageable pageable,
                                            @RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) Condition condition) {
        return ResponseEntity.ok(carService.findAll(pageable,keyword,condition));
    }

    @GetMapping("/features")
    public HttpEntity<Page<CarDTO>> findAllCarFeatures(Pageable pageable){
        return ResponseEntity.ok(carService.findAllCarFeatures(pageable));
    }

    @GetMapping("/conditions")
    public HttpEntity<List<ConditionProjection>> findAllConditions(){
        return ResponseEntity.ok(carService.findAllConditions());
    }

    @GetMapping("/{id}")
    public HttpEntity<CarDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @GetMapping("/{id}/details")
    public HttpEntity<CarDetailsDTO> findCarDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findCarDetailsById(id));
    }

    @GetMapping("/model")
    public HttpEntity<List<CarModelProjection>> findCarsModel(@RequestParam(required = false) Category category,
    @RequestParam(required=false) Condition condition){
        return ResponseEntity.ok(carService.findAllCarsModel(condition));
    }
//    @GetMapping("/brochures")
//    public HttpEntity<List<CarBrochureProjection>> findCarsModel(){
//        return ResponseEntity.ok(carService.findAllCarsBrochures(Category.BROCHURE));
//    }


    @GetMapping("/{modelName}/colors")
    public HttpEntity<List<ColorDTO>> findColorsByModelName(@PathVariable String modelName,@RequestParam Condition condition) {
        return ResponseEntity.ok(carService.findAllColorsByModelName(modelName,condition));
    }

    @PostMapping(path="/used",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUsedCar(@RequestPart("car") UsedCarInput usedCarInput,@RequestPart("car_model_image") MultipartFile carModelImage) throws IOException {
        carService.saveUsedCar(usedCarInput,carModelImage);
    }

    @PostMapping(path="/new",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestPart("car") CarInput carInput,@RequestPart("car_model_image") MultipartFile carModelImage,
                     @RequestPart(value = "exterior_images") MultipartFile [] exteriorImages,
                     @RequestPart(value = "feature_images") MultipartFile [] featureImages,
                     @RequestPart(value = "interior_images") MultipartFile [] interiorImages,
                     @RequestPart(value = "brochure_pdf") MultipartFile  brochure) throws IOException {


       carService.save(carInput,featureImages,exteriorImages,carModelImage,interiorImages,brochure);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        carService.deleteById(id);
    }


}
