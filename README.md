# TelarMVP

API REST de gestion de finanzas personales construida con Spring Boot y MySQL.

La primera funcionalidad implementada administra usuarios mediante una arquitectura por capas y acceso a datos con JDBC y consultas SQL. No se utiliza JPA.

## Requisitos

- JDK 17 o superior.
- MySQL 8.
- La base `finanzas_personales` y la tabla `usuario` creadas.

La tabla esperada tiene las columnas `id`, `nombre`, `email` y `password_hash`.

## Configuracion

Defina las variables de entorno antes de iniciar la aplicacion:

```bash
export DB_URL='jdbc:mysql://localhost:3306/finanzas_personales?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true'
export DB_USERNAME='telar_app'
export DB_PASSWORD='su_contrasena'
```

El archivo `.env.example` contiene un ejemplo sin credenciales reales. Spring Boot no carga archivos `.env` automaticamente; las variables deben exportarse en la terminal o configurarse en el IDE.

## Ejecucion

Si Java 17 se instalo con Homebrew en un Mac con Apple Silicon:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
```

```bash
./gradlew test
./gradlew bootRun
```

La API queda disponible en `http://localhost:8080/api/usuarios`.

## Endpoints

| Metodo | Ruta | Resultado esperado |
| --- | --- | --- |
| `GET` | `/api/usuarios` | Lista todos los usuarios |
| `GET` | `/api/usuarios/{id}` | Consulta un usuario |
| `POST` | `/api/usuarios` | Crea un usuario y responde `201` |
| `PUT` | `/api/usuarios/{id}` | Actualiza nombre y email |
| `DELETE` | `/api/usuarios/{id}` | Elimina el usuario y responde `204` |

Crear un usuario:

```bash
curl -i -X POST http://localhost:8080/api/usuarios \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Laura Perez","email":"laura@example.com","password":"ClaveSegura123"}'
```

Listar usuarios:

```bash
curl -i http://localhost:8080/api/usuarios
```

Actualizar un usuario:

```bash
curl -i -X PUT http://localhost:8080/api/usuarios/1 \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Laura Gomez","email":"laura.gomez@example.com"}'
```

Eliminar un usuario:

```bash
curl -i -X DELETE http://localhost:8080/api/usuarios/1
```

La contrasena nunca se devuelve en las respuestas y se almacena como hash BCrypt. El borrado puede eliminar informacion relacionada si las llaves foraneas de la base usan `ON DELETE CASCADE`.

## Arquitectura

```text
HTTP
  -> UsuarioController
  -> UsuarioService
  -> UsuarioRepository
  -> JdbcTemplate
  -> MySQL
```

- `controllers`: protocolo HTTP y codigos de respuesta.
- `services`: reglas de negocio y transacciones.
- `repositories`: consultas SQL y mapeo de resultados.
- `entities`: representacion de los datos del dominio.
- `dto`: validacion de los cuerpos de entrada.
- `exceptions`: respuestas consistentes para errores.

## Flujo de ramas recomendado

El trabajo nuevo debe salir de `develop` en ramas `feature/*`. Cada integrante debe hacer commits propios y abrir un pull request hacia `develop`. Cuando la entrega sea estable, `develop` se integra en `main`.

No se deben fabricar commits a nombre de otros integrantes: la evaluacion del trabajo en equipo debe corresponder a contribuciones reales.
