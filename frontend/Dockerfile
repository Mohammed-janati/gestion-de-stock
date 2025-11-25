FROM node:22.12-alpine as build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

FROM nginx:alpine

WORKDIR /app
COPY nginx.config  /etc/nginx/nginx.conf

COPY  --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Copy entrypoint script and make it executable
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 80
ENTRYPOINT ["/entrypoint.sh"]

