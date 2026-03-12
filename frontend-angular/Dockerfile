# ============================================
# Build Stage
# ============================================
FROM node:22-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies (using legacy-peer-deps for Angular version compatibility)
RUN npm ci --legacy-peer-deps

# Copy source code
COPY . .

# Build production bundle
RUN npm run build

# ============================================
# Runtime Stage
# ============================================
FROM nginx:alpine

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Remove default nginx files and copy built application from builder stage
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist/frontend-angular/browser /usr/share/nginx/html

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
