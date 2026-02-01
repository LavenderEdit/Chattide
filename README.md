<p align="center">
  <a href="https://endearing-blini-6a6b91.netlify.app/" target="_blank">
    <img src="https://drive.google.com/uc?export=view&id=1TuT30CiBkinh85WuTvjKGKN47hCyCS0Z" width="300" alt="Studios TKOH Logo">
  </a>
</p>

# 🌊 Chattide – Backend Social Network API

Chattide es una **API RESTful robusta, segura y escalable** diseñada para soportar una red social moderna. Está construida con **Java 21** y **Spring Boot**, enfocándose en buenas prácticas de arquitectura, seguridad avanzada y comunicación en tiempo real.

La plataforma gestiona autenticación segura, interacción social, grupos, notificaciones en tiempo real y almacenamiento multimedia en la nube.

---

## 🚀 Características Principales

### 🔐 Seguridad y Autenticación

* **JWT (JSON Web Tokens)**

  * Access Tokens y Refresh Tokens
  * Rotación de tokens para mayor seguridad
* **Recuperación de contraseña**

  * Sistema OTP (One-Time Password) vía correo electrónico
* **RBAC (Role-Based Access Control)**

  * Roles globales: `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`
  * Roles por grupo: `OWNER`, `ADMIN`, `MEMBER`
* **Seguridad a nivel de método**

  * Uso de `@PreAuthorize` para validar propiedad de recursos (ej. solo el autor puede editar un post)

---

### 👥 Gestión Social

* **Usuarios**

  * Perfil de usuario
  * Búsqueda avanzada
  * Avatares personalizados
* **Grupos**

  * Creación de comunidades
  * Unirse / salir
  * Expulsión de miembros
  * Transferencia de propiedad
* **Publicaciones (Posts)**

  * Muro de usuario
  * Feed por grupo
  * Soporte para imágenes
* **Interacciones**

  * Comentarios en publicaciones
  * Sistema de likes en posts y comentarios

---

### ⚡ Tiempo Real y Asincronía

* **Notificaciones en tiempo real**

  * WebSockets con STOMP
  * Notificaciones instantáneas ante nuevos comentarios
* **Arquitectura Orientada a Eventos (EDA)**

  * RabbitMQ para desacoplar procesos

**Flujo de eventos:**

```
Usuario comenta
   ↓
Productor AMQP
   ↓
RabbitMQ
   ↓
Consumidor AMQP
   ↓
WebSocket Push
```

---

### ☁️ Integraciones Cloud

* **Google Drive API (v3)**

  * Almacenamiento de avatares e imágenes de publicaciones
  * Generación de enlaces públicos de visualización
* **SMTP Email (Gmail)**

  * Envío de correos transaccionales (OTP, recuperación de contraseña)

---

## 🛠️ Stack Tecnológico

| Categoría     | Tecnología                  | Descripción                  |
| ------------- | --------------------------- | ---------------------------- |
| Lenguaje      | Java 21                     | Última versión LTS           |
| Framework     | Spring Boot 4.0.1           | Núcleo del backend           |
| Base de Datos | MySQL 8                     | Persistencia relacional      |
| Migraciones   | Flyway                      | Control de versiones DB      |
| Seguridad     | Spring Security 6           | Autenticación y autorización |
| ORM           | Spring Data JPA / Hibernate | Abstracción de base de datos |
| Mapping       | MapStruct                   | DTOs eficientes              |
| Utilidades    | Lombok                      | Reducción de boilerplate     |
| Messaging     | RabbitMQ                    | Procesamiento asíncrono      |
| Real-Time     | Spring WebSockets           | Comunicación bidireccional   |
| Cloud         | Google Drive API v3         | Almacenamiento de archivos   |

---

## 🏗️ Arquitectura y Patrones

El proyecto sigue una **Arquitectura en Capas (Layered Architecture)** limpia y mantenible:

* **Controller Layer (`/controller`)**

  * Manejo de peticiones HTTP
  * Validación de entrada (`@Valid`)
* **Service Layer (`/service`)**

  * Lógica de negocio
  * Validaciones de seguridad
  * Manejo de transacciones
* **Repository Layer (`/repository`)**

  * Acceso a datos con Spring Data JPA
* **Security Layer (`/security`)**

  * Filtros JWT
  * Control de acceso (`AccessControlService`)

### Patrones de Diseño Utilizados

* **DTO (Data Transfer Object)** – Separación entre entidades y respuestas API
* **Repository Pattern** – Abstracción del acceso a datos
* **Observer / Pub-Sub** – Implementado con RabbitMQ para notificaciones

---

## ⚙️ Configuración e Instalación

### Prerrequisitos

* JDK 21
* Maven
* MySQL Server
* RabbitMQ Server
* Credenciales de Google Cloud (OAuth 2.0 – Drive API)

---

### Variables de Entorno

El archivo `application.properties` utiliza variables de entorno para evitar exponer credenciales.

```properties
# Base de Datos
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=chattide_db
MYSQL_USER=root
MYSQL_PASSWORD=tu_password

# Seguridad JWT
JWT_TOKEN=tu_secreto_base64_min_256_bits
JWT_EXPIRATION_TIME=86400000

# Google Drive API
DRIVE_OAUTH_CLIENT_ID=tu_client_id.apps.googleusercontent.com
DRIVE_OAUTH_CLIENT_SECRET=tu_client_secret
DRIVE_OAUTH_REFRESH_TOKEN=tu_refresh_token
DRIVE_FOLDER_USER_AVATARS=id_carpeta_drive_avatares
DRIVE_FOLDER_USER_POSTS=id_carpeta_drive_posts

# Email SMTP
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
GMAIL_APP_EMAIL=tu_email@gmail.com
GMAIL_APP_PASSWORD=tu_app_password

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASS=guest
```

---

### Ejecución

1. Clona el repositorio
2. Asegúrate de que **MySQL** y **RabbitMQ** estén activos
3. Ejecuta la aplicación:

```bash
./mvnw spring-boot:run
```

Flyway ejecutará automáticamente las migraciones SQL necesarias.

---

## 📡 Endpoints Principales

### 🔑 Auth (`/api/auth`)

* `POST /register` – Registro de usuarios
* `POST /login` – Login (Access + Refresh Token)
* `POST /refresh-token` – Nuevo Access Token
* `POST /forgot-password` / `POST /reset-password` – Recuperación de contraseña

### 👤 Usuarios (`/api/usuarios`)

* `GET /{id}` – Obtener perfil
* `PUT /{id}/avatar` – Subir avatar
* `POST /buscar` – Búsqueda paginada

### 👥 Grupos (`/api/grupos`)

* `POST /` – Crear grupo
* `POST /{id}/unirse` – Unirse a grupo
* `DELETE /{id}/miembros` – Expulsar usuario

### 📝 Publicaciones (`/api/publicaciones`)

* `POST /` – Crear publicación
* `GET /grupo/{id}` – Feed de grupo
* `GET /usuario/{id}` – Muro de usuario

---

## 🔌 WebSockets

* **Endpoint:** `/ws` (SockJS soportado)
* **Notificaciones privadas:** `/user/queue/notifications`

---

## 🗄️ Base de Datos (Schema)

Tablas principales:

* `usuarios`
* `usuario_roles`
* `grupos`, `usuario_grupo`
* `publicaciones`
* `comentarios`
* `likes`, `likes_comentarios`
* `refresh_tokens`
* `password_reset_tokens`

---

## 🧪 Testing

Incluye soporte para:

* Tests unitarios
* Tests de integración
* Tests de seguridad y mensajería

Dependencias:

* `spring-boot-starter-test`
* `spring-boot-starter-amqp-test`
* `spring-boot-starter-security-test`

```bash
./mvnw test
```

---

<p align="center">
  <sub>🛠️ Desarrollado con 💙 por <strong>Studios TKOH</strong></sub><br>
</p>

