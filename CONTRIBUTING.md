# Contributing to Ritsu AI Assistant

¡Gracias por tu interés en contribuir a Ritsu AI Assistant! Este proyecto está diseñado para revolucionar la experiencia móvil con IA conversacional gratuita y offline.

## 🎯 Cómo Contribuir

### 1. Reportar Bugs
- Usa el template de "Bug Report"
- Incluye pasos para reproducir el problema
- Adjunta logs y capturas de pantalla si es posible
- Especifica la versión de Android y el dispositivo

### 2. Solicitar Funciones
- Usa el template de "Feature Request"
- Describe la funcionalidad deseada
- Explica por qué sería útil
- Considera si es compatible con el enfoque offline/gratuito

### 3. Contribuir Código
- Fork el repositorio
- Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
- Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
- Push a la rama (`git push origin feature/AmazingFeature`)
- Abre un Pull Request

## 🛠️ Configuración del Entorno de Desarrollo

### Requisitos
- Android Studio 2023.1.1+
- JDK 11+
- Android SDK API 26+
- Dispositivo Android 8.0+ para pruebas

### Configuración
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Configura un dispositivo de prueba
5. Ejecuta la aplicación

## 📋 Guías de Código

### Kotlin
- Usa Kotlin como lenguaje principal
- Sigue las convenciones de Kotlin
- Usa coroutines para operaciones asíncronas
- Implementa manejo de errores apropiado

### Arquitectura
- Sigue el patrón MVVM
- Usa Room para la base de datos
- Implementa Repository pattern
- Usa Dependency Injection manual

### UI/UX
- Usa Jetpack Compose para la interfaz
- Sigue Material Design 3
- Mantén el tema anime de Ritsu
- Asegura accesibilidad

### IA y Machine Learning
- Mantén el enfoque offline
- Usa modelos gratuitos y de código abierto
- Optimiza el rendimiento
- Documenta los modelos utilizados

## 🧪 Testing

### Pruebas Unitarias
- Escribe pruebas para ViewModels
- Prueba los repositorios
- Verifica la lógica de negocio
- Mantén cobertura de código alta

### Pruebas de Integración
- Prueba la base de datos
- Verifica los servicios
- Prueba la integración con IA
- Valida el reconocimiento de voz

### Pruebas de UI
- Prueba la interfaz de usuario
- Verifica la navegación
- Prueba en diferentes tamaños de pantalla
- Valida el tema oscuro/claro

## 📝 Documentación

### Código
- Documenta funciones complejas
- Usa comentarios claros
- Mantén README actualizado
- Documenta APIs internas

### Usuario
- Actualiza las instrucciones de instalación
- Documenta nuevas funcionalidades
- Mantén la guía de usuario
- Traduce a español e inglés

## 🔒 Privacidad y Seguridad

### Principios
- Todo el procesamiento debe ser local
- No enviar datos a servidores externos
- Proteger información del usuario
- Implementar encriptación cuando sea necesario

### Verificaciones
- Revisa permisos de la aplicación
- Valida el manejo de datos sensibles
- Verifica la configuración de seguridad
- Prueba el modo privacidad

## 🚀 Proceso de Pull Request

### Antes de Enviar
- [ ] El código compila sin errores
- [ ] Las pruebas pasan
- [ ] El código sigue las guías de estilo
- [ ] La documentación está actualizada
- [ ] Se han probado las nuevas funcionalidades

### Template de Pull Request
```markdown
## Descripción
Breve descripción de los cambios realizados.

## Tipo de Cambio
- [ ] Bug fix
- [ ] Nueva funcionalidad
- [ ] Mejora de rendimiento
- [ ] Documentación
- [ ] Refactoring

## Cambios Realizados
- Lista de cambios específicos

## Pruebas Realizadas
- Descripción de las pruebas

## Capturas de Pantalla (si aplica)
- Adjunta capturas si hay cambios de UI

## Checklist
- [ ] Mi código sigue las guías de estilo
- [ ] He realizado pruebas automáticas
- [ ] He actualizado la documentación
- [ ] Mis cambios no generan nuevos warnings
- [ ] He añadido pruebas que prueban mi fix/feature
```

## 🎨 Temas de Diseño

### Colores de Ritsu
- Rosa principal: `#FF69B4`
- Azul secundario: `#87CEEB`
- Fondo oscuro: `#1A1A2E`
- Fondo medio: `#16213E`

### Tipografía
- Usa Material Design Typography
- Mantén consistencia en tamaños
- Asegura legibilidad

### Iconografía
- Usa Material Icons
- Mantén consistencia visual
- Considera el tema anime

## 🌍 Internacionalización

### Idiomas Soportados
- Español (principal)
- Inglés
- Japonés (básico)

### Traducciones
- Mantén strings.xml actualizado
- Usa placeholders apropiados
- Considera contextos culturales

## 📊 Métricas y Analytics

### Rendimiento
- Monitorea tiempo de inicio
- Verifica uso de memoria
- Optimiza consumo de batería
- Mide tiempo de respuesta de IA

### Calidad
- Mantén cobertura de código alta
- Reduce warnings del compilador
- Optimiza tamaño del APK
- Verifica accesibilidad

## 🤝 Comunidad

### Comunicación
- Sé respetuoso y inclusivo
- Usa español o inglés
- Proporciona feedback constructivo
- Celebra las contribuciones

### Reconocimiento
- Los contribuidores serán reconocidos
- Se mantendrá un archivo de contribuidores
- Se destacarán contribuciones significativas

## 📞 Contacto

Si tienes preguntas sobre cómo contribuir:
- Abre un issue en GitHub
- Revisa la documentación del proyecto
- Consulta los ejemplos existentes

¡Gracias por hacer Ritsu AI Assistant mejor para todos! 🎭🤖✨