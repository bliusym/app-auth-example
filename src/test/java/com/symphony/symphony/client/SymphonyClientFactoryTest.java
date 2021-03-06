package com.symphony.symphony.client;

import com.symphony.example.pods.PodDirectory;
import com.symphony.example.pods.PodInfo;
import feign.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link SymphonyClientFactory}
 *
 * Created by Dan Nathanson on 1/2/17.
 */
public class SymphonyClientFactoryTest {

    private SymphonyClientFactory symphonyClientFactory;

    private PodDirectory podDirectory;


    @Before
    public void setup() {
        podDirectory = mock(PodDirectory.class);
        Client httpClient = mock(Client.class);
        HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
        when(httpClientBuilder.buildClient()).thenReturn(httpClient);
        symphonyClientFactory = new SymphonyClientFactory(httpClientBuilder, podDirectory);
    }

    /**
     * Test getting an AuthenticationClient for a pod.  Subsequent calls with same pod ID should return same object.
     */
    @Test
    public void testGetAuthenticationClient() {

        PodInfo podInfo = new PodInfo();
        podInfo.setPodId("pod-id");
        podInfo.setPodHost("pod-host");
        when(podDirectory.getPodInfo("pod-id")).thenReturn(podInfo);

        PodInfo anotherPodInfo = new PodInfo();
        anotherPodInfo.setPodId("another-pod-id");
        anotherPodInfo.setPodHost("another-pod-host");
        when(podDirectory.getPodInfo("another-pod-id")).thenReturn(anotherPodInfo);


        AuthenticationClient authenticationClient = symphonyClientFactory.getAuthenticationClient("pod-id");
        assertNotNull("Should return something", authenticationClient);

        AuthenticationClient anotherAuthenticationClient = symphonyClientFactory.getAuthenticationClient("another-pod-id");
        assertNotNull("Should return something", anotherAuthenticationClient);

        assertNotSame("Should be different objects", authenticationClient, anotherAuthenticationClient);

        AuthenticationClient sameAuthenticationClient = symphonyClientFactory.getAuthenticationClient("pod-id");

        assertSame("Should return same object", authenticationClient, sameAuthenticationClient);




    }


}