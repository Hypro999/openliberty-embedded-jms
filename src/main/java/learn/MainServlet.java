package learn;

import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSProducer;
import jakarta.jms.MapMessage;
import jakarta.jms.Queue;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import learn.utils.RequestCountUtil;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    @Resource(lookup = "jms/libertyQCF")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/libertyQ")
    private Queue queue;

    @Inject
    private RequestCountUtil requestCountService;

    private String getRequestPath(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getMethod());
        sb.append(" ");
        if (req.getContextPath() != null) {
            sb.append(req.getContextPath().trim());
        }
        if (req.getServletPath() != null) {
            sb.append(req.getServletPath().trim());
        }
        if (req.getPathInfo() != null) {
            sb.append(req.getPathInfo().trim());
        }
        return sb.toString();
    }

    private void enqueue_event(HttpServletRequest req) {
        try (JMSContext context = connectionFactory.createContext()) {
            JMSProducer producer = context.createProducer();
            MapMessage message = context.createMapMessage();
            try {
                message.setStringProperty("endpoint", getRequestPath(req));
                message.setIntProperty("requestCount", requestCountService.next());
            } catch (JMSException e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
            producer.send(queue, message);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        enqueue_event(req);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Request acknowledged.");
    }
}
