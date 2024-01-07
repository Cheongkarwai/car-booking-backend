package com.volvo.carbookingbackend.appointment.converter;

import com.volvo.carbookingbackend.appointment.entity.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

//@Component
//public class StatusConverter implements Converter<String[], Status[]> {
//    @Override
//    public Status[] convert(String[] source) {
//        if(source.length == 0) {
//            return null;
//        }
//        Status[] statuses = new Status[source.length];
//        int i = 0;
//        for(String status : source){
//            if(status.equals("")){
//                statuses[i++] = null;
//            }
//            else{
//                statuses[i++] = Status.valueOf(status);
//            }
//        }
//        return statuses;
//    }
//}
