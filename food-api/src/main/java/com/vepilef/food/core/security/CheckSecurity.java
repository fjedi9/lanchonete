package com.vepilef.food.core.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

    public @interface Cozinhas {

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')") // Spring Expression Language
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

        @PreAuthorize("@foodSecurity.podeConsultarCozinhas()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }
    }

    public @interface Restaurantes {

        // Usuário responsável não pode editar dados diretamente, tem de solicitar a um funcionário da vepilefFood
        @PreAuthorize("@foodSecurity.podeGerenciarCadastroRestaurantes()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarCadastro {
        }

        @PreAuthorize("@foodSecurity.podeGerenciarFuncionamentoRestaurantes(#restauranteId)")
        // RestauranteUsuarioResponsavel. @ dá acesso a um Bean do SPring
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarFuncionamento {
        }

        @PreAuthorize("@foodSecurity.podeConsultarRestaurantes()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

    }

    public @interface Pedidos {

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and isAuthenticated()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeCriar {
        }

        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or " +
                "@foodSecurity.usuarioAutenticadoIgual(returnObject.cliente.id) or " + // Usuario é o q realizou o pedido permitido
                "@foodSecurity.gerenciaRestaurante(returnObject.restaurante.id)")
        // Proprietário do Restaurante a qual o pedido pertence
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeBuscar {
        }

        @PreAuthorize("@foodSecurity.podePesquisarPedidos(#filtro.clienteId, #filtro.restauranteId)")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodePesquisar {

        }

        @PreAuthorize("@foodSecurity.podeGerenciarPedidos(#codigoPedido)")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarPedidos {
        }
    }

    public @interface FormaPagamento {

        @PreAuthorize("@foodSecurity.podeConsultarFormasPagamento()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

    }

    public @interface Cidades {

        @PreAuthorize("@foodSecurity.podeConsultarCidades()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_CIDADES')")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }
    }

    public @interface Estados {

        @PreAuthorize("@foodSecurity.podeConsultarEstados()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_ESTADOS')")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

    }

    public @interface UsuariosGruposPermissoes {

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and " +
                "@foodSecurity.usuarioAutenticadoIgual(#usuarioId)")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeAlterarPropriaSenha {
        }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and (" +
                "hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or " +
                "@foodSecurity.usuarioAutenticadoIgual(#usuarioId))")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeAlterarUsuario {
        }

        @PreAuthorize("@afoodSecurity.podeEditarUsuariosGruposPermissoes()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

        @PreAuthorize("@foodSecurity.podeConsultarUsuariosGruposPermissoes()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }
    }

    public @interface Estatisticas {

        @PreAuthorize("@foodSecurity.podeConsultarEstatisticas()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }
    }

}
