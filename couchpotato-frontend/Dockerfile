FROM node:14-alpine AS builder

WORKDIR /usr/src/app

COPY package.json package-lock.json ./

RUN npm install

COPY . .

RUN npm run build --prod

FROM nginx:alpine

COPY --from=builder /usr/src/app/dist/couchpotato-frontend/ /usr/share/nginx/html
COPY nginx.default /etc/nginx/conf.d/default.conf
