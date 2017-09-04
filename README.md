# sfx-auth

Servidor de autenticación OAuth2

## Instalación
1. Ejecutar script `oauth_tables.sql` en la base de datos previamente generada en PostgreSQL
2. Compilar código y ejecutar el JAR generado en dicha operación
3. Visitar la página `http://MY-URL.com` para verificar que el servicio se encuentre disponible

## Implementación de protocolo

Para implementar el protocolo OAuth2 en proyectos o servicios generados por nosotros, es indispensable contar con las siguientes librerías:

- hibernate-core-5.0.12.Final
- hibernate-entity-manager-5.0.12.Final
- spring-core-4.3.9.RELEASE
- spring-context-4.3.9.RELEASE
- spring-orm-4.3.9.RELEASE
- spring-security-core-4.2.3.RELEASE
- spring-security-config-4.2.3.RELEASE
- spring-security-oauth2-2.0.14.RELEASE
- spring-security-web-4.2.3.RELEASE
- spring-web-4.3.9.RELEASE
- spring-webmvc-4.3.9.RELEASE

En caso que se decida utilizar Spring Boot, es necesario seleccionar la versión a partir de la 1.5.6.RELEASE que ya empaqueta las librerías previamente mencionadas

Debido a la serialización del token de acceso es necesario que las clases `Authority` y `User` se incluyan dentro del paquete `com.sfinx.pdmm.oauth.domain`.

## Notas de empaquetado

Si se desea empaquetar el componente en formato **WAR** es necesario renombrar el archivo final como `service.war` para desplegarlo en el servidor Tomcat de PROD.

## Endpoints de autenticación

Las siguientes direcciones estarán disponibles solicitud de tokens de acceso

### Código de autorización

Para solicitar un token de acceso vía código de autenticación, es necesario realizar una petición `GET` al siguiente endpoint el cuál especifica el tipo de respuesta:

`http://MY-URL.com/oauth/authorize?response_type=code&client_id=pdmm&redirect_uri=http://MY-URL.com`

La respuesta por parte del servidor será una redirección al sitio indicado en redirect_uri con el siguiente formato:

`http://MY-URL.com?code=MY-CODE`

*Pendiente la solicitud de token mediante código*

### Autenticación implícita

Este método también solicita una petición `GET` desde el navegador al siguiente endpoint:

`http://MY-URL.com/oauth/authorize?response_type=token&client_id=pdmm&redirect_uri=http://url-cliente.com`

La respuesta del servidor se realizará con un redireccionamiento indicado por redirect_uri con el siguiente formato:

`http://MY-URL.com/#access_token=MY-ACCESS-TOKEN&token_type=bearer&expires_in=MY-TIME&scope=SCOPE-ONE%20SCOPE-TWO`

### Cierre de sesión

Concluye una sesión `HTTP` desde la plataforma web mediante una petición `GET` hacia el siguiente endpoint:

`http://MY-URL.com/logout`