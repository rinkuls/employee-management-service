# Stage 1: Build the Angular app
FROM node:18 AS build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy only the Angular source code and build the app
COPY src ./src
COPY angular.json ./
COPY tsconfig*.json ./
RUN npm run build -- --configuration=production

# Stage 2: Serve the app with NGINX
FROM nginx:stable
COPY --from=build /app/dist/employee-management-tool-ui /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
