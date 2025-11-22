# Cómo Instalar e Iniciar MongoDB en Windows

## 🔍 Verificar si MongoDB está Instalado

Primero, verifica si MongoDB ya está instalado:

```powershell
# Verificar si existe el servicio
Get-Service -Name "*mongo*"

# Verificar si mongod está en el PATH
mongod --version
```

---

## 📥 Opción 1: Instalación Manual de MongoDB (Recomendado)

### Paso 1: Descargar MongoDB
1. Ve a: https://www.mongodb.com/try/download/community
2. Selecciona:
   - **Version**: 7.0 (o la más reciente)
   - **Platform**: Windows
   - **Package**: MSI
3. Haz clic en **Download**

### Paso 2: Instalar MongoDB
1. Ejecuta el archivo `.msi` descargado
2. Durante la instalación:
   - ✅ Marca **"Install MongoDB as a Service"**
   - ✅ Marca **"Run service as Network Service user"**
   - ✅ Marca **"Install MongoDB Compass"** (opcional, pero útil)
   - Deja el nombre del servicio como **"MongoDB"**
   - Deja el puerto como **27017** (puerto por defecto)

### Paso 3: Verificar la Instalación
Después de instalar, MongoDB debería iniciarse automáticamente. Verifica:

```powershell
# Verificar que el servicio existe
Get-Service -Name "MongoDB"

# Verificar que está corriendo
Get-Service -Name "MongoDB" | Select-Object Status, Name
```

Si el Status es **"Running"**, MongoDB está funcionando ✅

---

## 🚀 Opción 2: Usar Docker (Más Rápido)

Si tienes Docker instalado:

```powershell
# Descargar e iniciar MongoDB en un contenedor
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Verificar que está corriendo
docker ps

# Ver logs
docker logs mongodb
```

**Ventajas**: No necesitas instalar MongoDB, solo Docker.

---

## 🛠️ Opción 3: Usar Chocolatey (Si tienes Chocolatey)

```powershell
# Instalar MongoDB
choco install mongodb

# Iniciar el servicio
Start-Service MongoDB
```

---

## ▶️ Cómo Iniciar MongoDB

### Si MongoDB está instalado como Servicio:

```powershell
# Iniciar el servicio
Start-Service MongoDB

# Verificar que está corriendo
Get-Service MongoDB

# Si necesitas detenerlo
Stop-Service MongoDB

# Si necesitas reiniciarlo
Restart-Service MongoDB
```

### Si MongoDB NO está instalado como Servicio:

1. **Encuentra la ruta de instalación** (normalmente):
   ```
   C:\Program Files\MongoDB\Server\7.0\bin\
   ```

2. **Crea el directorio de datos** (si no existe):
   ```powershell
   mkdir C:\data\db
   ```

3. **Inicia MongoDB manualmente**:
   ```powershell
   cd "C:\Program Files\MongoDB\Server\7.0\bin"
   .\mongod.exe
   ```
   
   **IMPORTANTE**: Deja esta ventana abierta mientras usas la aplicación.

---

## ✅ Verificar que MongoDB está Funcionando

### Método 1: Verificar el Servicio
```powershell
Get-Service MongoDB
```
Debe mostrar: `Status: Running`

### Método 2: Probar la Conexión
```powershell
# Si tienes mongosh instalado
mongosh

# O si tienes la versión antigua
mongo
```

Dentro de mongosh, ejecuta:
```javascript
db.version()
```
Debería mostrar la versión de MongoDB.

### Método 3: Probar desde el Navegador
Abre: `http://localhost:27017`

Si ves un mensaje como "It looks like you are trying to access MongoDB...", MongoDB está corriendo.

---

## 🔧 Solución de Problemas

### Error: "Service not found"
**Solución**: MongoDB no está instalado como servicio. Instálalo siguiendo la Opción 1.

### Error: "Access Denied" al iniciar el servicio
**Solución**: Ejecuta PowerShell como Administrador:
```powershell
# Click derecho en PowerShell > "Ejecutar como administrador"
Start-Service MongoDB
```

### Error: "Port 27017 already in use"
**Solución**: 
1. Verifica qué está usando el puerto:
   ```powershell
   netstat -ano | findstr :27017
   ```
2. Si es MongoDB, ya está corriendo ✅
3. Si es otra aplicación, detén esa aplicación o cambia el puerto de MongoDB

### Error: "Cannot create directory C:\data\db"
**Solución**: Crea el directorio manualmente:
```powershell
mkdir C:\data\db
```

### MongoDB no inicia automáticamente
**Solución**: Configura el servicio para que inicie automáticamente:
```powershell
Set-Service -Name MongoDB -StartupType Automatic
```

---

## 📝 Comandos Útiles

```powershell
# Ver estado del servicio
Get-Service MongoDB

# Iniciar MongoDB
Start-Service MongoDB

# Detener MongoDB
Stop-Service MongoDB

# Reiniciar MongoDB
Restart-Service MongoDB

# Ver logs del servicio (si está instalado como servicio)
Get-EventLog -LogName Application -Source MongoDB -Newest 10

# Verificar que está escuchando en el puerto 27017
netstat -ano | findstr :27017
```

---

## 🎯 Para tu Proyecto Spring Boot

Una vez que MongoDB esté corriendo:

1. **Verifica que está en el puerto 27017** (puerto por defecto)
2. **No necesitas crear la base de datos manualmente** - Spring Boot la creará automáticamente
3. **La base de datos se llamará "notificaciones"** (según tu `application.properties`)

---

## ✅ Checklist Rápido

- [ ] MongoDB descargado e instalado
- [ ] Servicio MongoDB corriendo (`Get-Service MongoDB` muestra "Running")
- [ ] Puerto 27017 disponible (`netstat -ano | findstr :27017`)
- [ ] Puedes conectarte con `mongosh` o `mongo`
- [ ] Listo para ejecutar tu aplicación Spring Boot ✅

---

## 🚀 Siguiente Paso

Una vez que MongoDB esté corriendo, ejecuta tu aplicación Spring Boot:

```powershell
cd "C:\Users\llans\OneDrive\Documentos\GitHub\Concurrente4"
mvn spring-boot:run
```

¡Listo! 🎉

