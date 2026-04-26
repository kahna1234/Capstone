FROM nginx:alpine

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf
COPY conf.d/upstreams.conf /etc/nginx/conf.d/upstreams.conf

# Expose ports
EXPOSE 80 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
