#!/bin/sh
set -e

# Replace environment variable placeholders in config.template.json
envsubst < /usr/share/nginx/html/assets/config.template.json > /usr/share/nginx/html/assets/config.json

# Start Nginx
nginx -g "daemon off;"
