# 🚗 ¿Dónde Aparqué? - Full Stack App

Aplicación completa para gestionar la ubicación del coche compartido entre amigos/familia. Permite guardar quién aparcó, dónde y cuándo, sincronizándose en tiempo real entre todos los dispositivos.

## 🏗️ Arquitectura

El proyecto consta de tres partes:
1.  **Android App**: Cliente nativo (Kotlin + Jetpack Compose) que instalas en el móvil.
2.  **Web Client**: App web accesible desde cualquier navegador (iOS/PC) para quien no tenga Android.
3.  **Backend Server**: Servidor Node.js + MongoDB que guarda los datos en la nube.

---

## ☁️ 1. Despliegue del Servidor (Backend)

Para que la app funcione 24/7 sin depender de tu ordenador, alojamos el servidor gratuitamente en la nube.

### A. Base de Datos (MongoDB Atlas)
1.  Crea una cuenta gratuita en [MongoDB Atlas](https://www.mongodb.com/try).
2.  Crea un Cluster **M0 (Free)**.
3.  Crea un usuario de base de datos (`admin` / password).
4.  En **Network Access**, permite acceso desde `0.0.0.0/0`.
5.  Obtén la **Connection String** (Drivers -> Node.js).

### B. Servidor (Render)
1.  Sube este código a **GitHub**.
2.  Crea un "Web Service" en [Render](https://render.com/).
3.  Conecta tu repositorio.
    - **Root Directory**: `server`
    - **Build Command**: `npm install`
    - **Start Command**: `node server.js`
4.  Añade una **Environment Variable**:
    - Key: `MONGODB_URI`
    - Value: (Tu connection string de MongoDB)

✅ **Resultado**: Obtendrás una URL (ej: `https://donde-aparque.onrender.com`) que es tanto tu API como tu Web.

---

## 📲 2. Cliente Android (APK)

### Configuración
En el código Android, asegúrate de que `ApiService.kt` apunta a tu servidor de Render:
```kotlin
const val BASE_URL = "https://tu-app-en-render.onrender.com/"
```

### Generar e Instalar APK
1.  En Android Studio: **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**.
2.  El archivo se genera en:
    `app/build/outputs/apk/debug/app-debug.apk`
3.  Envía este archivo por WhatsApp/Email a los usuarios.
4.  Instala activando "Orígenes desconocidos".

---

## 🌐 3. Cliente Web (iOS / PC)

No requiere instalación. Solo entra a la URL de tu servidor en Render:
👉 `https://tu-app-en-render.onrender.com`

Funciona exactamente igual que la app nativa, ideal para usuarios de iPhone.

---

## 🛠️ Desarrollo Local

Si quieres ejecutarlo en tu PC para hacer cambios:

**Backend:**
```bash
cd server
npm install
npm start
```

**Frontend (Android):**
- Abrir en Android Studio y ejecutar en emulador.
- Recuerda cambiar `BASE_URL` a `http://10.0.2.2:3000/` para pruebas locales.
