package fdconfiguration.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.util.backoff.FixedBackOff;

import javax.security.auth.DestroyFailedException;
import java.io.IOException;

@Configuration

public class DltConfig {

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate);
    }

    @Bean
    public CommonErrorHandler commonErrorHandler(DeadLetterPublishingRecoverer recoverer) {

        FixedBackOff fixedBackOff = new FixedBackOff(3000L, 3);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);

        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        errorHandler.addNotRetryableExceptions(IOException.class);
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
        errorHandler.addNotRetryableExceptions(IndexOutOfBoundsException.class);

        errorHandler.addNotRetryableExceptions(DestroyFailedException.class);
        errorHandler.addNotRetryableExceptions(ConversionException.class);
        errorHandler.addNotRetryableExceptions(NoSuchMethodException.class);
        errorHandler.addNotRetryableExceptions(ClassCastException.class);
        errorHandler.addNotRetryableExceptions(MessageConversionException.class);
        errorHandler.addNotRetryableExceptions(DeserializationException.class);


        return errorHandler;
    }

}
