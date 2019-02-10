package pl.ljedrzynski.iparkapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.ljedrzynski.iparkapp.common.serializers.LocalDateTimeDeserializer;
import pl.ljedrzynski.iparkapp.common.serializers.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;
import static pl.ljedrzynski.iparkapp.common.Constants.LONG_DATE_FORMAT;

@Configuration
public class JacksonConfig {

    public static final DateTimeFormatter FORMATTER = ofPattern(LONG_DATE_FORMAT);

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
