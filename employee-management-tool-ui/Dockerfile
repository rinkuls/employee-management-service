# Stage 1: Build the Angular app
FROM --platform=$BUILDPLATFORM node:18 as build

WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./

RUN npm install
RUN npx ngcc --properties es2023 browser module main --first-only --create-ivy-entry-points

# Copy the rest of the application code
COPY . .

# Build the Angular app
RUN npm run build

# Stage 2: Serve the app with NGINX
# Use a smaller, production-ready image
FROM nginx:stable
COPY default.conf /etc/nginx/conf.d
COPY --from=build /app/dist/employee-management-tool-ui/browser /usr/share/nginx/html

EXPOSE 80
