package com.vepilef.foodauth.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import jakarta.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer // Configura a aplicação como Authorization Server
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtKeyStoreProperties jwtKeyStoreProperties;

    @Autowired
    private DataSource dataSource;

    // Configura os clients que vão acessar o Resource Server
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // Client é a aplicação que utiliza os recursos do Resource Server utilizando um Access Token
        clients.jdbc(dataSource);
    }

    // Configura acesso ao endpoint de chacagem do token
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.checkTokenAccess("permitAll()")
                .tokenKeyAccess("permitAll()")
                .allowFormAuthenticationForClients(); // Permite passar as credenciais do cliente no corpo da requisição
    }



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        var enhancerChain = new TokenEnhancerChain();
        // O TokenEnhancer customizado deve vir primeiro na lista para ser reconhecido
        enhancerChain.setTokenEnhancers(Arrays.asList(new JwtCustomClaimsTokenEnhance(), jwtAccessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .accessTokenConverter(jwtAccessTokenConverter())
                // Adiciona informaçoes (claims) ao token
                .tokenEnhancer(enhancerChain)
                // Habilita opção de escolha granular de 'scopes' do usuário. Deve ser chamado após accessTokenConverter()
                .approvalStore(approvalStore(endpoints.getTokenStore()))
                .tokenGranter(tokenGranter(endpoints));
    }


    public ApprovalStore approvalStore(TokenStore tokenStore) {
        var approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(tokenStore);

        return approvalStore;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var jwtAccessTokenConverter = new JwtAccessTokenConverter();

        var jksResource = new ClassPathResource(jwtKeyStoreProperties.getPath());
        var keyStorepass = jwtKeyStoreProperties.getPassword(); // Senha para abrir arquivo jks
        var keyPairAlias = jwtKeyStoreProperties.getKeypairAlias(); // Apelido de identificação do par de chaves, pois dentro do arquivo pode haver mais de um par

        var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, keyStorepass.toCharArray());
        var keyPair = keyStoreKeyFactory.getKeyPair(keyPairAlias);

        jwtAccessTokenConverter.setKeyPair(keyPair);

        return jwtAccessTokenConverter;
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(
                endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        var granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, // PKCE
                endpoints.getTokenGranter()); // Demais fluxos granters implementados

        return new CompositeTokenGranter(granters);
    }

}
