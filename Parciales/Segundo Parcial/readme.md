# Proyecto Segundo Parcial
### Nombre:
* Geronimo Martinez Nuñez

Este proyecto implementa un sistema distribuido que utiliza un servicio proxy para redirigir solicitudes a dos servicios backend alternativos. El objetivo principal es procesar la secuencia de Collatz y distribuir la carga entre los servicios backend.

## Descripción del Proyecto

El sistema consta de tres componentes principales:

1. **Proxy Service**: Un servicio que actúa como intermediario entre el cliente (frontend) y los servicios backend. Alterna las solicitudes entre dos servicios backend para balancear la carga.
2. **Servicios Backend**: Dos servicios independientes que procesan la lógica de la secuencia de Collatz.
3. **Frontend**: Una interfaz web simple que permite al usuario ingresar un número y obtener la secuencia de Collatz.

## Arquitectura

- **Proxy Service**:
  - Escucha en el puerto `8080`.
  - Redirige las solicitudes a:
    - `http://52.91.9.72:8080/collatzsequence`
    - `http://54.226.14.206:8080/collatzsequence`
  - Alterna entre los servicios backend utilizando una lógica de intercalación.

- **Servicios Backend**:
  - Cada servicio implementa la lógica para calcular la secuencia de Collatz.
  - Responden en formato JSON.

- **Frontend**:
  - Permite al usuario ingresar un número.
  - Envía solicitudes al proxy para obtener la secuencia de Collatz.
  - Muestra el resultado en pantalla.

## Evidencia Despliegue
[![Evidencia Despliegue](https://img.youtube.com/vi/gf_mFVJYBGY/0.jpg)](https://youtu.be/gf_mFVJYBGY)
