//package com.ruoyi.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
///**
// * @Description $功能描述$
// * @Author yufei
// * @Date 2019-05-28 14:55
// **/
//@Configuration
//@EnableAuthorizationServer
//public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
//
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserDetailsService userService;
//
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("test")//客户端ID
//                .authorizedGrantTypes("password", "refresh_token")//设置验证方式
//                .scopes("read", "write")
//                .secret("123456")
//                .accessTokenValiditySeconds(10000) //token过期时间
//                .refreshTokenValiditySeconds(10000); //refresh过期时间
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(tokenStore)
//                .authenticationManager(authenticationManager)
//                .userDetailsService(userService); //配置userService 这样每次认证的时候会去检验用户是否锁定，有效等
//    }
//
//    @Bean
//    public TokenStore tokenStore() {
//        //使用内存的tokenStore
//        return new InMemoryTokenStore();
//    }
//}