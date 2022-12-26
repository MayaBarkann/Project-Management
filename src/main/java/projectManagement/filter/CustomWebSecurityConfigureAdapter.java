//package projectManagement.filter;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import projectManagement.service.AuthService;
//import projectManagement.service.PermissionsService;
//
////import projectManagement.filter.PermissionFilter;
//
//@Configuration
////@EnableWebSecurity
//public class CustomWebSecurityConfigureAdapter {
//    private final AuthService authService;
//    private final PermissionsService permissionsService;
//    public static final Logger logger = LogManager.getLogger(CustomWebSecurityConfigureAdapter.class);
//
//    @Autowired
//    public CustomWebSecurityConfigureAdapter(AuthService authService, PermissionsService permissionsService) {
//        System.out.println("CustomWebSecurityConfigureAdapter is created");
//        this.authService = authService;
//        this.permissionsService = permissionsService;
//    }
//
////    @Bean
////    public FilterRegistrationBean<CoreFilter> coreFilterFilterRegistrationBean() {
////        logger.info("creating CoreFilter");
////        FilterRegistrationBean<CoreFilter> registrationBean = new FilterRegistrationBean<>();
////        registrationBean.setFilter(new CoreFilter());
////        registrationBean.addUrlPatterns("/*");
////        registrationBean.setOrder(1);
//////        registrationBean.addUrlPatterns("^((?!/board/filter|/board/create-board|/board/get-board).)*$");
////        return registrationBean;
////    }
//
////    @Bean
////    public FilterRegistrationBean<TokenFilterN> tokenFilterNFilterRegistrationBean() {
////        logger.info("creating TokenFilter");
////        FilterRegistrationBean<TokenFilterN> registrationBean = new FilterRegistrationBean<>();
////        registrationBean.setFilter(new TokenFilterN(authService));
////        registrationBean.addUrlPatterns("/*");
////        registrationBean.setOrder(2);
////        return registrationBean;
////    }
////
////    @Bean
////    public FilterRegistrationBean<PermissionFilter> permissionFilterFilterRegistrationBean() {
////        logger.info("creating PermissionFilter");
////        FilterRegistrationBean<PermissionFilter> registrationBean = new FilterRegistrationBean<>();
////        registrationBean.setFilter(new PermissionFilter(permissionsService));
////        registrationBean.addUrlPatterns("/*");
////        registrationBean.setOrder(3);
//////        registrationBean.addUrlPatterns("^((?!/board/filter|/board/create-board|/board/get-board).)*$");
////        return registrationBean;
////    }
//
//
//
//}