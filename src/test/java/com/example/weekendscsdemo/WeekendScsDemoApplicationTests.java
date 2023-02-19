package com.example.weekendscsdemo;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Import({TestChannelBinderConfiguration.class})
@AutoConfigureMockMvc
class WeekendScsDemoApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;

    @Autowired
    WeekendScsDemoApplication weekendScsDemoApplication;

    @Test
    void contextLoads() {
        input.send(new GenericMessage<byte[]>("hello".getBytes()));
        AssertionsForClassTypes.assertThat(output.receive().getPayload()).isEqualTo("HELLO".getBytes());
    }

    @Test
    void httpSendThenSCSReceive()  {
        weekendScsDemoApplication.delegateToSupplier("hello");
        AssertionsForClassTypes.assertThat(output.receive().getPayload()).isEqualTo("HELLO".getBytes());
    }
}
