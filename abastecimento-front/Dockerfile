
# Build stage
FROM node:16.20.2-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist/abastecimento-front /usr/share/nginx/html

# Inject nginx configuration directly
RUN echo 'server { \
    listen 4520; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    location / { \
    try_files $uri $uri/ /index.html; \
    } \
    }' > /etc/nginx/conf.d/default.conf

EXPOSE 4520
CMD ["nginx", "-g", "daemon off;"]
