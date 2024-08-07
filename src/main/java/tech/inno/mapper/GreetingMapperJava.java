package tech.inno.mapper;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.inno.GreetingDto;
import tech.inno.GreetingListDto;
import tech.inno.Response;
import tech.inno.entity.GreetingEntity;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface GreetingMapperJava {

    default Response mapListOfGreetingEntitiesToResponse(List<GreetingEntity> greetingEntities) {
        return Response.newBuilder()
                .setGreetingListDto(
                        GreetingListDto.newBuilder()
                                .addAllGreeting(greetingEntities.stream()
                                        .map(this::map)
                                        .collect(Collectors.toList()))
                                .build()
                )
                .build();
    }

    @Mapping(target = "greetingDto", source = "greetingEntity")
    @Mapping(target = "mergeGreetingDto", ignore = true)
    @Mapping(target = "mergeErrorResponse", ignore = true)
    @Mapping(target = "errorResponse", ignore = true)
    @Mapping(target = "mergeResult", ignore = true)
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "mergeRowCountInt", ignore = true)
    @Mapping(target = "mergeRowCountLong", ignore = true)
    @Mapping(target = "rowCountLong", ignore = true)
    @Mapping(target = "rowCountInt", ignore = true)
    @Mapping(target = "mergeGreetingListDto", ignore = true)
    @Mapping(target = "greetingListDto", ignore = true)
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    Response mapGreetingEntityToResponse(GreetingEntity greetingEntity);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "greetingBytes", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    GreetingDto map(GreetingEntity greetingEntity);

    @Mapping(target = "result", source = "result")
    @Mapping(target = "greetingDto", ignore = true)
    @Mapping(target = "mergeGreetingDto", ignore = true)
    @Mapping(target = "mergeErrorResponse", ignore = true)
    @Mapping(target = "errorResponse", ignore = true)
    @Mapping(target = "mergeResult", ignore = true)
    @Mapping(target = "mergeRowCountInt", ignore = true)
    @Mapping(target = "mergeRowCountLong", ignore = true)
    @Mapping(target = "rowCountLong", ignore = true)
    @Mapping(target = "rowCountInt", ignore = true)
    @Mapping(target = "mergeGreetingListDto", ignore = true)
    @Mapping(target = "greetingListDto", ignore = true)
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    Response map(Boolean result);

    @Mapping(target = "value", source = "result")
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    BoolValue mapBooleanToBoolValue(Boolean result);

    @Mapping(target = "rowCountLong", source = "result")
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "greetingDto", ignore = true)
    @Mapping(target = "mergeGreetingDto", ignore = true)
    @Mapping(target = "mergeErrorResponse", ignore = true)
    @Mapping(target = "errorResponse", ignore = true)
    @Mapping(target = "mergeResult", ignore = true)
    @Mapping(target = "mergeRowCountInt", ignore = true)
    @Mapping(target = "mergeRowCountLong", ignore = true)
    @Mapping(target = "rowCountInt", ignore = true)
    @Mapping(target = "mergeGreetingListDto", ignore = true)
    @Mapping(target = "greetingListDto", ignore = true)
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    Response map(Long result);

    @Mapping(target = "value", source = "result")
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    Int64Value mapLongToInt64Value(Long result);

    @Mapping(target = "rowCountInt", source = "result")
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "greetingDto", ignore = true)
    @Mapping(target = "mergeGreetingDto", ignore = true)
    @Mapping(target = "mergeErrorResponse", ignore = true)
    @Mapping(target = "errorResponse", ignore = true)
    @Mapping(target = "mergeResult", ignore = true)
    @Mapping(target = "mergeRowCountInt", ignore = true)
    @Mapping(target = "mergeRowCountLong", ignore = true)
    @Mapping(target = "rowCountLong", ignore = true)
    @Mapping(target = "mergeGreetingListDto", ignore = true)
    @Mapping(target = "greetingListDto", ignore = true)
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    Response map(Integer result);

    @Mapping(target = "value", source = "result")
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    Int32Value mapIntegerToInt32Value(Integer result);

}
