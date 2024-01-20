package com.volvo.carbookingbackend.car.service;
import com.volvo.carbookingbackend.car.entity.*;
import com.volvo.carbookingbackend.car.mapper.*;
import com.volvo.carbookingbackend.exception.CarNotFoundException;
import com.volvo.carbookingbackend.car.dto.*;
import com.volvo.carbookingbackend.car.repository.CarDetailsRepository;
import com.volvo.carbookingbackend.car.repository.CarRepository;
import com.volvo.carbookingbackend.car.specification.CarSpecification;
import com.volvo.carbookingbackend.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CarService implements  ICarService{
    private final CarRepository carRepository;

    private final CarDetailsRepository carDetailsRepository;

    private final IFileService fileService;

    private final CarMapper carMapper;

    private final ImageMapper imageMapper;

    private final CarDetailsMapper carDetailsMapper;

    private final ImageDTOMapper imageDTOMapper;

    private final UsedCarMapper usedCarMapper;


//    private final Function<Car,CarDTO> carDTOConversion = car-> CarDTO.builder().id(car.getId())
//            .features(car.getFeatures().stream().map(featureDTOFunction).collect(Collectors.toList()))
//            .model(car.getModel())
//            .brand(car.getBrand())
//            .build();

    private final Function<Colors,ColorDTO> colorDTOFunction = color-> ColorDTO.builder()
            .name(color.getName())
            .code(color.getCode())
            .build();

    public CarService(CarRepository carRepository, CarDetailsRepository carDetailsRepository,
                      IFileService fileService,CarMapper carMapper,ImageMapper imageMapper,
                      CarDetailsMapper carDetailsMapper, ImageDTOMapper imageDTOMapper,
                      UsedCarMapper usedCarMapper){
        this.carRepository = carRepository;
        this.carDetailsRepository = carDetailsRepository;
        this.fileService = fileService;
        this.carMapper = carMapper;
        this.imageMapper = imageMapper;
        this.carDetailsMapper = carDetailsMapper;
        this.imageDTOMapper = imageDTOMapper;
        this.usedCarMapper = usedCarMapper;
    }
    public Page<CarDTO> findAll(Pageable pageable,String keyword,Condition condition){

        Specification<Car> carSpecification = Specification.where(null);

        if(StringUtils.hasText(keyword)){
            carSpecification = CarSpecification.hasKeyword(keyword);
        }

        if(Objects.nonNull(condition)){
            carSpecification = CarSpecification.hasCondition(condition);
        }
        return carRepository.findAll(carSpecification, Objects.requireNonNullElse(pageable, Pageable.ofSize(Integer.MAX_VALUE))).map(carMapper::toCarDTO);
    }

    public Page<CarDTO> findAllCarFeatures(Pageable pageable){
        if(Objects.isNull(pageable)){
            return carRepository.findAllCarFeatures(Pageable.ofSize(Integer.MAX_VALUE)).map(carMapper::toCarDTO);
        }
        return carRepository.findAllCarFeatures(pageable).map(carMapper::toCarDTO);
    }

    @Override
    public CarDTO findById(Long id) {
        Car car =  carRepository.findById(id).orElseThrow(()->new CarNotFoundException("Car not found"));
        return this.carMapper.toCarDTO(car);
    }

    @Override
    public List<CarModelProjection> findAllCarsModel(Condition condition) {
        return carRepository.findAllCarsModel(condition);
    }

    @Override
    public CarDTO findByModelName(String modelName) {
        Car car =  carRepository.findByModel(modelName).orElseThrow(()->new CarNotFoundException("Car not found"));
        return this.carMapper.toCarDTO(car);
    }

    @Override
    public List<ColorDTO> findAllColorsByModelName(String modelName, Condition condition) {
        return carDetailsRepository.findAllByCarModelAndCondition(modelName,condition).stream()
                .map(carDetails -> colorDTOFunction.apply(carDetails.getColors()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(CarInput carInput) {
        carRepository.save(carMapper.toCar(carInput));
    }

    @Override
    public void saveUsedCar(UsedCarInput usedCarInput,MultipartFile carModelFile) throws IOException {
//        List<String> carModelImagePaths = fileService.uploadFile(usedCarInput.model(),Category.CAR_MODEL.getName(),carModelFile);
//        Car car = usedCarMapper.toUsedCar(usedCarInput);
//        car.setModelImage(carModelImagePaths.get(0));
//        carRepository.save(car);
    }
    @Override
    public void save(CarInput carInput, MultipartFile[] featureFiles, MultipartFile[] exteriorFiles, MultipartFile carModelFile,
                     MultipartFile [] interiorFiles,MultipartFile brochure) throws IOException {

       switch(carInput.condition()){
           case USED -> {
               List<String> carModelImagePaths = fileService.uploadFile(carInput.model(),Category.CAR_MODEL.getName(),carModelFile);
               Car car = usedCarMapper.toUsedCar(carInput);
               car.setModelImage(carModelImagePaths.get(0));
               carRepository.save(car);
           }
           case NEW -> {
               List<String> carModelImagePaths = fileService.uploadFile(carInput.model(),Category.CAR_MODEL.getName(),carModelFile);
               List<String> brochurePdfPath = fileService.uploadFile(carInput.model(),Category.BROCHURE.getName(),brochure);
               List<String> exteriorImagePaths = fileService.uploadFile(carInput.model(),Category.EXTERIOR.getName(),exteriorFiles);
               List<String> featureImagePaths = fileService.uploadFile(carInput.model(),Category.FEATURE.getName(),featureFiles);
               List<String> interiorImagesPath = fileService.uploadFile(carInput.model(),Category.INTERIOR.getName(),interiorFiles);

               Car car = carMapper.toCar(carInput);
               List<Image> exteriorImages = imageMapper.toImages(exteriorImagePaths,Category.EXTERIOR);
               List<Image> featureImages = imageMapper.toImages(featureImagePaths,Category.FEATURE);
               List<Image> interiorImages = imageMapper.toImages(interiorImagesPath,Category.INTERIOR);

               car.addImages(exteriorImages);
               car.addImages(interiorImages);

               int count = 0;

               for(Feature feature : car.getFeatures()){
                   feature.setImage(featureImages.get(count));
                   count++;
               }

               car.setBrochurePdf(brochurePdfPath.get(0));
               car.setModelImage(carModelImagePaths.get(0));

               carRepository.save(car);
           }
       }
    }

    @Override
    public List<ConditionProjection> findAllConditions() {
        return carRepository.findDistinctCondition();
    }


    @Override
    public void deleteById(Long id){
        Car car = carRepository.findById(id).orElseThrow(()->new CarNotFoundException("Car not found"));
        List<String> filePaths = Arrays.asList(car.getModelImage(),car.getBrochurePdf());
        fileService.deleteFiles(car.getImages().stream().map(image->image.getPath()).collect(Collectors.toList()));
        fileService.deleteFiles(filePaths);
        carRepository.delete(car);
        //        carRepository.deleteById(id);
    }

    @Override
    public CarDetailsDTO findCarDetailsById(Long id) {

        Car car =  carRepository.findById(id).orElseThrow(()->new CarNotFoundException("Car not found"));
        CarDetailsDTO carDetailsDTO = this.carDetailsMapper.toCarDetailsDTO(car);

        List<ImageDTO> exteriorImageDTOs = imageDTOMapper.toImagesDTO(car.getImages(),Category.EXTERIOR);
        List<ImageDTO> interiorImageDTOs = imageDTOMapper.toImagesDTO(car.getImages(),Category.INTERIOR);

        carDetailsDTO.setExteriorImages(exteriorImageDTOs);
        carDetailsDTO.setInteriorImages(interiorImageDTOs);

        return carDetailsDTO;
    }

    @Override
    public void update(Long id, CarInput carInput, MultipartFile[] featureFiles, MultipartFile[] exteriorFiles, MultipartFile carModelFiles,
                       MultipartFile [] interiorFiles,MultipartFile brochure) throws IOException {

        Car car = carRepository.findById(id).orElseThrow(()->new CarNotFoundException("Car not found"));

        List<String> carModelImagePaths = fileService.uploadFile(carInput.model(),Category.CAR_MODEL.getName(),carModelFiles);
        List<String> brochurePdfPath = fileService.uploadFile(carInput.model(),Category.BROCHURE.getName(),brochure);
        List<String> exteriorImagePaths = fileService.uploadFile(carInput.model(),Category.EXTERIOR.getName(),exteriorFiles);
        List<String> featureImagePaths = fileService.uploadFile(carInput.model(),Category.FEATURE.getName(),featureFiles);
        List<String> interiorImagesPath = fileService.uploadFile(carInput.model(),Category.INTERIOR.getName(),interiorFiles);
        carMapper.update(carInput,car);
        List<Image> exteriorImages = imageMapper.toImages(exteriorImagePaths,Category.EXTERIOR);
        List<Image> featureImages = imageMapper.toImages(featureImagePaths,Category.FEATURE);
        List<Image> interiorImages = imageMapper.toImages(interiorImagesPath,Category.INTERIOR);

        car.addImages(exteriorImages);
        car.addImages(interiorImages);

        int count = 0;

        for(Feature feature : car.getFeatures()){
            feature.setImage(featureImages.get(count));
            count++;
        }

        car.setBrochurePdf(brochurePdfPath.get(0));
        car.setModelImage(carModelImagePaths.get(0));

        carRepository.save(car);
    }
}
