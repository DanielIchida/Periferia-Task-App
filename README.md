## Instalacion
### Task-Backend
- Java 17
- Spring boot 3.5
- Ejecutar el proyecto con el comando de Gradle ./gradlew bootRun
- Apunta al puerto 8090

### Task-FrontEnd
- Angular 21
- Node 22
- Instalar dependencias locales npm install
- Ejecutar el proyecto con el comando npm run start
- Apunta al puerto 4200

## Arquitectura
### Task-Backend
Uso las siguientes Capas:
- Capa de presentacion (Todo lo referente a controllers - la entrada de la informacion por medio de las Api Rest)
- Capa de DTOs (Todo lo referente a la capa de transferencias de datos al exterior)
- Capa de Servicio o Negocio (Todo lo referente a la logica del negocio)
- Capa de Acceso a Datos (todo lo referente a los repositorios en los cuales contiene la abstraccion de la base de datos)
- Capa de Modelo (es todo lo referente a modelos o entidades las cuales mapea la tabla de nuestra base de datos)
- Capas Transversales (no pertenecen a una capa si no al soporte de la aplicacion)
  - config (toda la configuracion a nivel general del proyecto o modulo)
  - exceptions (todas las excepciones personilzadas que se crean)
  - mappers (Su funcion es convertir objetos de un tipo a otro)
  - util (funciones de utilidad que sirven en toda la aplicacion)
 
### Task-FrontEnd
- Capa de pages (todas las paginas que usa la aplicacion)
- Capa de componentes (Todos los componentes que pueden ser reutilizables)
- Capa de interfaces (Todo los modelos o objetos)
- Capa de services (Todo lo referente a la logica de negocio)
