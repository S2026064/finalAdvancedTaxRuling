package za.gov.sars.atr.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import za.gov.sars.config.DataSourceConfiguration;



public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        initialization(servletContext);
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(DataSourceConfiguration.class);

        ctx.setServletContext(servletContext);
        servletContext.addListener(new ContextLoaderListener(ctx));
        servletContext.addListener(new RequestContextListener());

        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        dynamic.addMapping("*.xhtml");
        dynamic.setLoadOnStartup(1);
    }
    public void initialization(ServletContext servletContext) {
        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "false");
        servletContext.setInitParameter("primefaces.CLIENT_SIDE_LOCALISATION", "false");
        servletContext.setInitParameter("primefaces.COOKIES_SAME_SITE", "strict");
        servletContext.setInitParameter("primefaces.CSP", "false");
        servletContext.setInitParameter("primefaces.CSP_POLICY", "null");
        servletContext.setInitParameter("primefaces.CSP_REPORT_ONLY_POLICY", "null");
        servletContext.setInitParameter("primefaces.DIR", "ltr");
        servletContext.setInitParameter("primefaces.EARLY_POST_PARAM_EVALUATION", "false");
        servletContext.setInitParameter("primefaces.EXCEPTION_TYPES_TO_IGNORE_IN_LOGGING", "null");
        servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
        servletContext.setInitParameter("primefaces.FLEX", "false");
        servletContext.setInitParameter("primefaces.INTERPOLATE_CLIENT_SIDE_VALIDATION_MESSAGES", "false");
        servletContext.setInitParameter("primefaces.LEGACY_WIDGET_NAMESPACE", "false");
        servletContext.setInitParameter("primefaces.MARK_INPUT_AS_INVALID_ON_ERROR_MSG", "false");
        servletContext.setInitParameter("primefaces.MOVE_SCRIPTS_TO_BOTTOM", "false");
        servletContext.setInitParameter("primefaces.MULTI_VIEW_STATE_STORE", "session");
        servletContext.setInitParameter("primefaces.PRIME_ICONS", "true");
        servletContext.setInitParameter("primefaces.RESET_VALUES", "false");
        servletContext.setInitParameter("primefaces.SUBMIT", "full");
        servletContext.setInitParameter("primefaces.THEME", "nova-light");
        servletContext.setInitParameter("primefaces.TOUCHABLE", "true");
        servletContext.setInitParameter("primefaces.TRANSFORM_METADATA", "false");
        servletContext.setInitParameter("primefaces.UPLOADER", "auto");
        servletContext.setInitParameter("net.bootsfaces.blockUI", "true");
        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Production");
        servletContext.setInitParameter("javax.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE", "true");
        servletContext.setInitParameter("com.sun.faces.numberOfViewsInSession", "3");
        servletContext.setInitParameter("com.sun.faces.numberOfLogicalViews", "10");
        servletContext.setInitParameter("BootsFaces_THEME", "default");
        servletContext.setInitParameter("BootsFaces_USETHEME", "cerulean");

    }
}
