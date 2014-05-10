package web.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import web.accounts.AccountService;
import web.messagesystem.MessageSystem;
import web.frontend.Frontend;
import web.resourcesystem.Configuration;
import web.resourcesystem.ResourceFactory;

/**
 * @author d.kildishev
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = runWebServer();

        if (server == null) {
            return;
        }

        server.join();
    }

    public static Server runWebServer() throws Exception {
        ResourceFactory resourceFactory = ResourceFactory.instance("resources/");
        Configuration configuration = (Configuration)resourceFactory.getResource("config.xml");

        if (configuration == null) {
            return null;
        }

        MessageSystem ms = new MessageSystem();
        AccountService accountService1 = new AccountService(ms, "hibernate.cfg.xml");
        AccountService accountService2 = new AccountService(ms, "hibernate.cfg.xml");
        Frontend frontend = new Frontend(ms);

        (new Thread(frontend)).start();
        (new Thread(accountService1)).start();
        (new Thread(accountService2)).start();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(Integer.parseInt(configuration.port));

        server.setHandler(handlers);
        server.start();

        return server;
    }
}
