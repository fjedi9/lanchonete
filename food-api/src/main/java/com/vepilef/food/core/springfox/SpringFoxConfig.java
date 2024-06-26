package com.vepilef.food.core.springfox;

import com.fasterxml.classmate.TypeResolver;
import com.vepilef.food.api.exceptionhandler.Problem;
import com.vepilef.food.api.v1.model.*;
import com.vepilef.food.api.v1.openapi.model.*;
import com.vepilef.food.api.v2.model.CidadeModelV2;
import com.vepilef.food.api.v2.model.CozinhaModelV2;
import com.vepilef.food.api.v2.openapi.model.CidadesModelV2OpenApi;
import com.vepilef.food.api.v2.openapi.model.CozinhasModelV2OpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {

    // Registrando uma instância de Docket (sumário) como um Componente Spring
    @Bean
    public Docket apiDocketV1() {
        var typeResolver = new TypeResolver();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("V1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vepilef.food.api"))
                .paths(PathSelectors.ant("/v1/**"))
                .build()
                .useDefaultResponseMessages(false) // Desabilitando httpStatus codes error padrão
                .globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
                .globalResponseMessage(RequestMethod.POST, globalPostResponseMessages())
                .globalResponseMessage(RequestMethod.PUT, globalPutResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
                .additionalModels(typeResolver.resolve(Problem.class)) // Adicionando Modelo extra para ser exibido em Models
                .directModelSubstitute(Pageable.class, PageableModelOpenApi.class) // Para exibir os parâmetros recebidos em um Pageable
                .directModelSubstitute(Links.class, LinksModelOpenApi.class) // Substituindo Links do HATEOAS
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(PagedModel.class, CozinhaModel.class),
                        CozinhasModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(PagedModel.class, PedidoResumoModel.class),
                        PedidosResumoModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, CidadeModel.class),
                        CidadesModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, EstadoModel.class),
                        EstadosModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, FormaPagamentoModel.class),
                        FormasPagamentoModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, GrupoModel.class),
                        GruposModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, PermissaoModel.class),
                        PermissoesModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, ProdutoModel.class),
                        ProdutosModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, RestauranteBasicoModel.class),
                        RestaurantesBasicoModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, UsuarioModel.class),
                        UsuarioModelOpenApi.class))

                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()))

                .apiInfo(apiInfoV1())
                .ignoredParameterTypes(
                        ServletWebRequest.class, // Parâmetro injetado pelo Spring, não inserido pelo usuário, desnecessário na doc
                        URL.class, URI.class, URLStreamHandler.class, // Ignorando a descrição destes tipos de "Model"
                        Resource.class, File.class, InputStream.class)
                .tags(new Tag("Cidades", "Gerencia as cidades"))
                .tags(new Tag("Grupos", "Gerencia os grupos de usuários"))
                .tags(new Tag("Cozinhas", "Gerencia as cozinhas"))
                .tags(new Tag("Formas de pagamento", "Gerencia as formas de pagamento"))
                .tags(new Tag("Pedidos", "Gerencia os pedidos"))
                .tags(new Tag("Restaurantes", "Gerencia os restaurantes"))
                .tags(new Tag("Estados", "Gerencia os estados"))
                .tags(new Tag("Produtos", "Gerencia os produtos"))
                .tags(new Tag("Usuários", "Gerencia os usuários"))
                .tags(new Tag("Estatísticas", "Estatísticas do Food"))
                .tags(new Tag("Permissões", "Gerencia as permissões"));
    }

    @Bean
    public Docket apiDocketV2() {
        var typeResolver = new TypeResolver();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("V2")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vepilef.food.api"))
                .paths(PathSelectors.ant("/v2/**"))
                .build()
                .useDefaultResponseMessages(false) // Desabilitando httpStatus codes error padrão
                .globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
                .globalResponseMessage(RequestMethod.POST, globalPostResponseMessages())
                .globalResponseMessage(RequestMethod.PUT, globalPutResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
                .additionalModels(typeResolver.resolve(Problem.class)) // Adicionando Modelo extra para ser exibido em Models
                .directModelSubstitute(Pageable.class, PageableModelOpenApi.class) // Para exibir os parâmetros recebidos em um Pageable
                .directModelSubstitute(Links.class, LinksModelOpenApi.class) // Substituindo Links do HATEOAS

                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, CidadeModelV2.class),
                        CidadesModelV2OpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, CozinhaModelV2.class),
                        CozinhasModelV2OpenApi.class))

                .apiInfo(apiInfoV2())
                .tags(new Tag("Cidades", "Gerencia as cidades"),
                        new Tag("Cozinhas", "Gerencia as Cozinhas"));
    }

    public SecurityScheme securityScheme() {
        return new OAuthBuilder()
                .name("vepilefFood")
                .grantTypes(grantTypes())
                .scopes(scopes())
                .build();
    }

    private SecurityContext securityContext() {
        var securityReference = SecurityReference.builder()
                .reference("vepilefFood") // Referencia o Scheme de segurança utilizado (mesmo nome do securityScheme acima)
                .scopes(scopes().toArray(new AuthorizationScope[0]))
                .build();

        return SecurityContext.builder()
                .securityReferences(Arrays.asList(securityReference))
                .forPaths(PathSelectors.any()) // Para quais paths o SecurityScheme será utilizado (todos)
                .build();
    }

    private List<GrantType> grantTypes() {
        return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
    }

    // Lista de Scopes disponíveis para utilização pela página de documentação
    private List<AuthorizationScope> scopes() {
        return Arrays.asList(
                new AuthorizationScope("READ", "Acesso de leitura"),
                new AuthorizationScope("WRITE", "Acesso de escrita"));
    }

    // Lista de Códigos de status de erro Globais que serão exibidos na Documentação (Problem.class)
    private List<ResponseMessage> globalGetResponseMessages() {
        return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.OK.value())
                        .message("Consulta realizada com sucesso") // SpringFox substitui pelo que está específico em cada método
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Erro interno no servidor")
                        .responseModel(new ModelRef("Problema")) // Referenciando o modelo de resposta em caso 400 de GetMethod
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Recurso não possui representação aceita pelo consumidor")
                        .build()
        );
    }

    private List<ResponseMessage> globalPostResponseMessages() {
        return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Requisição inválida (erro do cliente)")
                        .responseModel(new ModelRef("Problema"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Erro interno no servidor")
                        .responseModel(new ModelRef("Problema"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Recurso não possui representação aceita pelo consumidor")
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                        .message("Requisição recusada porque o corpo está em um formato não suportado")
                        .responseModel(new ModelRef("Problema"))
                        .build()
        );
    }

    private List<ResponseMessage> globalPutResponseMessages() {
        return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Requisição inválida (erro do cliente)")
                        .responseModel(new ModelRef("Problema"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Erro interno no servidor")
                        .responseModel(new ModelRef("Problema"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Recurso não possui representação aceita pelo consumidor")
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                        .message("Requisição recusada porque o corpo está em um formato não suportado")
                        .responseModel(new ModelRef("Problema"))
                        .build()
        );
    }

    private List<ResponseMessage> globalDeleteResponseMessages() {
        return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Requisição inválida (erro do cliente)")
                        .responseModel(new ModelRef("Problema"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Erro interno no servidor")
                        .responseModel(new ModelRef("Problema"))
                        .build()
        );
    }

    private ApiInfo apiInfoV1() {
        return new ApiInfoBuilder()
                .title("vepilefFood API (Depreciada)")
                .description("API aberta para clientes e restaurantes. <br>" +
                        "<strong>Essa versão da API está depreciada e deixará de existir a partir de 01/01/20XX." +
                        "Use a versão mais atual da API.</strong>")
                .version("1")
                .contact(new Contact("Food", "http://www.food.com", "contato@food.com"))
                .build();
    }

    private ApiInfo apiInfoV2() {
        return new ApiInfoBuilder()
                .title("vepilefFood API")
                .description("API aberta para clientes e restaurantes")
                .version("2")
                .contact(new Contact("Food", "http://www.food.com", "contato@food.com"))
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
