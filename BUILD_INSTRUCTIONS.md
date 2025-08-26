# Instrucciones de Compilación - Ritsu AI Assistant

## 📋 Requisitos Previos

### Software Necesario
- **Android Studio** (versión 2023.1.1 o superior)
- **Java Development Kit (JDK)** 11 o superior
- **Android SDK** API level 26 (Android 8.0) o superior
- **Gradle** 8.0 o superior

### Hardware Recomendado
- **RAM**: Mínimo 8GB, recomendado 16GB
- **Almacenamiento**: Mínimo 10GB de espacio libre
- **CPU**: Procesador de 4 núcleos o superior
- **Dispositivo de prueba**: Android 8.0+ con 4GB+ RAM

## 🚀 Pasos de Compilación

### 1. Clonar el Proyecto
```bash
git clone <repository-url>
cd ritsu-ai-assistant
```

### 2. Configurar Android Studio
1. Abrir Android Studio
2. Seleccionar "Open an existing Android Studio project"
3. Navegar al directorio del proyecto y seleccionarlo
4. Esperar a que se sincronice el proyecto

### 3. Configurar SDK y Dependencias
1. Ir a **File > Project Structure**
2. En **SDK Location**, verificar que Android SDK esté configurado
3. En **Modules > app**, verificar que:
   - **Compile SDK Version**: 34
   - **Min SDK Version**: 26
   - **Target SDK Version**: 34

### 4. Sincronizar Gradle
1. Hacer clic en **Sync Project with Gradle Files** (icono de elefante)
2. Esperar a que se descarguen todas las dependencias
3. Verificar que no hay errores de compilación

### 5. Configurar Permisos de Desarrollo
1. En el dispositivo Android, habilitar **Opciones de desarrollador**
2. Activar **Depuración USB**
3. Conectar el dispositivo por USB
4. Autorizar la depuración en el dispositivo

### 6. Compilar y Ejecutar
1. Seleccionar el dispositivo de destino en la barra de herramientas
2. Hacer clic en **Run** (botón verde de play)
3. Esperar a que se compile e instale la aplicación

## 🔧 Configuración Post-Instalación

### 1. Configurar como Launcher Predeterminado
1. Abrir **Configuración** del dispositivo
2. Ir a **Aplicaciones > Aplicaciones predeterminadas**
3. Seleccionar **Aplicación de inicio**
4. Elegir **Ritsu AI Assistant**

### 2. Configurar Permisos de Accesibilidad
1. Ir a **Configuración > Accesibilidad**
2. Buscar **Ritsu AI Assistant**
3. Activar el servicio de accesibilidad
4. Conceder todos los permisos solicitados

### 3. Configurar Permisos de Notificaciones
1. Ir a **Configuración > Notificaciones**
2. Buscar **Ritsu AI Assistant**
3. Activar todas las notificaciones
4. Configurar como **Servicio de notificaciones**

### 4. Configurar Permisos de Llamadas
1. Ir a **Configuración > Aplicaciones > Ritsu AI Assistant**
2. Seleccionar **Permisos**
3. Conceder permisos de:
   - Teléfono
   - Contactos
   - Micrófono
   - Almacenamiento
   - Ubicación

## 📱 Primera Ejecución

### 1. Tutorial Inicial
- La aplicación mostrará un tutorial interactivo
- Seguir las instrucciones para configurar Ritsu
- Personalizar la voz y apariencia

### 2. Descarga de Modelos de IA
- La aplicación descargará automáticamente los modelos necesarios
- Este proceso puede tomar varios minutos dependiendo de la conexión
- Los modelos se almacenan localmente para funcionamiento offline

### 3. Configuración de Contactos
- Importar contactos del dispositivo
- Configurar personalidades de Ritsu para cada contacto
- Establecer respuestas automáticas

## 🛠️ Solución de Problemas

### Errores de Compilación Comunes

#### Error: "Could not resolve dependencies"
```bash
# Limpiar caché de Gradle
./gradlew clean
./gradlew --refresh-dependencies
```

#### Error: "Out of memory"
```bash
# Aumentar memoria de Gradle en gradle.properties
org.gradle.jvmargs=-Xmx8g -XX:MaxPermSize=1g
```

#### Error: "SDK not found"
1. Verificar que Android SDK esté instalado
2. Configurar ANDROID_HOME en variables de entorno
3. Sincronizar proyecto en Android Studio

### Errores de Ejecución Comunes

#### La aplicación no se inicia
1. Verificar permisos de accesibilidad
2. Reiniciar el dispositivo
3. Desinstalar y reinstalar la aplicación

#### Reconocimiento de voz no funciona
1. Verificar permisos de micrófono
2. Verificar que los modelos de Vosk se descargaron correctamente
3. Reiniciar el servicio de reconocimiento de voz

#### Llamadas no se manejan automáticamente
1. Verificar permisos de teléfono
2. Configurar como launcher predeterminado
3. Verificar configuración de accesibilidad

## 📦 Generar APK de Release

### 1. Configurar Firma
1. Crear keystore para firma:
```bash
keytool -genkey -v -keystore ritsu-release-key.keystore -alias ritsu-key-alias -keyalg RSA -keysize 2048 -validity 10000
```

2. Configurar en `app/build.gradle`:
```gradle
android {
    signingConfigs {
        release {
            storeFile file("ritsu-release-key.keystore")
            storePassword "your-store-password"
            keyAlias "ritsu-key-alias"
            keyPassword "your-key-password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 2. Generar APK
```bash
# Generar APK de release
./gradlew assembleRelease

# El APK se encontrará en:
# app/build/outputs/apk/release/app-release.apk
```

### 3. Generar Bundle (recomendado para Play Store)
```bash
# Generar Android App Bundle
./gradlew bundleRelease

# El bundle se encontrará en:
# app/build/outputs/bundle/release/app-release.aab
```

## 🔍 Verificación de Calidad

### Pruebas Automatizadas
```bash
# Ejecutar pruebas unitarias
./gradlew test

# Ejecutar pruebas de instrumentación
./gradlew connectedAndroidTest

# Ejecutar análisis de código
./gradlew lint
```

### Pruebas Manuales
1. **Funcionalidad básica**: Verificar que la aplicación se inicia correctamente
2. **Reconocimiento de voz**: Probar comandos de voz
3. **Manejo de llamadas**: Simular llamadas entrantes
4. **Interfaz de usuario**: Verificar que todos los elementos son accesibles
5. **Rendimiento**: Verificar que no hay lag o crashes

## 📊 Métricas de Rendimiento

### Tamaño del APK
- **APK de debug**: ~150-200MB
- **APK de release**: ~100-150MB
- **Bundle de release**: ~80-120MB

### Uso de Memoria
- **RAM mínima**: 2GB
- **RAM recomendada**: 4GB+
- **Almacenamiento**: 1GB+ para modelos de IA

### Tiempo de Inicio
- **Primera ejecución**: 30-60 segundos (descarga de modelos)
- **Ejecuciones posteriores**: 5-10 segundos

## 🚀 Despliegue

### Google Play Store
1. Crear cuenta de desarrollador
2. Subir Android App Bundle (.aab)
3. Configurar ficha de la aplicación
4. Publicar en modo de prueba interna
5. Probar exhaustivamente
6. Publicar en producción

### Distribución Directa
1. Generar APK firmado
2. Distribuir por canales directos
3. Instalar manualmente en dispositivos
4. Configurar permisos manualmente

## 📞 Soporte

Para problemas técnicos o preguntas:
- Crear issue en el repositorio
- Consultar la documentación técnica
- Revisar logs de la aplicación

---

**¡Ritsu AI Assistant está listo para revolucionar tu experiencia móvil!** 🎭🤖