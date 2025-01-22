# Stage 1: Build the Angular app
FROM node:18 AS build

WORKDIR /app

# Install dependencies
COPY ./package*.json ./
RUN npm install

# Copy source code and build the Angular app
COPY ./ ./
RUN npm run build -- --output-path=dist --configuration=production

# Stage 2: Serve the app with Nginx
FROM nginx:stable

# Copy built files to Nginx web directory
COPY --from=build /app/dist /usr/share/nginx/html

# Custom Nginx configuration (optional)
COPY ./default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
