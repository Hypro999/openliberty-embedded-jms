package learn.consumers;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/libertyQ")
})
public class RequestCountReporter implements MessageListener {
    public RequestCountReporter() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            String endpoint = message.getStringProperty("endpoint");
            int requestCount = message.getIntProperty("requestCount");
            System.out.println(String.format("Request count for %s: %d", endpoint, requestCount));
        } catch (JMSException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
