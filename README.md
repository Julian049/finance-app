# FinanceApp

FinanceApp es una aplicación de Android moderna diseñada para la gestión de finanzas personales, con soporte offline-first y sincronización con un servidor remoto.

---

## Características

- Registro de nuevas transacciones (ingresos y gastos)
- Visualización del saldo actual calculado a partir de todas las transacciones
- Persistencia sin conexión con Room 
- Sincronización con API REST mediante Retrofit
- Indicador visual del estado de conexión con el servidor+

---

## Arquitectura

El proyecto sigue el patrón **MVVM** con **Clean Architecture**, organizado en tres capas:

`data/`: Implementaciones de repositorios, base de datos local en Room y servicios remotos con Retrofit

`di/`: Módulos de Hilt para la inyección de dependencias.

`domain/`: Modelos de negocio, interfaces de repositorios y casos de uso.

`ui/`: Pantallas de la aplicación, navegación y temas visuales

`viewmodel/`: Lógica de presentación que comunica la UI con la capa de dominio.

### Casos de uso

| Caso de uso | Descripción |
|---|---|
| `GetTransactionsUseCase` | Obtiene la lista de transacciones (local + remoto) |
| `AddTransactionUseCase` | Registra una nueva transacción localmente |
| `GetTotalAmountUseCase` | Calcula el saldo total |
| `SyncTransactionsUseCase` | Sube las transacciones no sincronizadas al servidor |
| `HealthCheckUseCase` | Verifica la disponibilidad de la API |

---

## Stack tecnológico


| Tecnología | Uso |
|---|---|
| Kotlin | Lenguaje |
| Jetpack Compose | UI |
| Navigation Compose | Navegación entre pantallas |
| Room | Base de datos local |
| Retrofit + Gson | Cliente HTTP / serialización |
| Hilt | Inyección de dependencias |

---

## Pruebas

El proyecto incluye pruebas unitarias con JUnit 4 y MockK.

```bash
./gradlew test
```

Ejemplo: `TransactionRepositoryImplTest` verifica que `getTotalAmount` suma correctamente los valores de una lista de transacciones (ingresos positivos y gastos negativos).

---

## Autor

* **Julian David Bocanegra Segura** - Desarrollador