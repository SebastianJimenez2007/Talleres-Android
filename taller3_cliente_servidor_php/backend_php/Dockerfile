FROM php:8.2-apache

# Habilitar extensiones necesarias para MySQL
RUN docker-php-ext-install mysqli pdo pdo_mysql

# Copiar el backend PHP a la raíz de Apache
COPY . /var/www/html/

# Exponer el puerto estándar HTTP
EXPOSE 80
