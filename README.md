# Sistema de Gestión de Créditos para Comercio Minorista

Aplicación de escritorio (Java Swing + MySQL) para administrar créditos personales otorgados a clientes de una tienda de indumentaria (caso real de una tienda en Trelew, Chubut). Centraliza clientes, créditos, cuotas, pagos y periodos (lotes), reduciendo el manejo manual en planillas y mejorando la trazabilidad.

> Trabajo realizado para la asignatura **SEMINARIO** de la **Licenciatura en Informática – Universidad Siglo XXI**.

---

## 1. Características principales

- ABM de Clientes (alta, modificación, búsqueda / filtrado, baja, listado general).
- Otorgamiento de créditos con:
  - Monto (capital) y cantidad de cuotas.
  - Tasa de interés mensual tomada desde la tabla `variables`.
  - Generación automática de cuotas (capital + interés lineal distribuido).
- Gestión de cuotas: estados pendiente / pagada / mora (mora aplicada en cierre de lote).
- Registro de pagos de cuotas con:
  - Método de pago (efectivo / banco / transferencia).
  - Observaciones opcionales.
  - Generación de recibo imprimible (monoespaciado).
- Anulación de pagos (revierte estado de cuota y potencialmente estado del crédito).
- Administración de lotes (cierre de periodo y creación del siguiente; al cerrar se marcan en mora las cuotas pendientes alcanzadas).
- Cambio de contraseña operativa (y contraseña maestra para acceso de contingencia).
- Consulta de:
  - Créditos y cuotas por cliente (vista maestro–detalle).
  - Pagos por cliente.
  - Cuotas pendientes para cobro (desde panel de pago).
- Estados de crédito: vigente / cancelado.
- Parámetros globales centralizados (tabla `variables`).

---

## 2. Arquitectura

Patrón por capas sencillo:

```
Presentación (Swing Panels / Dialogs)
        ↓
Controladores (Controller.*)
        ↓
Modelo (POJOs en Model.*)
        ↓
DAO / Persistencia (Dao.* con JDBC)
        ↓
Base de Datos MySQL (script creditos.sql)
```

- Patrón DAO para separar SQL de la lógica.
- Transacciones manuales en operaciones críticas:
  - Crear crédito + cuotas
  - Registrar pago
  - Cerrar lote
  - Anular pago
- Sin frameworks adicionales (facilita despliegue).

---

## 3. Modelo de datos (tablas clave)

| Tabla      | Descripción |
|------------|-------------|
| clientes   | Datos de identificación y contacto (documento único). |
| creditos   | Cabecera del crédito (capital, tasa, cantidad de cuotas, estado, lote origen). |
| cuotas     | Cuotas generadas automáticamente (número, monto, estado). |
| pagos      | Registros de pagos individuales de cuotas. |
| lotes      | Periodos operativos (apertura / cierre). |
| variables  | Fila única de configuración (contraseñas, interés, contadores). |

Script completo: `creditos.sql`.

---

## 4. Cálculo de interés y cuotas

Interés lineal mensual:
```
total_con_interes = capital * (1 + n * i)
cuota_base = floor( (total_con_interes / n) * 100 ) / 100
```
Las primeras (n-1) cuotas usan `cuota_base`; la última ajusta centavos (±0.01).  
La tasa se guarda en `variables.interes_mensual` como decimal (ej. 0.0500 → 5.00%).

---

## 5. Requisitos previos

- Java JDK 17+ (recomendado).
- MySQL 8.x (o compatible).
- IDE o herramientas de compilación (IntelliJ / Eclipse / VS Code).
- Driver MySQL JDBC (`mysql-connector-j`).

---

## 6. Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Titoyco/seminario.git
   cd seminario
   ```
2. Crear la base de datos:
   ```sql
   SOURCE /ruta/al/proyecto/creditos.sql;
   ```
3. Ajustar credenciales en `Dao/ConexionMySQL.java`.
4. Compilar:
   ```bash
   javac $(find . -name "*.java")
   ```
5. Ejecutar:
   ```bash
   java MainWindow
   ```

---

## 7. Credenciales iniciales

Tabla `variables`:
- Contraseña operativa: `admin`
- Contraseña maestra: `master`

---

## 8. Flujo de uso básico

1. Iniciar aplicación → login.
2. Alta de Cliente (si corresponde).
3. Alta de Crédito → se generan cuotas.
4. Consulta de Créditos / Cuotas.
5. Pagar Cuota → recibo.
6. Anular pago (si hubo error).
7. Cerrar Lote (fin de periodo) → cuotas pendientes a mora.
8. Consultar listados.

---

## 9. Estructura de directorios (resumen)

```
Controller/       # Lógica de orquestación
Dao/              # Acceso a datos (JDBC)
Model/            # Entidades de dominio (POJOs)
View/             # Panels Swing
creditos.sql      # DDL + datos de ejemplo
MainWindow.java   # Entrada principal
```

---

## 10. Consultas de verificación rápidas (opcional)

- Créditos vigentes:
  ```sql
  SELECT id, id_cliente, estado FROM creditos WHERE estado='vigente';
  ```
- Cuotas pendientes de un crédito:
  ```sql
  SELECT numero, monto, estado FROM cuotas WHERE id_credito = <ID>;
  ```
- Pagos por cliente:
  ```sql
  SELECT p.* FROM pagos p
  JOIN cuotas cu ON cu.id = p.id_cuota
  JOIN creditos cr ON cr.id = cu.id_credito
  WHERE cr.id_cliente = <ID_CLIENTE>;
  ```
- Cuotas en mora:
  ```sql
  SELECT * FROM cuotas WHERE estado='mora';
  ```

---

## 11. Roadmap (mejoras futuras)

- Emisión formal de comprobante de alta de crédito.
- Reportes agregados (mora, montos pendientes, ranking clientes).
- Roles y permisos.
- Exportación a PDF/Excel.
- Notificaciones (recordatorios de mora).
- Hash de contraseñas / política de cambio.
- Auditoría de operaciones.

---

## 12. Datos de ejemplo

El script `creditos.sql` carga clientes, créditos, cuotas, pagos y lotes para demostrar flujos básicos.  
Se pueden limpiar tablas con `TRUNCATE` para comenzar desde cero.

---

## 13. Problemas comunes

| Problema | Causa probable | Solución |
|----------|----------------|----------|
| `Access denied for user` | Credenciales MySQL incorrectas | Editar `ConexionMySQL.java` |
| No se generan cuotas | Tasa nula / cantCuotas <= 0 | Revisar `variables.interes_mensual` y datos ingresados |
| Recibo vacío | Pago no recuperado correctamente | Revisar consola / integridad de datos |
| Error SSL | Parámetros SSL sin configurar | Usar `useSSL=false` en la URL |

---

## 14. Créditos

Proyecto desarrollado como trabajo académico para la materia **SEMINARIO** de la **Licenciatura en Informática – Universidad Siglo XXI**.  
Autor: **Roberto Omar Davies**  
Email: **roberto.davies@gmail.com**

Los datos de ejemplo son ficticios y no corresponden a personas reales.

---

### Resumen rápido (TL;DR)

Java Swing + MySQL.  
Clona → ejecuta `creditos.sql` → ajusta conexión → corre `MainWindow`.  
Login: `admin` (o `master`).  
Otorga créditos, genera cuotas, registra pagos, emite recibos, cierra lotes.

---
