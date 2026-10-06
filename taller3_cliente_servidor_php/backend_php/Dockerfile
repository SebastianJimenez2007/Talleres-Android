FROM php:8.2-apache

# Instalar certificados CA para soportar conexiones seguras TLS/SSL con TiDB Cloud
RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

# Instalar extensiones necesarias de MySQL
RUN docker-php-ext-install mysqli pdo pdo_mysql

# Copiar el backend PHP a la raíz de Apache
COPY . /var/www/html/

# Exponer el puerto estándar HTTP
EXPOSE 80
