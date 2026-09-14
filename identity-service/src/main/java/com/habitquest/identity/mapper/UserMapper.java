package com.habitquest.identity.mapper;

import com.habitquest.identity.dto.RegistrationResponse;
import com.habitquest.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    RegistrationResponse toRegistrationResponse(User user);

}
