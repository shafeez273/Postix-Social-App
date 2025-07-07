# postix-frontend/Dockerfile
FROM node:23-alpine AS build

WORKDIR /app

# 1) copy your entire project (including package.json)
COPY package.json package-lock.json ./


# 2) install dependencies
RUN npm install
COPY . .
# 3) build the production bundle
ARG POSTIX_API
# if you need the API URL baked in at build-time:
RUN echo "VITE_APP_POSTIX_API=${POSTIX_API}" > .env
RUN npm run build

# 4) serve with nginx
FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx","-g","daemon off;"]
