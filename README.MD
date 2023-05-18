# Challenge ONE Back End Java + Spring

Este proyecto es una API para el foro de Alura Latam.

## Tecnologias Usadas

- Java 17
- Spring Security
- Token JWT
- SwaggerUI - OpenApi

## Funcionalidades

Los usuarios podrán:

- Crear una cuenta de usuario
- Iniciar sesión con su cuenta de usuario
- Crear un nuevo tema de discusión (topic)
- Ver una lista de todos los temas de discusión (topics)
- Ver los detalles de un tema de discusión específico, incluyendo todas las respuestas (replies)
- Crear una nueva respuesta (reply) para un tema de discusión específico
- Actualizar una respuesta o un topico existente si el usuario es el autor de estos

Adicionalmente, se integró la documentación mediante swagger, la API está protegida con Spring Security y utiliza JWT para manejar la autenticación y autorización. Al registrarse o iniciar sesión, se genera un token JWT para el usuario. Este token se utiliza para autenticar todas las solicitudes posteriores a la API. El token tiene una vida útil de dos horas y debe renovarse periódicamente para evitar el acceso no autorizado.

http://localhost:8080/swagger-ui/index.html - Link para ingresar a Swagger UI

### Preview
![image](https://github.com/wesvm/ForoAlura-APIRest/assets/76707027/55d0f179-7fda-418f-a33c-b31adaccdfc0)


![cms_files_10224_1673890737Prancheta_9](https://user-images.githubusercontent.com/76707027/236654191-cc167f20-851a-4cc7-b48b-0c9682911ef5.png)
