# Plan de Implementación: Docker & AWS EKS

Este documento detalla el plan paso a paso para transformar el proyecto **Innovatech** de un conjunto de microservicios locales a una arquitectura nativa de la nube, contenerizada con Docker y orquestada en Amazon Web Services (AWS) mediante EKS (Elastic Kubernetes Service).

## User Review Required

> [!CAUTION]
> Desplegar infraestructura en AWS (especialmente un clúster EKS y una instancia RDS) **incurre en costos mensuales**, incluso si la usas poco. Asegúrate de apagar los recursos cuando no los uses si no cuentas con un presupuesto para mantenerlos 24/7.

> [!IMPORTANT]
> El plan propone dividir el trabajo en fases muy marcadas. Es fundamental que **no pasemos a la siguiente fase sin haber validado al 100% la anterior**.

## Open Questions

> [!NOTE]
> 1. **Herramienta de Infraestructura (IaC):** Para crear la VPC, EKS y RDS en AWS, ¿prefieres que escribamos scripts de **Terraform** (recomendado para proyectos formales), que usemos **AWS CLI**, o usar **eksctl** (la forma más fácil y rápida de levantar EKS)?
> 2. **Base de Datos:** El plan asume que usaremos **Amazon RDS (MySQL)** administrado, ya que es la mejor práctica para bases de datos en producción (en lugar de correr MySQL dentro del propio clúster de Kubernetes). ¿Estás de acuerdo con esto?
> 3. **AWS CLI Configuracion:** ¿Ya tienes configurado el `aws-cli` en tu computadora con credenciales válidas (Access Key y Secret Key)?

## Proposed Changes (Step-by-Step)

### FASE 1: Contenerización Local (Docker)
Antes de subir nada a la nube, debemos dockerizar la aplicación.
- **[NEW] Dockerfiles:** Crear un `Dockerfile` (Multi-stage con Java 21) en la raíz de cada microservicio.
- **[NEW] docker-compose.yml:** Crear un entorno local para verificar que los contenedores se construyen y comunican entre sí correctamente (incluyendo un MySQL local desechable para esta prueba).

### FASE 2: Registry de Imágenes (Amazon ECR)
Kubernetes necesita descargar tus imágenes desde algún lugar. Usaremos Elastic Container Registry.
- **Crear Repositorios:** Crear un repositorio ECR para cada microservicio.
- **Build & Push:** Construir las imágenes Docker finales y subirlas (push) a Amazon ECR.

### FASE 3: Infraestructura Base (VPC y RDS)
- **VPC y Subredes:** Crear una VPC personalizada para aislar tu red, con subredes públicas y privadas.
- **Base de Datos (Amazon RDS):** Provisionar una base de datos MySQL en las subredes privadas. El API Gateway y los microservicios se conectarán a este RDS.

### FASE 4: Orquestación (Amazon EKS)
- **Creación del Clúster:** Desplegar el clúster EKS (`innovatech-cluster`) en la VPC creada.
- **Nodos de Trabajo (Node Groups):** Levantar instancias EC2 (ej. `t3.medium`) que actuarán como los workers donde correrán los contenedores.
- **Configuración Local:** Configurar tu `kubectl` local para que apunte a este nuevo clúster de AWS.

### FASE 5: Despliegue en Kubernetes (K8s)
Crearemos una nueva carpeta `k8s/` en tu repositorio con manifiestos YAML:
- **[NEW] k8s/configmap.yaml / k8s/secret.yaml:** Para almacenar la URL de la base de datos RDS, contraseñas y variables de entorno.
- **[NEW] k8s/deployments.yaml:** Para decirle a EKS cuántas réplicas levantar de cada microservicio, apuntando a las imágenes subidas a ECR en la Fase 2.
- **[NEW] k8s/services.yaml:** Para definir la comunicación interna entre los pods de tu clúster.
- **[NEW] k8s/ingress.yaml (o Service LoadBalancer):** Para crear un Application Load Balancer (ALB) público en AWS asociado únicamente al `ms-api_gateway`, exponiendo tu aplicación a internet.

## Verification Plan

1. **Prueba Fase 1:** Levantar el proyecto con `docker-compose up` y validar peticiones HTTP de forma local.
2. **Prueba Fase 3:** Conectar un cliente de base de datos (ej. DBeaver o MySQL Workbench) a la nueva instancia de Amazon RDS para confirmar que es accesible.
3. **Prueba Fase 5:** Ejecutar `kubectl get pods` y `kubectl get services` para verificar que todo esté en estado `Running`. Finalmente, golpear la URL pública provista por AWS (Load Balancer DNS) usando Postman para confirmar que toda la arquitectura está funcionando end-to-end.
