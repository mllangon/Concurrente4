# Guía Paso a Paso para Ejecutar el Proyecto

## 📋 Requisitos Previos

Antes de comenzar, necesitas tener instalado:

1. **Java 17 o superior**
2. **Maven 3.6 o superior**
3. **MongoDB 4.4 o superior**

---

## 🔍 Paso 1: Verificar Instalaciones

### Verificar Java
Abre una terminal (PowerShell o CMD) y ejecuta:
```bash
java -version
```
Debe mostrar algo como: `openjdk version "17.x.x"` o superior.

### Verificar Maven
```bash
mvn -version
```
Debe mostrar la versión de Maven instalada.

### Verificar MongoDB
```bash
mongod --version
```
Debe mostrar la versión de MongoDB.

---

## 🗄️ Paso 2: Instalar MongoDB (si no lo tienes)

### Opción A: Instalación Manual
1. Descarga MongoDB desde: https://www.mongodb.com/try/download/community
2. Ejecuta el instalador y sigue las instrucciones
3. Durante la instalación, marca la opción "Install MongoDB as a Service"

### Opción B: Usando Chocolatey (Windows)
```bash
choco install mongodb
```

### Opción C: Usando Docker
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

---

## 🚀 Paso 3: Iniciar MongoDB

### Si MongoDB está instalado como servicio (Windows):
MongoDB debería iniciarse automáticamente. Verifica que esté corriendo:
```bash
# Verificar si el servicio está corriendo
Get-Service MongoDB
```

Si no está corriendo, inícialo:
```bash
# Iniciar el servicio MongoDB
Start-Service MongoDB
```

### Si MongoDB NO está instalado como servicio:
Abre una terminal y ejecuta:
```bash
mongod
```
**IMPORTANTE**: Deja esta terminal abierta mientras ejecutas la aplicación.

### Verificar que MongoDB está corriendo:
Abre otra terminal y ejecuta:
```bash
mongo --eval "db.version()"
```
O si usas MongoDB 6+:
```bash
mongosh --eval "db.version()"
```

Si muestra la versión, MongoDB está funcionando correctamente.

---

## 📂 Paso 4: Navegar al Directorio del Proyecto

Abre una terminal y navega a la carpeta del proyecto:
```bash
cd "C:\Users\llans\OneDrive\Documentos\GitHub\Concurrente4"
```

---

## 🔨 Paso 5: Compilar el Proyecto

Ejecuta Maven para descargar dependencias y compilar:
```bash
mvn clean install
```

Este comando:
- Descarga todas las dependencias necesarias
- Compila el código Java
- Ejecuta las pruebas (si las hay)
- Crea el archivo JAR

**Tiempo estimado**: 2-5 minutos (la primera vez puede tardar más por las descargas)

Si todo va bien, verás al final: `BUILD SUCCESS`

---

## ▶️ Paso 6: Ejecutar la Aplicación

Tienes dos opciones:

### Opción A: Usando Maven (Recomendado)
```bash
mvn spring-boot:run
```

### Opción B: Usando el JAR compilado
```bash
java -jar target/notificaciones-sse-1.0.0.jar
```

**Espera a ver este mensaje en la consola:**
```
Started NotificacionesApplication in X.XXX seconds
```

Esto indica que la aplicación está corriendo en el puerto 8080.

---

## 🌐 Paso 7: Acceder a la Aplicación

1. Abre tu navegador web (Chrome, Firefox, Edge, etc.)
2. Ve a la siguiente URL:
   ```
   http://localhost:8080/notificaciones/usuario1
   ```

Deberías ver la interfaz de notificaciones con:
- Un encabezado azul con el título
- Un selector para filtrar por tipo
- Un botón "Generar Notificaciones de Ejemplo"
- Una tabla (inicialmente vacía o con mensaje de carga)

---

## 🧪 Paso 8: Probar la Aplicación

### 8.1. Generar Notificaciones de Ejemplo
1. Haz clic en el botón **"Generar Notificaciones de Ejemplo"**
2. Deberías ver aparecer 3 notificaciones en la tabla:
   - "Pedido enviado" (INFO)
   - "Pago rechazado" (ALERTA)
   - "Servidor caído" (URGENTE)

### 8.2. Probar el Filtrado
1. En el selector "Filtrar por tipo", selecciona **"INFO"**
2. Solo deberías ver la notificación de tipo INFO
3. Selecciona **"Todos"** para ver todas de nuevo

### 8.3. Marcar como Leída
1. Haz clic en el botón **"Marcar Leído"** de cualquier notificación no leída
2. La notificación debería cambiar de color (ya no estará resaltada en amarillo)
3. El símbolo ✗ debería cambiar a ✓

### 8.4. Eliminar Notificación
1. Haz clic en el botón **"Eliminar"** de cualquier notificación
2. Confirma la eliminación en el diálogo
3. La notificación debería desaparecer de la tabla

### 8.5. Probar Notificaciones en Tiempo Real
Para probar que SSE funciona correctamente:

**Opción A: Usando otra terminal con curl**
```bash
# En otra terminal, crear una nueva notificación
curl -X POST http://localhost:8080/notificaciones/api ^
  -H "Content-Type: application/json" ^
  -d "{\"usuario\":\"usuario1\",\"mensaje\":\"Nueva notificación en tiempo real\",\"tipo\":\"INFO\"}"
```

**Opción B: Usando Postman o similar**
- Método: POST
- URL: `http://localhost:8080/notificaciones/api`
- Headers: `Content-Type: application/json`
- Body (JSON):
```json
{
  "usuario": "usuario1",
  "mensaje": "Nueva notificación en tiempo real",
  "tipo": "INFO"
}
```

La notificación debería aparecer automáticamente en el navegador sin recargar la página.

---

## 🔍 Verificar que Todo Funciona

### Verificar MongoDB
Abre otra terminal y ejecuta:
```bash
mongosh
```
Luego dentro de mongosh:
```javascript
use notificaciones
db.notificaciones.find().pretty()
```
Deberías ver las notificaciones guardadas en la base de datos.

### Verificar Logs de la Aplicación
En la terminal donde está corriendo la aplicación, deberías ver logs como:
```
DEBUG com.concurrente.notificaciones...
```

---

## ⚠️ Solución de Problemas Comunes

### Error: "Cannot connect to MongoDB"
**Solución**: 
- Verifica que MongoDB esté corriendo: `Get-Service MongoDB`
- Si no está corriendo: `Start-Service MongoDB`
- Verifica que esté en el puerto 27017

### Error: "Port 8080 is already in use"
**Solución**: 
- Cambia el puerto en `application.properties`: `server.port=8081`
- O cierra la aplicación que está usando el puerto 8080

### Error: "Java version not supported"
**Solución**: 
- Verifica que tengas Java 17+: `java -version`
- Si no, descarga e instala Java 17 desde: https://adoptium.net/

### Error: "Maven not found"
**Solución**: 
- Instala Maven desde: https://maven.apache.org/download.cgi
- O usa el wrapper: `mvnw spring-boot:run` (si existe mvnw.bat)

### La página no carga o muestra error
**Solución**: 
- Verifica que la aplicación esté corriendo (deberías ver logs en la terminal)
- Verifica la URL: debe ser exactamente `http://localhost:8080/notificaciones/usuario1`
- Revisa la consola del navegador (F12) para ver errores de JavaScript

### Las notificaciones no aparecen en tiempo real
**Solución**: 
- Verifica que el navegador soporte SSE (todos los navegadores modernos lo soportan)
- Abre la consola del navegador (F12) y revisa si hay errores
- Verifica que el endpoint SSE esté funcionando: `http://localhost:8080/notificaciones/sse/usuario1`

---

## 🛑 Detener la Aplicación

Para detener la aplicación:
1. Ve a la terminal donde está corriendo
2. Presiona `Ctrl + C`
3. Espera a que se cierre correctamente

Para detener MongoDB (si lo iniciaste manualmente):
- Si está como servicio: `Stop-Service MongoDB`
- Si lo iniciaste con `mongod`: Presiona `Ctrl + C` en esa terminal

---

## 📝 Notas Adicionales

- La aplicación se conecta automáticamente a MongoDB en `localhost:27017`
- La base de datos se llama `notificaciones` y se crea automáticamente
- La colección se llama `notificaciones` y se crea automáticamente
- Puedes cambiar el usuario en la URL: `/notificaciones/usuario2`, `/notificaciones/usuario3`, etc.
- Cada usuario verá solo sus propias notificaciones

---

## ✅ Checklist de Ejecución

- [ ] Java 17+ instalado y verificado
- [ ] Maven instalado y verificado
- [ ] MongoDB instalado y corriendo
- [ ] Proyecto compilado exitosamente (`mvn clean install`)
- [ ] Aplicación ejecutándose (`mvn spring-boot:run`)
- [ ] Página web accesible en `http://localhost:8080/notificaciones/usuario1`
- [ ] Notificaciones de ejemplo generadas correctamente
- [ ] Filtrado por tipo funciona
- [ ] Marcar como leída funciona
- [ ] Eliminar notificación funciona
- [ ] Notificaciones en tiempo real funcionan (SSE)

---

¡Listo! Si completaste todos los pasos, tu aplicación debería estar funcionando correctamente. 🎉

