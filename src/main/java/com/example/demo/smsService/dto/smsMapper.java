package com.example.demo.smsService.dto;

import com.example.demo.smsService.entity.smsEntity;

public class smsMapper {
    /**
     * use builder pattern here instead of constructors
     * @param smsDTO
     * @return
     */
    public static smsEntity mapToEntity(smsDTO smsDTO) {
//        smsEntity smsentity = new smsEntity(
//                smsDTO.getId(),
//                smsDTO.getPhone_number(),
//                smsDTO.getMessage(),
//                smsDTO.getStatus(),
//                smsDTO.getFaliure_code(),
//                smsDTO.getFaliure_comment(),
//                smsDTO.getCreated_at(),
//                smsDTO.getUpdated_at()
//        );
        smsEntity smsentity = new smsEntity().builder()
                .id(smsDTO.getId())
                .phone_number(smsDTO.getPhone_number())
                .message(smsDTO.getMessage())
                .status(smsDTO.getStatus())
                .faliure_code(smsDTO.getFaliure_code())
                .faliure_comment(smsDTO.getFaliure_comment())
                .created_at(smsDTO.getCreated_at())
                .updated_at(smsDTO.getUpdated_at())
                .build();

         return smsentity;
    }

    public static smsDTO mapToDTO(smsEntity smsentity) {
        return new smsDTO(
                smsentity.getId(),
                smsentity.getPhone_number(),
                smsentity.getMessage(),
                smsentity.getStatus(),
                smsentity.getFaliure_code(),
                smsentity.getFaliure_comment(),
                smsentity.getCreated_at(),
                smsentity.getUpdated_at()
        );
    }
}
