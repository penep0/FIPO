# Vite는 dist에 빌드 결과물이 생김
FROM node:20-alpine as build
WORKDIR /app
COPY . .
COPY .env.production .env.production
RUN npm install
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
COPY nginx.conf /etc/nginx/conf.d/default.conf
