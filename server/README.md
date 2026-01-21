# Servidor Backend - Donde Aparque el Coche

Servidor Node.js simple para sincronizar la ubicación del coche entre dispositivos.

## 📦 Requisitos

Necesitas tener **Node.js** instalado. Si no lo tienes:

### Windows
1. Descarga desde: https://nodejs.org/
2. Instala la versión LTS (recomendada)
3. Verifica en la terminal: `node --version`

### Linux/Mac
```bash
# Ubuntu/Debian
sudo apt install nodejs npm

# Mac (con Homebrew)
brew install node
```

---

## 🚀 Instalación y Ejecución

### 1. Instalar dependencias
Abre una terminal en la carpeta `server/` y ejecuta:

```bash
npm install
```

Esto instalará `express` y `cors`.

### 2. Ejecutar el servidor
```bash
npm start
```

o simplemente:
```bash
node server.js
```

Deberías ver:
```
🚀 Servidor corriendo en http://localhost:3000
📍 Estado actual: Desconocido
💡 Para exponer públicamente, ejecuta: ngrok http 3000
```

### 3. Exponer el servidor públicamente con ngrok

Para que los móviles puedan acceder desde internet, usa **ngrok**:

#### Instalar ngrok
1. Descarga desde: https://ngrok.com/download
2. Extrae el ejecutable
3. (Opcional) Crea cuenta gratuita en ngrok.com para más funciones

#### Ejecutar ngrok
En otra terminal, ejecuta:
```bash
ngrok http 3000
```

Te mostrará algo como:
```
Forwarding  https://abc123.ngrok-free.app -> http://localhost:3000
```

✅ **Copia esa URL** (ej: `https://abc123.ngrok-free.app`)

---

## 📱 Configurar la App Android

1. Abre el archivo `ApiService.kt` en Android Studio
2. Busca la línea que dice `BASE_URL`
3. Pega tu URL de ngrok:
```kotlin
private const val BASE_URL = "https://abc123.ngrok-free.app/"
```
4. Recompila la app

---

## 🧪 Probar el Servidor

### Prueba GET (obtener estado)
Abre en el navegador:
```
http://localhost:3000/status
```

Deberías ver:
```json
{
  "location": "Desconocido",
  "user": "",
  "timestamp": "",
  "timestampRaw": 0
}
```

### Prueba POST (actualizar estado)
Usando curl o Postman:
```bash
curl -X POST http://localhost:3000/status \
  -H "Content-Type: application/json" \
  -d '{
    "location": "Mercadona",
    "user": "Víctor",
    "timestamp": "20:00 19 ene",
    "timestampRaw": 1234567890
  }'
```

---

## 🔧 Solución de Problemas

### Error: "Cannot find module 'express'"
Ejecuta `npm install` en la carpeta `server/`

### ngrok dice "command not found"
Asegúrate de que ngrok está en tu PATH o ejecútalo con la ruta completa

### Los móviles no se conectan
- Verifica que el servidor está corriendo (`node server.js`)
- Verifica que ngrok está activo
- Copia la URL de ngrok EXACTA en `ApiService.kt`
- Asegúrate de que los móviles tienen conexión a internet

---

## 💡 Notas Importantes

- **El servidor debe estar SIEMPRE encendido** para que la app funcione
- **ngrok genera URLs aleatorias** cada vez que lo ejecutas (en la versión gratuita)
- Si reinicias ngrok, actualiza la URL en `ApiService.kt` y recompila la app
- Los datos se guardan **en memoria**, se pierden al cerrar el servidor

---

## 🎯 Alternativas para Producción

Si quieres una solución permanente sin tener tu PC encendida:

1. **Heroku** (gratis con limitaciones)
2. **Railway** (gratis, fácil de usar)
3. **Render** (gratis, 750h/mes)
4. **Un servidor VPS barato** (ej: DigitalOcean $4/mes)

---

¡Listo! 🎉
